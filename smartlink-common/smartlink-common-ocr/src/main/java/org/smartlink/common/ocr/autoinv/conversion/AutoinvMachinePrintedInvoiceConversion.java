package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 机打发票转换类
 */
public class AutoinvMachinePrintedInvoiceConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvMachinePrintedInvoiceConversion INSTANCE = new AutoinvMachinePrintedInvoiceConversion();
    }

    private AutoinvMachinePrintedInvoiceConversion() {}

    public static AutoinvMachinePrintedInvoiceConversion getInstance() {
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
            invoice.setRightInvoiceCode(jsonObject.getStr("print_code"));
            invoice.setRightInvoiceNumber(jsonObject.getStr("print_no"));

            // 设置销售方信息
            invoice.setSellerName(jsonObject.getStr("seller_name"));
            invoice.setSellerNo(jsonObject.getStr("seller_tax_id"));
            invoice.setSellerAddress(jsonObject.getStr("seller_address_phone"));
            invoice.setSellerAccount(jsonObject.getStr("seller_bank_info"));

            // 设置购买方信息
            invoice.setBuyerName(jsonObject.getStr("buyer_name"));
            invoice.setBuyerNo(jsonObject.getStr("buyer_tax_id"));

            // 设置其他信息
            invoice.setIssuer(jsonObject.getStr("issuer"));
            invoice.setChecker(jsonObject.getStr("checker"));
            invoice.setPayee(jsonObject.getStr("payee"));
            invoice.setRemark(jsonObject.getStr("notes"));
            invoice.setCompanySeal(jsonObject.getStr("seal"));

            // 处理发票明细
            List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
            JSONArray detailsArray = jsonObject.getJSONArray("details");
            if (detailsArray != null && detailsArray.size() > 0) {
                for (int i = 0; i < detailsArray.size(); i++) {
                    JSONObject detailObj = detailsArray.getJSONObject(i);
                    DataOcrDetails ocrDetail = new DataOcrDetails();
                    ocrDetail.setId(IdUtil.simpleUUID());
                    ocrDetail.setOcrId(invoice.getId());
                    ocrDetail.setFileId(dataImageFilesInfo.getFileId());

                    // 映射明细字段
                    ocrDetail.setDetailAmount(detailObj.getStr("amount"));
                    ocrDetail.setUnit(detailObj.getStr("unit"));
                    ocrDetail.setDetailsCount(detailObj.getStr("quantity"));
                    ocrDetail.setCommodityName(detailObj.getStr("merchandise_name"));
                    ocrDetail.setTax(detailObj.getStr("tax_amount"));
                    ocrDetail.setPrice(detailObj.getStr("unit_price"));
                    ocrDetail.setStandard(detailObj.getStr("model_nunber"));
                    ocrDetail.setTaxRate(detailObj.getStr("tax_rate"));

                    ocrDetailsList.add(ocrDetail);
                }
            }

            // 设置明细数据到发票对象
            invoice.setDetails(ocrDetailsList);

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE, invoice, null, identifyResult.getStr("msg")));



        }
        return resultsList;
    }
}
