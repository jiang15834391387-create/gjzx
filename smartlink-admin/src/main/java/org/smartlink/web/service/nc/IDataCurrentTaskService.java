package org.smartlink.web.service.nc;

import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.invoice.bo.DataCurrentTaskBo;

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
     * 新增任务
     *
     * @param dataCurrentTask 任务
     * @return 结果
     */
    Boolean insertByBo(DataCurrentTaskBo bo);
}
