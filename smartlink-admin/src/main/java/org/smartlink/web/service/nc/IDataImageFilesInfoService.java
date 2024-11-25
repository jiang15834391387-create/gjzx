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

    /**
     * 根据BatchId查询
     *
     * @param batchId
     * @return
     */
    List<DataImageFilesInfo> selectByBatchId(String batchId);

    /**
     * 根据文件ID查询
     *
     * @param fileId 文件ID
     * @return {@link DataImageFilesInfo}
     */
    DataImageFilesInfo selectById(String fileId);
}
