package org.smartlink.web.ocr.service.nccbip.conversion;

import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.ocr.exception.OcrException;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.nccbip.response.NccBipOcrResponse;


/**
 * <p>Title: TollRoadsConversion</p>
 * <p>
 * <p>Description:税务云小票转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class ReceiptConversion implements ChangeIdentifyInfo<NccBipOcrResponse.OneDataDTO.DatasDTO> {

    private static class LazyHolder {

        private static final ReceiptConversion INSTANCE = new ReceiptConversion();
    }

    private ReceiptConversion() {}

    public static ReceiptConversion getInstance() {

        return LazyHolder.INSTANCE;
    }
    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, NccBipOcrResponse.OneDataDTO.DatasDTO datasDTO) throws OcrException {
        return null;
    }
}
