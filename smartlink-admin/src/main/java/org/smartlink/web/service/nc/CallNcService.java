package org.smartlink.web.service.nc;


import org.smartlink.web.domain.DataBillType;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.SysDept;
import org.smartlink.web.domain.SysUser;

import java.util.List;

/**
 * 调用NCC接口
 *
 * @author L
 */
public interface CallNcService {

    /**
     * 同步用户
     */
    List<SysUser> synchronizeUser(String factoryCode, String dataSource, String webUrl) throws Exception;

    /**
     * 同步组织
     */
    List<SysDept> synchronizeDepartment(String factoryCode, String dataSource, String webUrl) throws Exception;

    /**
     * 同步单据类型
     */
    List<DataBillType> synchronizeBillType(String factoryCode, String dataSource, String webUrl) throws Exception;

    /**
     * 拉取影像任务
     */
    DataCurrentTask synchronizeTaskFromNc(String factoryCode, String dataSource, String groupId, String barCode, String userId, String webUrl) throws Exception;
}
