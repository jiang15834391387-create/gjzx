package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.DataBillType;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.SysDept;
import org.smartlink.server.nc.domain.SysUser;
import org.smartlink.server.nc.domain.dto.NcUpdateTaskStateDTO;

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

    /**
     * NCC下事后补扫场景提交后调用
     * @param ncUpdateTaskStateDTO
     * @return
     */
    String updateNcImageStateForReScan(NcUpdateTaskStateDTO ncUpdateTaskStateDTO) throws Exception;

    /**
     * 更改影像状态
     * @param ncUpdateTaskStateDTO 对象
     * @return 结果
     * @throws Exception 异常
     */
    String updateNcImageState(NcUpdateTaskStateDTO ncUpdateTaskStateDTO) throws Exception;

    /**
     * 更改影像状态
     * @param ncUpdateTaskStateDTO 对象
     * @return 结果
     * @throws Exception 异常
     */
    String updateBipImageState(NcUpdateTaskStateDTO ncUpdateTaskStateDTO) throws Exception;

}
