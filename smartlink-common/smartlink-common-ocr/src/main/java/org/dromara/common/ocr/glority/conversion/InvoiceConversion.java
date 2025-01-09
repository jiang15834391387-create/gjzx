package org.dromara.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.dromara.common.ocr.core.ChangeIdentifyInfo;
import org.dromara.common.ocr.entity.IdentificationData;
import org.dromara.common.ocr.exception.OcrException;
import org.dromara.common.ocr.glority.response.IdentifyResults;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;


import java.math.BigDecimal;
import java.util.*;

/**
 * 增值税
 *
 **/
public class InvoiceConversion implements ChangeIdentifyInfo<IdentifyResults> {

    private static class LazyHolder {

        private static final InvoiceConversion INSTANCE = new InvoiceConversion();
    }

    private InvoiceConversion() {
    }

    public static InvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 电子专票票小秘
     */
    private final static String GLORITY_ELECTRONIC_INVOICE = "10102";
    // 2022新版电子普通发票
    private final static String GLORITY_TAX_ELECTRONIC_NO_INVOICE_CODE_INVOICE = "10108";
    // 2022新版电子专用发票
    private final static String GLORITY_SPECIAL_ELECTRONIC_NO_INVOICE_CODE_INVOICE = "10107";

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {

        return null;
    }

}
