package org.smartlink.server.nc.ocr.service;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;

import java.util.List;

/**
 * 识别策略
 *
 * @author L
 */
public interface IOcrStrategy {
    /**
     *
     * @param dataImageFilesInfo 图片实体
     * @param bytes 图片字节
     * @param businessSerialNo 单据主键 eq：税务云所需字段
     * @return 返回k，识别实体
     */
    List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, byte[] bytes) throws Exception;

//    /**
//     * 合同OCR识别
//     * @param dataContract
//     * @param bytes
//     * @return
//     */
//    List<ItemContractListBean> getIdentificationContractData(DataContract dataContract, byte[] bytes);
//
//    /**
//     * 文档全文识别
//     * @param byteOld
//     * @return
//     */
//    CcintDocumentResult getIdentificationDocument(byte[] byteOld) throws Exception;
}
