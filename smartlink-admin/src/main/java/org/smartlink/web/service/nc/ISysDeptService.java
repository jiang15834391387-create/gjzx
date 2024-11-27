package org.smartlink.web.service.nc;

import org.smartlink.web.domain.SysDept;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author L
 */
public interface ISysDeptService {
    /**
     * 添加所有同步过来的部门数据
     * @param deptList
     * @return
     */
    Boolean insertAllExternalDept(List<SysDept> deptList);

    /**
     * 删除所有同步过来的部门数据
     * @return
     */
    Boolean deleteAllExternalDept();

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    SysDept selectDeptById(String deptId);

}
