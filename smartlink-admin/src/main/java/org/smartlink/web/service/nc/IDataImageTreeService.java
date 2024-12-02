package org.smartlink.web.service.nc;

import org.smartlink.web.domain.imagefilesinfo.DataImageTree;

import java.util.List;

public interface IDataImageTreeService {

    /**
     * 影像扫描页面提交单据前判断自定义节点下是否有发票
     * @param productName 自定义节点名称集合
     * @return
     */
    boolean verifyImageTree(List<String> productName, String batchId);

    /**
     * 插入或者更新
     * @param dataImageTree 实体
     * @return 是否成功
     */
    boolean saveOrUpdate(DataImageTree dataImageTree);

    /**
     * 根据文件id集合查询数据
     * @param fileIdList
     * @return
     */
    List<DataImageTree> selectDataImageTreeByFileIdList(List<String> fileIdList);

    /**
     * 根据fileId查询树表
     * @param fileId
     */
    DataImageTree selectImageTreeByFileId(String fileId);

    /**
     * 根据fileId删除树表
     * @param fileId
     */
    Boolean deleteImageTreeByFileId(String fileId);
}
