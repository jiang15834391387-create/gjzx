package org.smartlink.workflow.flowable.config;

import cn.hutool.core.collection.CollUtil;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.smartlink.common.core.utils.StringUtils;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.cfg.TransactionState;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 无人自动跳过（只处理 TASK_CREATED）
 * - 事务提交后(COMMITTED)触发，避免 TASK_CREATED 太早导致 identityLink 还没落库的误判
 * - 无 assignee 且无 candidateUser，candidateGroup(角色)下也无人 -> addComment + complete
 */
@Component
public class AutoSkipFlowableListener implements FlowableEventListener {

    @Lazy
    @org.springframework.beans.factory.annotation.Autowired
    private TaskService taskService;

    @Lazy
    @org.springframework.beans.factory.annotation.Autowired
    private RuntimeService runtimeService;

    @Lazy
    @org.springframework.beans.factory.annotation.Autowired
    private IdentityService identityService;

    @Lazy
    @org.springframework.beans.factory.annotation.Autowired
    private org.smartlink.common.core.service.UserService userService;

    private static final String AUTO_SKIP_COUNT = "AUTO_SKIP_COUNT";
    private static final int AUTO_SKIP_MAX = 20;

    public AutoSkipFlowableListener(TaskService taskService,
                                    RuntimeService runtimeService,
                                    IdentityService identityService,
                                    @Lazy org.smartlink.common.core.service.UserService userService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.identityService = identityService;
        this.userService = userService;
    }

    @Override
    public void onEvent(FlowableEvent event) {
        if (!(event instanceof FlowableEntityEvent entityEvent)) {
            return;
        }
        Object entity = entityEvent.getEntity();
        if (!(entity instanceof TaskEntity taskEntity)) {
            return;
        }

        // COMMITTED 后再查一遍最新 task（避免拿到“创建瞬间的半成品”）
        Task task = taskService.createTaskQuery().taskId(taskEntity.getId()).singleResult();
        if (task == null) {
            return;
        }

        // 已分配办理人：不跳
        if (StringUtils.isNotBlank(task.getAssignee())) {
            return;
        }

        // identityLinks：候选用户/候选组
        List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
        if (CollUtil.isEmpty(links)) {
            // links 为空时不跳，避免误判（有些分配逻辑可能稍后才写入）
            return;
        }

        List<String> candidateUsers = new ArrayList<>();
        List<String> candidateGroups = new ArrayList<>();

        for (IdentityLink link : links) {
            if (!"candidate".equals(link.getType())) {
                continue;
            }
            if (StringUtils.isNotBlank(link.getUserId())) {
                candidateUsers.add(link.getUserId());
            }
            if (StringUtils.isNotBlank(link.getGroupId())) {
                candidateGroups.add(link.getGroupId());
            }
        }

        // 有候选用户：不跳
        if (CollUtil.isNotEmpty(candidateUsers)) {
            return;
        }

        // 没候选用户、也没候选组：确定无人 -> 跳
        if (CollUtil.isEmpty(candidateGroups)) {
            doSkip(task, "自动跳过：节点【" + task.getName() + "】无候选用户/候选组");
            return;
        }

        // 候选组 = 角色 roleId（你们待办就是按 GROUP_ID_ IN (roleIds) 过滤）
        List<Long> roleIds = new ArrayList<>();
        for (String gid : new HashSet<>(candidateGroups)) {
            try {
                roleIds.add(Long.valueOf(gid));
            } catch (Exception e) {
                // gid 不是数字：为了安全，直接不跳（避免误伤）
                return;
            }
        }

        if (CollUtil.isEmpty(roleIds)) {
            return;
        }

        List<Long> userIds = userService.selectUserIdsByRoleIds(roleIds);
        if (CollUtil.isNotEmpty(userIds)) {
            return; // 角色下有人，不跳
        }

        // 角色下无人：跳过
        doSkip(task, "自动跳过：节点【" + task.getName() + "】候选角色无人（roleIds=" + roleIds + "）");
    }

    private void doSkip(Task task, String reason) {
        // 防无限跳（极端：后面所有节点都没人）
        Integer cnt = (Integer) runtimeService.getVariable(task.getProcessInstanceId(), AUTO_SKIP_COUNT);
        if (cnt == null) {
            cnt = 0;
        }
        if (cnt >= AUTO_SKIP_MAX) {
            throw new RuntimeException("自动跳过超过上限(" + AUTO_SKIP_MAX + ")，疑似流程配置/组织架构异常");
        }
        runtimeService.setVariable(task.getProcessInstanceId(), AUTO_SKIP_COUNT, cnt + 1);

        // 写审批意见 + complete（会进入历史审批信息）
        identityService.setAuthenticatedUserId("system");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "AUTO_SKIP", reason);

        Map<String, Object> vars = new HashMap<>();
        vars.put("AUTO_SKIPPED", true);
        taskService.complete(task.getId(), vars);
    }

    @Override
    public boolean isFailOnException() {
        // 建议 false：自动跳过异常不影响引擎主流程（你也可以保持 true）
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        // ✅ 关键：事务生命周期触发
        return true;
    }

    @Override
    public String getOnTransaction() {
        // ✅ 提交后触发
        return TransactionState.COMMITTED.name();
    }

    @Override
    public Collection<? extends org.flowable.common.engine.api.delegate.event.FlowableEventType> getTypes() {
        return List.of(FlowableEngineEventType.TASK_CREATED);
    }
}
