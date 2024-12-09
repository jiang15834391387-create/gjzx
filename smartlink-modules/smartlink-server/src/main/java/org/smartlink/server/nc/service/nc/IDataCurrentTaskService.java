package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.invoice.bo.DataCurrentTaskBo;
import org.smartlink.server.nc.domain.vo.DataCurrentTaskVo;

import java.util.List;

/**
 * 影像任务Service接口
 */
public interface IDataCurrentTaskService {

    /**
     * 根据流水号查询所属任务
     * @param businessSerialNo
     * @return DataCurrentTask
     */
    DataCurrentTask selectDataCurrentTaskByBusinessSerialNo(String businessSerialNo);

    /**
     * 若task记录不存在，则插入；反之更新
     * @param dataCurrentTask
     * @return
     */
    Boolean insertOrUpdateDataCurrentTaskByBusinessSerialNo(DataCurrentTask dataCurrentTask);

    /**
     * 根据userId获取代办任务列表
     * @param userId
     * @return
     */
    List<DataCurrentTask> getTaskListByUserId(String userId);

    /**
     * 查询任务
     *
     * @param businessSerialNo 任务主键
     * @return 任务
     */
    DataCurrentTaskVo queryById(String businessSerialNo);

    /**
     * 新增任务
     *
     * @param dataCurrentTask 任务
     * @return 结果
     */
    Boolean insertByBo(DataCurrentTaskBo bo);

    /**
     *  更新任务表数据
     * @param dataCurrentTask
     * @return int
     */
    int updateDataCurrentTask(DataCurrentTask dataCurrentTask);
}
