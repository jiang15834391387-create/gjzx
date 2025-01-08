package org.dromara.common.ocr.core;



import org.dromara.common.ocr.entity.IdentificationData;
import org.dromara.common.ocr.exception.OcrException;
import org.smartlink.business.domain.DataImageFilesInfo;

/**
 * <p>Title: ChangeIdentifyInfo</p>
 * <p>Description: 将不同厂商识别信息转换为实体类</p>
 * @author dataFly
 **/
public interface ChangeIdentifyInfo<T> {

    IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, T t) throws OcrException;

//    default NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO reverseConversion(BaseEntity baseEntity) {
//        return null;
//    }



}
