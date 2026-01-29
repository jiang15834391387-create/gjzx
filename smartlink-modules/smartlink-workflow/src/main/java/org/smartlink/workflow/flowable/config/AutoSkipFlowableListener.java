package org.smartlink.workflow.flowable.config;

import cn.hutool.core.collection.CollUtil;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.cfg.TransactionState;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.smartlink.common.core.utils.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 无人自动跳过监听器（事务提交后触发）
 * 重点：使用 ObjectProvider 延迟获取 Flowable Service，避免 processEngine 创建时循环依赖
 */
@Component
public class AutoSkipFlowableListener implements FlowableEventListener {

    private final ObjectProvider<TaskService> taskServiceProvider;
    private final ObjectProvider<RuntimeService> runtimeServiceProvider;
    private final ObjectProvider<IdentityService> identityServiceProvider;
    private final ObjectProvider<org.smartlink.common.core.service.UserService> userServiceProvider;

    private static final String AUTO_SKIP_COUNT = "AUTO_SKIP_COUNT";
    private static final int AUTO_SKIP_MAX = 20;

    public AutoSkipFlowableListener(
        ObjectProvider<TaskService> taskServiceProvider,
        ObjectProvider<RuntimeService> runtimeServiceProvider,
        ObjectProvider<IdentityService> identityServiceProvider,
        ObjectProvider<org.smartlink.common.core.service.UserService> userServiceProvider) {
        this.taskServiceProvider = taskServiceProvider;
        this.runtimeServiceProvider = runtimeServiceProvider;
        this.identityServiceProvider = identityServiceProvider;
        this.userServiceProvider = userServiceProvider;

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

        // 事件真正触发时再取 service（此时 processEngine 已经创建完成）
        TaskService taskService = taskServiceProvider.getIfAvailable();
        RuntimeService runtimeService = runtimeServiceProvider.getIfAvailable();
        IdentityService identityService = identityServiceProvider.getIfAvailable();
        org.smartlink.common.core.service.UserService userService = userServiceProvider.getIfAvailable();

        if (taskService == null || runtimeService == null || identityService == null || userService == null) {
            // 服务还未就绪，直接跳过（一般不会发生）
            return;
        }

        // COMMITTED 后再查一遍最新任务，避免半成品
        Task task = taskService.createTaskQuery().taskId(taskEntity.getId()).singleResult();
        if (task == null) {
            return;
        }
        if (StringUtils.isNotBlank(task.getAssignee())) {
            return;
        }

        List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
        if (CollUtil.isEmpty(links)) {
            // links 为空不跳，避免误判
            return;
        }

        List<String> candidateUsers = new ArrayList<>();
        Set<String> candidateGroups = new HashSet<>();

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

        if (CollUtil.isNotEmpty(candidateUsers)) {
            return;
        }

        if (candidateGroups.isEmpty()) {
            doSkip(taskService, runtimeService, identityService, task,
                "自动跳过：节点【" + task.getName() + "】无候选用户/候选组");
            return;
        }

        // 你们系统 GROUP_ID_ = roleId（数字字符串），因为待办是 GROUP_ID_ IN (roleIds)
        List<Long> roleIds = new ArrayList<>();
        for (String gid : candidateGroups) {
            try {
                roleIds.add(Long.valueOf(gid));
            } catch (Exception e) {
                // 不可解析：不跳过，避免误伤
                return;
            }
        }

        List<Long> userIds = userService.selectUserIdsByRoleIds(roleIds);
        if (CollUtil.isNotEmpty(userIds)) {
            return;
        }
        List<String> roleNames = userService.selectRoleName(roleIds);


        String roleDesc = roleNames.isEmpty()
            ? roleIds.toString()
            : String.join("、", roleNames);

        doSkip(taskService, runtimeService, identityService, task,
            "自动跳过：节点【" + task.getName() + "】候选角色无人（角色：" + roleDesc + "）");

    }

    private void doSkip(TaskService taskService,
                        RuntimeService runtimeService,
                        IdentityService identityService,
                        Task task,
                        String reason) {

        Integer cnt = (Integer) runtimeService.getVariable(task.getProcessInstanceId(), AUTO_SKIP_COUNT);
        if (cnt == null) {
            cnt = 0;
        }
        if (cnt >= AUTO_SKIP_MAX) {
            throw new RuntimeException("自动跳过超过上限(" + AUTO_SKIP_MAX + ")，疑似流程配置/组织架构异常");
        }
        runtimeService.setVariable(task.getProcessInstanceId(), AUTO_SKIP_COUNT, cnt + 1);

        identityService.setAuthenticatedUserId("system");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "AUTO_SKIP", reason);

        Map<String, Object> vars = new HashMap<>();
        vars.put("AUTO_SKIPPED", true);
        taskService.complete(task.getId(), vars);
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return true;
    }

    @Override
    public String getOnTransaction() {
        return TransactionState.COMMITTED.name();
    }

    @Override
    public Collection<? extends org.flowable.common.engine.api.delegate.event.FlowableEventType> getTypes() {
        return List.of(FlowableEngineEventType.TASK_CREATED);
    }
}
