package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;
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
        JSONObject jsonObject = identifyResults.getDetails();
//        String json = jsonObject.toJSONString().replaceAll("items", "details");
//        DataOcrInfo invoice = JSONObject.parseObject(json, DataOcrInfo.class);
        DataOcrInfo invoice = new DataOcrInfo();
        if (null == invoice) {
            return null;
        }
        invoice.setId(IdUtil.simpleUUID());
        invoice.setInvoiceCode(jsonObject.getStr("code"));
        invoice.setInvoiceNumber(jsonObject.getStr("number"));
        invoice.setInvoiceDate(jsonObject.getStr("date"));
        invoice.setSumTax(jsonObject.getStr("tax"));
        String pretaxAmount = jsonObject.getStr("pretax_amount");
        if(StrUtil.isNotEmpty(pretaxAmount)){
            invoice.setSumAmount(pretaxAmount);
        }else{
            invoice.setSumAmount(jsonObject.getStr("total"));
        }
        invoice.setPretaxAmount(jsonObject.getStr("pretax_amount"));
        invoice.setTotalLowercase(jsonObject.getStr("total"));
        invoice.setRightInvoiceNumber(jsonObject.getStr("number_confirm"));
        invoice.setRightInvoiceCode(jsonObject.getStr("code_confirm"));
        // 大写 价税合计
        invoice.setTotalUppercase(jsonObject.getStr("total"));
        invoice.setCheckCode(jsonObject.getStr("check_code"));
        invoice.setSellerName(jsonObject.getStr("seller"));
        invoice.setSellerNo(jsonObject.getStr("seller_tax_id"));
        invoice.setSellerAddress(jsonObject.getStr("seller_addr_tel"));
        invoice.setSellerAccount(jsonObject.getStr("seller_bank_account"));
        invoice.setBuyerName(jsonObject.getStr("buyer"));
        invoice.setBuyerNo(jsonObject.getStr("buyer_tax_id"));
        invoice.setBuyerAccount(jsonObject.getStr("buyer_bank_account"));
        invoice.setBuyerAddress(jsonObject.getStr("buyer_addr_tel"));
        String password = jsonObject.getStr("ciphertext");
        if (StringUtils.isNotEmpty(password)) {
            List<String> split = Arrays.asList(password.split(",").clone());
            if (split.size() > 3) {
                invoice.setPassword1(split.get(0));
            }
        }
        invoice.setMachineCode(jsonObject.getStr("machine_code"));
        invoice.setPayee(jsonObject.getStr("receiptor"));
        invoice.setChecker(jsonObject.getStr("reviewer"));
        invoice.setIssuer(jsonObject.getStr("issuer"));
        invoice.setMachineCode(jsonObject.getStr("machine_code"));
        invoice.setCompanySeal(jsonObject.getStr("company_seal"));
        invoice.setSellCompanySeal(jsonObject.getStr("company_seal_mark"));
        invoice.setProvince(jsonObject.getStr("province"));
        invoice.setCity(jsonObject.getStr("city"));
        invoice.setInvoiceSheet(jsonObject.getStr("form_type"));
        invoice.setPageNumber(jsonObject.getStr("form_name"));
        invoice.setRemark(jsonObject.getStr("remark"));
        invoice.setItemNames(jsonObject.getStr("item_names"));
        String invoiceCode = identifyResults.getType();
        invoice.setRegion(jsonObject.getStr("Region"));
//        invoice.setOrientation(Integer.parseInt("-"+identifyResults.getOrientation()));

        //发票类型
        if (null != invoiceCode) {
            dataImageFilesInfo.setInvoice(invoiceCode.substring(invoiceCode.length() - 3));
        }
        if (GLORITY_ELECTRONIC_INVOICE.equals(invoiceCode)) {
            // 判断是不是区块链发票
            if ("1".equals(jsonObject.getStr("block_chain"))) {
                dataImageFilesInfo.setInvoice(InvoiceConstants.ELECTRONIC_INVOICE_QUKUAILIAN);
            } else if ("1".equals(jsonObject.getStr("transit_mark"))) {
                // 过路费电子普票
                dataImageFilesInfo.setInvoice(InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY);
            }
        }else if(StrUtil.equals(invoiceCode,GLORITY_TAX_ELECTRONIC_NO_INVOICE_CODE_INVOICE)){
            // 电子普票
            dataImageFilesInfo.setInvoice(InvoiceConstants.ELECTRONIC_INVOICE);
        }else if(StrUtil.equals(invoiceCode,GLORITY_SPECIAL_ELECTRONIC_NO_INVOICE_CODE_INVOICE)){
            // 电子专票
            dataImageFilesInfo.setInvoice(InvoiceConstants.ELECTRONIC_OFD_INVOICE);
        }
        JSONArray list = jsonObject.getJSONArray("items");
        List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
        for (Object invoiceDetails : list) {
            DataOcrDetails e = new DataOcrDetails();
            LinkedHashMap<String,String> fJson= (LinkedHashMap) invoiceDetails;
            e.setId(IdUtil.fastSimpleUUID());
            e.setName(fJson.get("name"));
            e.setStandard(fJson.get("specification"));
            e.setUnit(fJson.get("unit"));
            if(StrUtil.isNotEmpty(fJson.get("price"))){
                e.setPrice(new BigDecimal(fJson.get("price")));
            }else{
                e.setPrice(new BigDecimal(0));
            }
            e.setDetailAmount(new BigDecimal(fJson.get("total")));
            e.setTaxRate(fJson.get("tax_rate"));
            if(NumberUtil.isNumber(fJson.get("tax"))){
                e.setTax(new BigDecimal(fJson.get("tax")));
            }else{
                e.setTax(new BigDecimal(0));
            }
            e.setFileId(dataImageFilesInfo.getFileId());
            ocrDetailsList.add(e);
        }
        invoice.setDetails(ocrDetailsList);
        invoice.setFileId(dataImageFilesInfo.getFileId());
        invoice.setCheckInvoice("0");
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), invoice);
    }

}
