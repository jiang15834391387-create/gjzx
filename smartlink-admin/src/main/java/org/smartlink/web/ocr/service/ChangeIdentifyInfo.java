package org.smartlink.web.ocr.service;


import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.ocr.exception.OcrException;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.nccbip.response.NccBipOcrResponse;

/**
 * <p>Title: ChangeIdentifyInfo</p>
 * <p>Description: 将不同厂商识别信息转换为实体类</p>
 * @author L
 **/
public interface ChangeIdentifyInfo<T> {

    IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, T t) throws OcrException;

    default NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO reverseConversion(BaseEntity baseEntity) {
        return null;
    }



}
