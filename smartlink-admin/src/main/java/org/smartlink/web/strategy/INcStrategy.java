package org.smartlink.web.strategy;

import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.dto.NcDeleteServiceDTO;
import org.smartlink.web.domain.dto.NcImageServiceDTO;
import org.smartlink.web.domain.dto.TaskSubmitDTO;
import org.smartlink.web.domain.dto.UpdateTaskDTO;

public interface INcStrategy {
    /**
     * NC业务删除发票逻辑
     * @param ncDeleteServiceDTO
     */
    void deleteNcInvoiceDataBusinessService(NcDeleteServiceDTO ncDeleteServiceDTO) throws Exception;

    /**
     * 业务系统提交影像状态逻辑
     * @param taskSubmitDTO
     * @return
     */
    DataCurrentTask submitTaskStateToBusinessService(TaskSubmitDTO taskSubmitDTO) throws Exception;

    /**
     * 业务系统驳回影像状态逻辑
     * @param updateTaskDTO
     */
    DataCurrentTask rejectTaskStateBusinessService(UpdateTaskDTO updateTaskDTO) throws Exception;

    /**
     * NC业务上传发票逻辑
     * @param ncImageServiceDTO
     * @return 返回结果
     */
    DataImageFilesInfo doBusinessService(NcImageServiceDTO ncImageServiceDTO) throws ClassNotFoundException;
}
