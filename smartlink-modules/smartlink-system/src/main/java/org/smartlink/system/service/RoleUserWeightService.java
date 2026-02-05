package org.smartlink.system.service;

import org.smartlink.system.domain.vo.RoleUserWeightDTO;
import org.smartlink.system.domain.vo.RoleUserWeightVO;
import org.smartlink.system.domain.vo.SysUserVo;

import java.util.List;

public interface RoleUserWeightService {

    /**
     * 查询角色下用户权重
     */
    List<RoleUserWeightVO> listRoleUserWeight(Long roleId);

    /**
     * 批量保存权重
     */
    void saveRoleUserWeight(List<RoleUserWeightDTO> list);

    /**
     * 流程用：查询角色用户权重
     */
    List<RoleUserWeightDTO> listUserWeightByRoleIds(List<Long> roleIds);

    List<SysUserVo> listParticipants(String taskId);
}
