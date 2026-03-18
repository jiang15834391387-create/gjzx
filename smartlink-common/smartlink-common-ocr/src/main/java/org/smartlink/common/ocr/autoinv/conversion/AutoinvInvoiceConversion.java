package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.AutoinvInvoiceTypeMap;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 增值税发票转换类
 */
public class AutoinvInvoiceConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvInvoiceConversion INSTANCE = new AutoinvInvoiceConversion();
    }

    private AutoinvInvoiceConversion() {}

    public static AutoinvInvoiceConversion getInstance() {
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
            invoice.setCheckCode(jsonObject.getStr("check_code"));

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
            invoice.setProvince(jsonObject.getStr("province"));
            invoice.setCity(jsonObject.getStr("city"));
            invoice.setInvoiceSheet(jsonObject.getStr("sheet_no"));
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
                    ocrDetail.setDetailsCount(detailObj.getStr("quantity"));
                    ocrDetail.setCommodityName(detailObj.getStr("merchandise_name"));
                    ocrDetail.setTax(detailObj.getStr("tax_amount"));
                    ocrDetail.setPrice(detailObj.getStr("unit_price"));
                    ocrDetail.setStandard(detailObj.getStr("model_nunber"));
                    ocrDetail.setTaxRate(detailObj.getStr("tax_rate"));
                    ocrDetail.setUnit(detailObj.getStr("unit"));

                    ocrDetailsList.add(ocrDetail);
                }
            }

            // 设置数电票相关信息
            invoice.setRightInvoiceNumber(jsonObject.getStr("einvoice_no"));
            invoice.setElectronicMark(jsonObject.getStr("einvoice_mark"));

            // 根据autoinv原始类型获取系统统一类型
            String autoinvType = identifyResult.getStr("type");
            String systemType = AutoinvInvoiceTypeMap.getSystemType(autoinvType);
            // 如果获取不到系统类型，默认使用增值税专用发票类型
            if (StringUtils.isEmpty(systemType)) {
                systemType = InvoiceConstants.GLORITY_TAX_SPECIAL_CODE;
            }
            invoice.setDetails(ocrDetailsList);
            dataImageFilesInfo.setInvoice(systemType);
            dataImageFilesInfo.setMessage(identifyResult.getStr("msg"));
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            // 添加到结果列表
            resultsList.add(new IdentificationData<>(systemType, invoice, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
