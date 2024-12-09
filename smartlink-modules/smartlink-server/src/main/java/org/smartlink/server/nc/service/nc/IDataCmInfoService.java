package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.DataCmInfoBo;
import org.smartlink.server.nc.domain.vo.DataCmInfoVo;

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

    /**
     * 查询任务、图片中间关联列表
     *
     * @param dataCmInfo 任务、图片中间关联
     * @return 任务、图片中间关联集合
     */
    List<DataCmInfoVo> queryList(DataCmInfoBo bo);

    /**
     * 修改任务、图片中间关联
     *
     * @param dataCmInfo 任务、图片中间关联
     * @return 结果
     */
    Boolean insertByBo(DataCmInfoBo bo);

}
