package org.smartlink.web.service.nc;

import org.smartlink.common.core.domain.R;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.modle.BaseEntity;

import java.util.List;

public interface IDataOcrService {

    /**
     * 删除多个
     *
     * @param fileIds 文件ID数组
     * @return String
     */
    R<String> deleteMultipleFile(List<String> fileIds) throws Exception;

    /**
     * 删除ocr信息
     *
     * @param type   发票类型
     * @param fileId 文件id
     */

    void deleteInvoiceDataByType(String type, String fileId) throws ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 删除ocr信息
     *
     * @param baseEntity 发票实体
     * @param fileId     文件id
     */

    void deleteInvoiceData(BaseEntity baseEntity, String fileId);

    /**
     * 根据文件ID查询发票信息(仅供删除使用)
     *
     * @return 发票内容
     */
    List<BaseEntity> ocrQueryByFileIdAll(DataImageFilesInfo filesInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException ;
}
