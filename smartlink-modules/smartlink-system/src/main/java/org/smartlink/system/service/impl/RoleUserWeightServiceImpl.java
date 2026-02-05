package org.smartlink.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.system.domain.bo.ParticipantQueryBo;
import org.smartlink.system.domain.vo.RoleUserWeightDTO;
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.mapper.SysUserMapper;
import org.smartlink.system.mapper.SysUserRoleMapper;
import org.smartlink.system.service.RoleUserWeightService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.smartlink.system.domain.vo.RoleUserWeightVO;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RoleUserWeightServiceImpl implements RoleUserWeightService {

    private static final String INITIATOR = "initiator";
    private final SysUserRoleMapper userRoleMapper;
    private final TaskService taskService;
    private final SysUserMapper sysUserMapper;
    private final org.springframework.beans.factory.ObjectProvider<org.flowable.engine.RuntimeService> runtimeServiceProvider;

    public RoleUserWeightServiceImpl(SysUserRoleMapper userRoleMapper, TaskService taskService,
                                     SysUserMapper sysUserMapper,
                                     org.springframework.beans.factory.ObjectProvider<org.flowable.engine.RuntimeService> runtimeServiceProvider) {
        this.userRoleMapper = userRoleMapper;
        this.taskService = taskService;
        this.sysUserMapper = sysUserMapper;
        this.runtimeServiceProvider = runtimeServiceProvider;
    }

    @Override
    public List<RoleUserWeightVO> listRoleUserWeight(Long roleId) {
        return userRoleMapper.selectRoleUserWeight(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleUserWeight(List<RoleUserWeightDTO> list) {
        for (RoleUserWeightDTO dto : list) {
            if (dto.getWeight() == null || dto.getWeight() < 1 || dto.getWeight() > 100) {
                throw new ServiceException("权重必须在 1~100 之间");
            }
            userRoleMapper.updateUserRoleWeight(
                dto.getRoleId(),
                dto.getUserId(),
                dto.getWeight()
            );
        }
    }

    @Override
    public List<RoleUserWeightDTO> listUserWeightByRoleIds(List<Long> roleIds) {
        return userRoleMapper.selectUserWeightByRoleIds(roleIds);
    }

    @Override
    public List<SysUserVo> listParticipants(ParticipantQueryBo bo) {

        String taskId = bo == null ? null : bo.getTaskId();
        if (StringUtils.isBlank(taskId)) {
            return Collections.emptyList();
        }

        // 1) 校验任务是否存在
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return Collections.emptyList();
        }

        // 2) 获取当前任务 identity links（候选用户/候选组）
        List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
        if (CollUtil.isEmpty(links)) {
            return Collections.emptyList();
        }

        Set<Long> candidateUserIds = new HashSet<>();
        Set<Long> candidateRoleIds = new HashSet<>();

        for (IdentityLink link : links) {
            if (!"candidate".equals(link.getType())) {
                continue;
            }

            // 候选用户
            if (StringUtils.isNotBlank(link.getUserId())) {
                try {
                    candidateUserIds.add(Long.valueOf(link.getUserId()));
                } catch (Exception ignore) {
                }
            }

            // 候选组（你的系统 groupId = roleId）
            if (StringUtils.isNotBlank(link.getGroupId())) {
                try {
                    candidateRoleIds.add(Long.valueOf(link.getGroupId()));
                } catch (Exception ignore) {
                }
            }
        }

        List<SysUserVo> list = userRoleMapper.selectTaskAuthorizedUserList(
            taskId,
            bo.getPhonenumber(),
            bo.getEmail(),
            bo.getUserName(),
            bo.getNickName()
        );
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 3.1 去重
        Map<Long, SysUserVo> uniq = new LinkedHashMap<>();
        for (SysUserVo u : list) {
            if (u != null && u.getUserId() != null) {
                uniq.putIfAbsent(u.getUserId(), u);
            }
        }
        List<SysUserVo> users = new ArrayList<>(uniq.values());

        // 3.2 不允许转给自己（避免误操作）
        Long currentUserId = LoginHelper.getUserId();
        if (currentUserId != null) {
            users = users.stream()
                .filter(u -> u.getUserId() != null && !u.getUserId().equals(currentUserId))
                .collect(Collectors.toList());
        }

        Long starterId = null;
        try {
            RuntimeService runtimeService = runtimeServiceProvider.getIfAvailable();
            if (runtimeService != null) {
                Object starterVar = runtimeService.getVariable(task.getProcessInstanceId(), INITIATOR);
                if (starterVar != null && StringUtils.isNotBlank(String.valueOf(starterVar))) {
                    starterId = Long.valueOf(String.valueOf(starterVar));
                }
            }
        } catch (Exception ignore) {
        }

        if (starterId != null) {
            Long sid = starterId;
            boolean containsStarter = users.stream().anyMatch(u -> sid.equals(u.getUserId()));
            if (containsStarter) {
                boolean hasOther = users.stream().anyMatch(u -> u.getUserId() != null && !sid.equals(u.getUserId()));
                if (hasOther) {
                    users = users.stream()
                        .filter(u -> u.getUserId() != null && !sid.equals(u.getUserId()))
                        .collect(Collectors.toList());
                } else {
                    return Collections.emptyList();
                }
            }
        }

        return users;

    }
}
