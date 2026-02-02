package org.smartlink.workflow.flowable.config;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.cfg.TransactionState;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.workflow.common.constant.FlowConstant;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.concurrent.Executor;
import java.util.*;

/**
 * 自动跳过监听器
 */
@Component
@Slf4j
public class AutoSkipFlowableListener implements FlowableEventListener {

    private final ObjectProvider<TaskService> taskServiceProvider;
    private final ObjectProvider<RuntimeService> runtimeServiceProvider;
    private final ObjectProvider<IdentityService> identityServiceProvider;
    private final ObjectProvider<HistoryService> historyServiceProvider;
    private final ObjectProvider<org.smartlink.common.core.service.UserService> userServiceProvider;

    private static final String AUTO_SKIP_COUNT = "AUTO_SKIP_COUNT";
    private static final int AUTO_SKIP_MAX = 20;

    @Autowired
    @Qualifier("wfSkipExecutor")
    private Executor wfSkipExecutor;

    public AutoSkipFlowableListener(
        ObjectProvider<TaskService> taskServiceProvider,
        ObjectProvider<RuntimeService> runtimeServiceProvider,
        ObjectProvider<IdentityService> identityServiceProvider,
        ObjectProvider<HistoryService> historyServiceProvider,
        ObjectProvider<org.smartlink.common.core.service.UserService> userServiceProvider
    ) {
        this.taskServiceProvider = taskServiceProvider;
        this.runtimeServiceProvider = runtimeServiceProvider;
        this.identityServiceProvider = identityServiceProvider;
        this.historyServiceProvider = historyServiceProvider;
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

        TaskService taskService = taskServiceProvider.getIfAvailable();
        RuntimeService runtimeService = runtimeServiceProvider.getIfAvailable();
        IdentityService identityService = identityServiceProvider.getIfAvailable();
        HistoryService historyService = historyServiceProvider.getIfAvailable();
        org.smartlink.common.core.service.UserService userService = userServiceProvider.getIfAvailable();

        if (taskService == null || runtimeService == null || identityService == null || historyService == null || userService == null) {
            return;
        }

        Task task = taskService.createTaskQuery().taskId(taskEntity.getId()).singleResult();
        if (task == null) {
            return;
        }

        //强制不处理流程第一个用户任务（提交节点）
        Task firstTask = taskService.createTaskQuery()
            .processInstanceId(task.getProcessInstanceId())
            .orderByTaskCreateTime()
            .asc()
            .listPage(0, 1)   // 只查第一个，性能更好
            .stream()
            .findFirst()
            .orElse(null);

        boolean isFirstUserTask = (firstTask != null)
            && StringUtils.isNotBlank(firstTask.getId())
            && firstTask.getId().equals(task.getId());

        if (isFirstUserTask) {
            log.debug("AutoSkipFlowableListener：首个用户任务不处理，taskId={}, name={}", task.getId(), task.getName());
            return;
        }


        // 已有办理人不处理
        if (StringUtils.isNotBlank(task.getAssignee())) {
            return;
        }

        List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
        if (CollUtil.isEmpty(links)) {
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

        // 取提交流程发起人
        Long starterId = getStarterId(runtimeService, historyService, task.getProcessInstanceId());

        if (CollUtil.isNotEmpty(candidateUsers)) {

            // 1) 把 candidateUsers 转成 Long（过滤非数字）
            List<Long> candidateUserIds = new ArrayList<>();
            for (String uid : candidateUsers) {
                try {
                    candidateUserIds.add(Long.valueOf(uid));
                } catch (Exception ignore) {
                }
            }

            // 2) 查真实存在的用户（避免配置了用户但用户已被删除）
            List<org.smartlink.common.core.domain.dto.UserDTO> userList = CollUtil.isEmpty(candidateUserIds)
                ? Collections.emptyList()
                : userService.selectListByIds(candidateUserIds);

            Set<Long> realUserIds = new HashSet<>();
            if (CollUtil.isNotEmpty(userList)) {
                for (org.smartlink.common.core.domain.dto.UserDTO u : userList) {
                    if (u != null && u.getUserId() != null) {
                        realUserIds.add(u.getUserId());
                    }
                }
            }

            // 3) 候选用户无人（真实用户为空） -> 先尝试候选组；若也没有组就直接跳过
            if (CollUtil.isEmpty(realUserIds)) {
                // 有候选组：继续走候选组判断（不要直接 return/skip）
                if (CollUtil.isEmpty(candidateGroups)) {
                    doSkip(taskService, runtimeService, identityService, task,
                        "自动跳过：节点【" + task.getName() + "】候选用户无人");
                    return;
                }
                // 继续往下走候选组逻辑
            } else {
                // 4) 只有提交人一个候选用户 -> 先尝试候选组；若也没有组就跳过
                if (starterId != null) {
                    boolean hasOtherApprover = realUserIds.stream().anyMatch(uid -> uid != null && !uid.equals(starterId));
                    if (!hasOtherApprover) {
                        if (CollUtil.isEmpty(candidateGroups)) {
                            doSkip(taskService, runtimeService, identityService, task,
                                "自动跳过：节点【" + task.getName() + "】候选用户仅提交人（userId=" + starterId + "）");
                            return;
                        }
                        // 有候选组：继续走候选组逻辑（别直接 return）
                    } else {
                        // 候选用户里存在除提交人外其他人：不跳过（结束）
                        return;
                    }
                } else {
                    // 没拿到提交人id，且候选用户存在：按“有人”处理，不跳过
                    return;
                }
            }
        }

        // 没有候选用户也没有候选组 -> 直接跳过
        if (candidateGroups.isEmpty()) {
            doSkip(taskService, runtimeService, identityService, task,
                "自动跳过：节点【" + task.getName() + "】无候选用户/候选组");
            return;
        }

        List<Long> roleIds = new ArrayList<>();
        for (String gid : candidateGroups) {
            try {
                roleIds.add(Long.valueOf(gid));
            } catch (Exception e) {
                // 不可解析：不跳过，避免误伤
                return;
            }
        }

        // 角色下的所有用户
        List<Long> roleUserIds = userService.selectUserIdsByRoleIds(roleIds);

        // 2.1 角色下完全无人 -> 跳过
        if (CollUtil.isEmpty(roleUserIds)) {
            String roleDesc = buildRoleDesc(userService, roleIds);
            doSkip(taskService, runtimeService, identityService, task,
                "自动跳过：节点【" + task.getName() + "】候选角色无人（角色：" + roleDesc + "）");
            return;
        }

        // 2.2 角色下有人，但只有提交人一个 -> 跳过
        if (starterId != null) {
            boolean hasOtherApprover = roleUserIds.stream().anyMatch(uid -> uid != null && !uid.equals(starterId));
            if (!hasOtherApprover) {
                String roleDesc = buildRoleDesc(userService, roleIds);
                doSkip(taskService, runtimeService, identityService, task,
                    "自动跳过：节点【" + task.getName() + "】候选角色仅提交人（角色：" + roleDesc + "）");
                return;
            }
        }

        // 2.3 角色下有除提交人外其他人
    }

    private Long getStarterId(RuntimeService runtimeService, HistoryService historyService, String processInstanceId) {
        String starterIdStr = null;

        try {
            Object starterVar = runtimeService.getVariable(processInstanceId, FlowConstant.INITIATOR);
            if (starterVar != null) {
                starterIdStr = String.valueOf(starterVar);
            }
        } catch (Exception ignore) {
        }

        if (StringUtils.isBlank(starterIdStr)) {
            try {
                HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
                if (hpi != null && StringUtils.isNotBlank(hpi.getStartUserId())) {
                    starterIdStr = hpi.getStartUserId();
                }
            } catch (Exception ignore) {
            }
        }

        try {
            if (StringUtils.isNotBlank(starterIdStr)) {
                return Long.valueOf(starterIdStr);
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    private String buildRoleDesc(org.smartlink.common.core.service.UserService userService, List<Long> roleIds) {
        try {
            List<String> roleNames = userService.selectRoleName(roleIds);
            if (roleNames != null && !roleNames.isEmpty()) {
                return String.join("、", roleNames);
            }
        } catch (Exception ignore) {
        }
        return roleIds.toString();
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
        final String taskId = task.getId();
        final String procInstId = task.getProcessInstanceId();
        final String finalReason = reason;
        wfSkipExecutor.execute(() -> {
            try {
                identityService.setAuthenticatedUserId("system");
                taskService.addComment(taskId, procInstId, "AUTO_SKIP", finalReason);

                Map<String, Object> vars = new HashMap<>();
                vars.put("AUTO_SKIPPED", true);

                taskService.complete(taskId, vars);
            } catch (Exception e) {
                log.error("Auto skip failed, taskId={}, reason={}", taskId, finalReason, e);
            }
        });
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
