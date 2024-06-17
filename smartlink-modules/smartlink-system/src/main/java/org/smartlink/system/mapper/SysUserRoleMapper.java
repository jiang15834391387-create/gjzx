package org.smartlink.system.mapper;

import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.system.domain.SysUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author Lion Li
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRole, SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
