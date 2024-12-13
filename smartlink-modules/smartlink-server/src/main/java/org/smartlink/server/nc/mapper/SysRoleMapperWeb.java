package org.smartlink.server.nc.mapper;

import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.system.domain.SysRole;
import org.smartlink.system.domain.vo.SysRoleVo;

import java.util.List;

public interface SysRoleMapperWeb extends BaseMapperPlus<SysRole, SysRoleVo> {
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);
}
