package org.smartlink.web.service.nc;

import org.smartlink.web.domain.DataCmInfo;

import java.util.List;

public interface IDataCmInfoService {

    List<DataCmInfo> selectDataCmInfoListByBusinessSerialNoList(List<String> businessSerialNoList);
}
