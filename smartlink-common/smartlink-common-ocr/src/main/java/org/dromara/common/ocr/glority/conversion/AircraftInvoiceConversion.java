package org.dromara.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import org.dromara.common.ocr.core.ChangeIdentifyInfo;
import org.dromara.common.ocr.entity.IdentificationData;
import org.dromara.common.ocr.exception.OcrException;
import org.dromara.common.ocr.glority.response.IdentifyResults;
import org.smartlink.business.domain.DataImageFilesInfo;


import java.util.Date;

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
