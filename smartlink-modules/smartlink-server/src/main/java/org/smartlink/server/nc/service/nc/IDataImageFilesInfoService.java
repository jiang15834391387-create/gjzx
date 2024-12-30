package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.DataImageFilesInfo;

import java.util.List;

/**
 * 图片文件Service接口
 */
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
     * 根据type查询list
     *
     */
    List<DataImageFilesInfo> selectAllByType(String type);

    /**
     * 根据文件ID查询
     *
     * @param fileId 文件ID
     * @return {@link DataImageFilesInfo}
     */
    DataImageFilesInfo selectById(String fileId);

    Boolean updateById(DataImageFilesInfo filesInfo);

    /**
     * 根据文件ID查询
     *
     * @param fileIds fileIds
     * @return {@link DataImageFilesInfo}
     */
    List<DataImageFilesInfo> selectDataImageFilesInfoListByFileIdList(List<String> fileIds);

    /**
     * 根据BatchId和Cip查询
     *
     * @param batchId 此批号
     * @param cip 来源
     * @return DataImageFilesInfo
     */
    List<DataImageFilesInfo> selectByBatchIdAndCip(String batchId , String cip);

    /**
     * 根据barCode模糊查询图片列表
     * @param barCode barCode
     * @return
     */
    List<DataImageFilesInfo> fuzzySelectAllByBarCode(String barCode);

    /**
     * 新增文件
     *
     * @param filesInfo @{@link DataImageFilesInfo}
     * @return Boolean
     */
    Boolean insert(DataImageFilesInfo filesInfo);

    /**
     * 根据ID删除
     *
     * @param fileId 文件ID
     * @return @{@link Boolean}
     */
    Boolean deleteById(String fileId);
}
