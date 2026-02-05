package org.smartlink.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.system.domain.vo.RoleUserWeightDTO;
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.mapper.SysUserMapper;
import org.smartlink.system.mapper.SysUserRoleMapper;
import org.smartlink.system.service.RoleUserWeightService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.smartlink.system.domain.vo.RoleUserWeightVO;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleUserWeightServiceImpl implements RoleUserWeightService {

    private final SysUserRoleMapper userRoleMapper;
    private final TaskService taskService;
    private final SysUserMapper sysUserMapper;
    public RoleUserWeightServiceImpl(SysUserRoleMapper userRoleMapper, TaskService taskService, SysUserMapper sysUserMapper) {
        this.userRoleMapper = userRoleMapper;
        this.taskService = taskService;
        this.sysUserMapper = sysUserMapper;
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
    public List<SysUserVo> listParticipants(String taskId) {

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

            // 3) 统一走 Mapper：一次性查询“当前task的所有可办理用户”
            // ✅ 修改点：这里不在 Java 手动 join 角色用户，而是走 SQL union（更稳、更快、也方便加 deptName）
            return userRoleMapper.selectTaskAuthorizedUserList(taskId);
        }
}
