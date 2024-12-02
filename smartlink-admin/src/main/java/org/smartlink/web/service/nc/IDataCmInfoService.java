package org.smartlink.web.service.nc;

import org.smartlink.web.domain.DataCmInfo;

import java.util.List;

/**
 * 任务、图片中间关联Service接口
 */
public interface IDataCmInfoService {

    List<DataCmInfo> selectDataCmInfoListByBusinessSerialNoList(List<String> businessSerialNoList);

    List<DataCmInfo> selectDataCmInfoByBusinessSerialNo(String businessSerialNo);

    /**
     * ybz保存中间表
     * @param barcode   友报账编号
     * @return
     */
    DataCmInfo saveCminfo(String barcode);

    /**
     * 根据barCode查询
     *
     * @param barCode   友报账编号
     * @return
     */
    List<DataCmInfo> findAllByBarCode(String barCode);

    /**
     * 根据批次号查询
     *
     * @param batchId   批次号
     * @return
     */
    List<DataCmInfo> findAllByBatchId(String batchId);

}
