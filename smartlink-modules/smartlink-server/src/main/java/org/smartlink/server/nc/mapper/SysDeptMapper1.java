package org.smartlink.server.nc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.server.nc.annotation.DataColumn;
import org.smartlink.server.nc.annotation.DataPermission;
import org.smartlink.server.nc.domain.SysDept;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author L
 */
public interface SysDeptMapper1 extends BaseMapperPlus<SysDept, SysDept> {

    /**
     * 查询部门管理数据
     *
     * @param queryWrapper 查询条件
     * @return 部门信息集合
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id")
    })
    List<SysDept> selectDeptList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    List<String> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);




    @Select("SELECT sd.dept_code,sd.dept_name\n" +
            "FROM sys_dept sd,sys_role_dept srd,sys_user_role sur\n" +
            "where sd.dept_id=srd.dept_id and srd.role_id=sur.role_id\n" +
            "and sd.del_flag='0'and sur.user_id=#{userId}")
    SysDept selectDeptName(String userId);

}
