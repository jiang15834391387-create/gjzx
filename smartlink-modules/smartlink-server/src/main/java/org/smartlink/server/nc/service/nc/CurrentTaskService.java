package org.smartlink.server.nc.service.nc;

import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.dto.TaskSubmitDTO;
import org.smartlink.server.nc.domain.dto.UpdateTaskDTO;
import org.smartlink.server.nc.domain.invoice.bo.DataCurrentTaskBo;
import org.smartlink.server.nc.domain.scan.response.InitializationResponse;


public interface CurrentTaskService {

    /**
     * 提交影像状态
     * @param taskSubmitDTO 提交DTO
     * @return 结果
     */
    R<DataCurrentTask> submitTaskState(TaskSubmitDTO taskSubmitDTO);

    /**
     * 批量提交影像状态
     * @param taskSubmitDTO 提交DTO
     * @return 结果
     */
    R<DataCurrentTask> batchSubmitTaskState(TaskSubmitDTO taskSubmitDTO);

    /**
     * 驳回影像状态
     * @param updateTaskDTO 驳回dto
     * @return 结果
     */
    R<Void> rejectTaskState(UpdateTaskDTO updateTaskDTO);

    /**
     * 初始化 单据、图片中间表
     *
     * @param businessSerialNo 单据流水号
     * @return
     */
    InitializationResponse initialize(String businessSerialNo);

    /**
     * 查询是否存在该单据，有就查询，没有新增
     *
     * @param task 单据流水号
     * @return 新增后的实体
     */
    DataCurrentTask selectInsert(DataCurrentTaskBo task);
}
