package org.smartlink.server.nc.ocr.service;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.nccbip.response.NccBipOcrResponse;

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
