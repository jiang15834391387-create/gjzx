package org.smartlink.web.ocr.service.nccbip.conversion;


import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.ocr.exception.OcrException;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.nccbip.response.NccBipOcrResponse;

/**
 * <p>Title: OthersConversion</p>
 * <p>
 * <p>Description:非识别转换为实体类 </p>
 *
 * @author L
 **/
public class NccBipOthersConversion implements ChangeIdentifyInfo<NccBipOcrResponse.OneDataDTO.DatasDTO> {

    private static class LazyHolder {

        private static final NccBipOthersConversion INSTANCE = new NccBipOthersConversion();
    }
    public static NccBipOthersConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    private NccBipOthersConversion() {}


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, NccBipOcrResponse.OneDataDTO.DatasDTO datasDTO) throws OcrException {
        return null;
    }

    @Override
    public NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO reverseConversion(BaseEntity baseEntity) {
        return new NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO();
    }
}
