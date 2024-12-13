package org.smartlink.server.nc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.smartlink.server.nc.annotation.DataColumn;
import org.smartlink.server.nc.annotation.DataPermission;
import org.smartlink.server.nc.domain.SysUser;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author L
 */

public interface SysUserMapperWeb extends BaseMapperPlusWeb<SysUserMapperWeb,SysUser, SysUser> {

    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<SysUser> selectPageUserList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询用户列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    List<SysUser> selectUserList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<SysUser> selectAllocatedList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<SysUser> selectUnallocatedList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    SysUser selectUserByPhonenumber(String phonenumber);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(@Param("userId") Long userId);

    /**
     * 根据userId获取UserName
     * @param userId
     * @return
     */
    @Select("SELECT user_name FROM sys_user WHERE user_id = #{userId} and del_flag = '0'")
    String selectUserNameByUserId(@Param("userId")String userId);

    @Select("SELECT user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, remark, nc_user_id, group_id, nc_user_relevance_identity, nc_user_relevance_code, tenant_id, create_by, create_time, update_by, update_time " +
        "FROM sys_user " +
        "WHERE del_flag = '0' AND (nc_user_id = #{ncUserId} AND create_by = #{createBy})")
    List<SysUser> selectListByNcUserId(@Param("ncUserId") String ncUserId, @Param("createBy") String createBy);

}
