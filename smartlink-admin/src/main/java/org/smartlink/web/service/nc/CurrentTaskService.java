package org.smartlink.web.service.nc;

import org.smartlink.common.core.domain.R;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.dto.TaskSubmitDTO;
import org.smartlink.web.domain.dto.UpdateTaskDTO;

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
}
