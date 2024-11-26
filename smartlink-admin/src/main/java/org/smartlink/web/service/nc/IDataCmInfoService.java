package org.smartlink.web.service.nc;

import org.smartlink.web.domain.DataCmInfo;

import java.util.List;

/**
 * 任务、图片中间关联Service接口
 */
public interface IDataCmInfoService {

    List<DataCmInfo> selectDataCmInfoListByBusinessSerialNoList(List<String> businessSerialNoList);

    List<DataCmInfo> selectDataCmInfoByBusinessSerialNo(String businessSerialNo);

}
