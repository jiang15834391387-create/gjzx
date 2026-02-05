package org.smartlink.system.mapper;

import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.system.domain.SysUserRole;
import org.smartlink.system.domain.vo.RoleUserWeightDTO;
import org.smartlink.system.domain.vo.RoleUserWeightVO;
import org.smartlink.system.domain.vo.SysUserVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author Lion Li
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRole, SysUserRole> {

    List<SysUserVo> selectTaskAuthorizedUserList(@Param("taskId") String taskId,
                                                 @Param("phonenumber") String phonenumber,
                                                 @Param("email") String email,
                                                 @Param("userName") String userName,
                                                 @Param("nickName") String nickName);

    List<Long> selectUserIdsByRoleId(Long roleId);
    /**
     * 查询角色下所有用户 + 权重
     */
    List<RoleUserWeightVO> selectRoleUserWeight(@Param("roleId") Long roleId);

    /**
     * 更新用户在角色下的权重
     */
    int updateUserRoleWeight(@Param("roleId") Long roleId,
                             @Param("userId") Long userId,
                             @Param("weight") Integer weight);

    /**
     * 方案B：按角色查询用户 + 权重（给流程用）
     */
    List<RoleUserWeightDTO> selectUserWeightByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 多角色候选下，选择排除提交人后的权重最高用户（权重取该用户在这些角色中的 MAX(weight)）
     */
    Long selectTopUserIdByRoleIds(@Param("roleIds") List<Long> roleIds,
                                  @Param("starterId") Long starterId);
}
