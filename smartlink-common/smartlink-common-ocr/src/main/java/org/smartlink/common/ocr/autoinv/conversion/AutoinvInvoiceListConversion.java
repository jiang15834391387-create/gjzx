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
 * autoinv 增值税发票销货清单转换类
 */
public class AutoinvInvoiceListConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvInvoiceListConversion INSTANCE = new AutoinvInvoiceListConversion();
    }

    private AutoinvInvoiceListConversion() {}

    public static AutoinvInvoiceListConversion getInstance() {
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
            invoice.setInvoiceDate(jsonObject.getStr("invoice_open_date"));
            invoice.setSumTax(jsonObject.getStr("tax_amount"));
            invoice.setPretaxAmount(jsonObject.getStr("pretax_amount"));
            invoice.setInvoiceTotal(jsonObject.getStr("sum_amount_lowercase"));
            invoice.setTotalUppercase(jsonObject.getStr("sum_amount_capital"));

            // 设置销售方信息
            invoice.setSellerName(jsonObject.getStr("seller_name"));
            invoice.setSellerNo(jsonObject.getStr("seller_tax_no"));
            invoice.setSellerAddress(jsonObject.getStr("seller_address_phone"));
            invoice.setSellerAccount(jsonObject.getStr("seller_bank_info"));

            // 设置购买方信息
            invoice.setBuyerName(jsonObject.getStr("buyer_name"));
            invoice.setBuyerNo(jsonObject.getStr("buyer_tax_no"));
            invoice.setBuyerAddress(jsonObject.getStr("buyer_address_phone"));
            invoice.setBuyerAccount(jsonObject.getStr("buyer_bank_info"));

            // 设置其他信息
            invoice.setPassword1(jsonObject.getStr("cipher_text"));
            invoice.setMachineCode(jsonObject.getStr("machine_code"));
            invoice.setPayee(jsonObject.getStr("payee"));
            invoice.setChecker(jsonObject.getStr("checker"));
            invoice.setIssuer(jsonObject.getStr("invoice_opener"));
            invoice.setRemark(jsonObject.getStr("notes"));

            dataImageFilesInfo.setInvoice(InvoiceConstants.DIGITAL_INVOICE_LIST);
            dataImageFilesInfo.setMessage(identifyResult.getStr("msg"));
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.DIGITAL_INVOICE_LIST, invoice, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
