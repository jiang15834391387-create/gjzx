package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
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
 * autoinv 通用手工发票转换类
 */
public class AutoinvManualInvoiceConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvManualInvoiceConversion INSTANCE = new AutoinvManualInvoiceConversion();
    }

    private AutoinvManualInvoiceConversion() {}

    public static AutoinvManualInvoiceConversion getInstance() {
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

            // 设置发票基本信息
            invoice.setInvoiceCode(jsonObject.getStr("invoice_code"));
            invoice.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            invoice.setInvoiceDate(jsonObject.getStr("date"));
            invoice.setInvoiceTotal(jsonObject.getStr("amount_little"));
            invoice.setTotalUppercase(jsonObject.getStr("amount_big"));
            invoice.setCheckCode(jsonObject.getStr("check_code"));

            // 设置销售方信息
            invoice.setSellerName(jsonObject.getStr("seller_name"));
            invoice.setSellerNo(jsonObject.getStr("seller_tax_id"));

            // 设置购买方信息
            invoice.setBuyerName(jsonObject.getStr("buyer_name"));

            // 设置其他信息
            invoice.setProvince(jsonObject.getStr("province"));
            invoice.setCity(jsonObject.getStr("city"));
            invoice.setCompanySeal(jsonObject.getStr("seal"));

            dataImageFilesInfo.setInvoice(InvoiceConstants.GLORITY_MANUAL_INVOICE_CODE);
            dataImageFilesInfo.setMessage(identifyResult.getStr("msg"));
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_MANUAL_INVOICE_CODE, invoice, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
