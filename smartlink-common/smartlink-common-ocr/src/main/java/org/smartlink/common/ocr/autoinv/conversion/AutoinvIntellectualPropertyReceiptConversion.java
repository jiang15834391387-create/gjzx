package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 知识产权收费收据转换类
 */
public class AutoinvIntellectualPropertyReceiptConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvIntellectualPropertyReceiptConversion INSTANCE = new AutoinvIntellectualPropertyReceiptConversion();
    }

    private AutoinvIntellectualPropertyReceiptConversion() {}

    public static AutoinvIntellectualPropertyReceiptConversion getInstance() {
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

            DataOcrInfo invoice = new DataOcrInfo();
            invoice.setId(IdUtil.simpleUUID());
            invoice.setFileId(dataImageFilesInfo.getFileId());

            // 设置知识产权收费收据基本信息
            invoice.setInvoiceCode(jsonObject.getStr("invoice_code"));
            invoice.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            invoice.setInvoiceDate(jsonObject.getStr("date"));
            invoice.setInvoiceTotal(jsonObject.getStr("amount_little"));
            invoice.setTotalUppercase(jsonObject.getStr("amount_big"));

            // 设置收费单位信息
            invoice.setSellerName(jsonObject.getStr("collecting_unit_name"));
            invoice.setSellerNo(jsonObject.getStr("collecting_unit_code"));

            // 设置缴费人信息
            invoice.setBuyerName(jsonObject.getStr("payer_name"));

            // 设置其他信息
            invoice.setRemark(jsonObject.getStr("notes"));
            invoice.setCompanySeal(jsonObject.getStr("seal"));

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.REIMBURSABLE_OTHER_CODE, invoice, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
