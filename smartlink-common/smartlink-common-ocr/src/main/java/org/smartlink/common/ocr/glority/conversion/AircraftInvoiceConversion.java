package org.smartlink.common.ocr.glority.conversion;

import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

/**
 * 机打发票
 *
 **/
public class AircraftInvoiceConversion implements ChangeIdentifyInfo<IdentifyResults> {


    private static class LazyHolder {

        private static final AircraftInvoiceConversion INSTANCE = new AircraftInvoiceConversion();
    }

    private AircraftInvoiceConversion() {
    }

    public static AircraftInvoiceConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {

        return null;
//
    }


}
