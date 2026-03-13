package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataQuotaInvoice;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 定额发票转换类
 */
public class AutoinvQuotaInvoiceConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvQuotaInvoiceConversion INSTANCE = new AutoinvQuotaInvoiceConversion();
    }

    private AutoinvQuotaInvoiceConversion() {}

    public static AutoinvQuotaInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<AutoinvIdentifyResult> identifyResults) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (AutoinvIdentifyResult identifyResult : identifyResults) {
            JSONObject jsonObject = identifyResult;
            if (jsonObject == null) {
                continue;
            }

            DataQuotaInvoice quotaInvoice = new DataQuotaInvoice();
            quotaInvoice.setId(IdUtil.simpleUUID());
            quotaInvoice.setFileId(dataImageFilesInfo.getFileId());

            // 设置发票基本信息
            quotaInvoice.setInvoiceCode(jsonObject.getStr("invoice_code"));
            quotaInvoice.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            quotaInvoice.setInvoiceTotal(jsonObject.getStr("amount_little"));
            quotaInvoice.setMoneyUppercase(jsonObject.getStr("amount_big"));


            // 设置其他信息
            quotaInvoice.setProvince(jsonObject.getStr("province"));
            quotaInvoice.setCity(jsonObject.getStr("city"));
            quotaInvoice.setCompanySeal(jsonObject.getStr("seal"));

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE, quotaInvoice, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
