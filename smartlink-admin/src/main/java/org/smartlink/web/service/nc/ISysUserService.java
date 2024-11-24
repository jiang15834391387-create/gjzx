package org.smartlink.web.service.nc;


import org.smartlink.web.domain.SysUser;

import java.util.List;

public interface ISysUserService {

    /**
     * 删除所有同步过来的用户数据
     * @return
     */
    Boolean deleteAllExternalUser();

    /**
     * 批量添加同步业务系统的用户
     * @return
     */
    Boolean insertAllExternalUser(List<SysUser> userList);

    /**
     * 查询业务系统用户信息
     * @param ncUserId
     * @return
     */
    List<SysUser> selectListByNcUserId(String ncUserId);

    /**
     * 根据userId查询userName
     * @param userId
     * @return
     */
    String selectUserNameByUserId(String userId);
}
