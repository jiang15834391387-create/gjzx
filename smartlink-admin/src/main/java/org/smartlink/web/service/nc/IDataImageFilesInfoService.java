package org.smartlink.web.service.nc;

import org.smartlink.web.domain.DataImageFilesInfo;

import java.util.List;

public interface IDataImageFilesInfoService {

    /**
     * 根据batchId集合查询图片列表
     * @param batchIds batchIds
     * @return
     */
    List<DataImageFilesInfo> selectAllByBatchIdList(List<String> batchIds);
}
