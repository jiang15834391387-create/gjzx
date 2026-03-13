package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataNonTax;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 政府非税收入转换类
 */
public class AutoinvNonTaxRevenueConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvNonTaxRevenueConversion INSTANCE = new AutoinvNonTaxRevenueConversion();
    }

    private AutoinvNonTaxRevenueConversion() {}

    public static AutoinvNonTaxRevenueConversion getInstance() {
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

            DataNonTax nonTax = new DataNonTax();
            nonTax.setId(IdUtil.simpleUUID());
            nonTax.setFileId(dataImageFilesInfo.getFileId());

            // 设置发票基本信息
            nonTax.setInvoiceCode(jsonObject.getStr("invoice_code"));
            nonTax.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            nonTax.setInvoiceDate(jsonObject.getStr("date"));
            nonTax.setInvoiceTotal(jsonObject.getStr("amount_little"));
            nonTax.setTotalWords(jsonObject.getStr("amount_big"));

            // 设置缴款人信息
            nonTax.setPayer(jsonObject.getStr("payer"));
            nonTax.setSocialCreditCode(jsonObject.getStr("payer_tax_no"));
            nonTax.setPayerAccountNumber(jsonObject.getStr("payer_account"));
            nonTax.setPayerAccountOpeningBank(jsonObject.getStr("payer_bank"));
            nonTax.setReceiverAccountNumber(jsonObject.getStr("payee_account"));
            nonTax.setReceiverAccountOpeningBank(jsonObject.getStr("payee_bank"));

            // 设置执收单位信息
            nonTax.setPayeeCode(jsonObject.getStr("collection_company_code"));
            nonTax.setPayee(jsonObject.getStr("collection_company"));

            // 设置其他信息
            nonTax.setChecker(jsonObject.getStr("checker"));
            nonTax.setHandler(jsonObject.getStr("operator"));
            nonTax.setPaymentCode(jsonObject.getStr("payment_code"));
            nonTax.setOtherInfo(jsonObject.getStr("other_info"));
            nonTax.setElectronicMark(jsonObject.getStr("ebill_mark"));
            nonTax.setInvoiceStamp(jsonObject.getStr("seal"));

            // 处理非税收入明细
            List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
            JSONArray detailsArray = jsonObject.getJSONArray("details");
            if (detailsArray != null && detailsArray.size() > 0) {
                for (int i = 0; i < detailsArray.size(); i++) {
                    JSONObject detailObj = detailsArray.getJSONObject(i);
                    DataOcrDetails ocrDetail = new DataOcrDetails();
                    ocrDetail.setId(IdUtil.simpleUUID());
                    ocrDetail.setOcrId(nonTax.getId());
                    ocrDetail.setFileId(dataImageFilesInfo.getFileId());

                    // 映射明细字段
                    ocrDetail.setDetailAmount(detailObj.getStr("amount"));
                    ocrDetail.setProjectCode(detailObj.getStr("project_code"));
                    ocrDetail.setCommodityName(detailObj.getStr("project_name"));
                    ocrDetail.setUnit(detailObj.getStr("unit"));
                    ocrDetail.setStandard(detailObj.getStr("charge_standard"));
                    ocrDetail.setRemark(detailObj.getStr("remark"));

                    ocrDetailsList.add(ocrDetail);
                }
            }

            // 设置明细数据到非税收入对象
            nonTax.setDetails(ocrDetailsList);

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE, nonTax, null, identifyResult.getStr("msg")));



        }
        return resultsList;
    }
}
