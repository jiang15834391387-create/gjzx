package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


//非税收入类票据
public class DataNonTaxConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final DataNonTaxConversion INSTANCE = new DataNonTaxConversion();
    }

    private DataNonTaxConversion() {
    }

    public static DataNonTaxConversion getInstance() {
        return DataNonTaxConversion.LazyHolder.INSTANCE;
    }


    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataNonTax invoice = new DataNonTax();
            if (null == jsonObject) {
                return null;
            }
            invoice.setId(IdUtil.simpleUUID());
            invoice.setFileId(dataImageFilesInfo.getFileId());
            invoice.setInvoiceCode(jsonObject.getStr("bill_code"));
            invoice.setInvoiceNumber(jsonObject.getStr("bill_number"));
            invoice.setInvoiceDate(jsonObject.getStr("date"));
            invoice.setChecker(jsonObject.getStr("checker"));

            invoice.setReceiver(jsonObject.getStr("receiver"));
            invoice.setTitle(jsonObject.getStr("title"));
            invoice.setElectronicMark(jsonObject.getStr("electronic_mark"));
            invoice.setKind(jsonObject.getStr("kind"));
            // 大写 价税合计
            invoice.setCheckCode(jsonObject.getStr("check_code"));
            invoice.setPayee(jsonObject.getStr("payee"));
            invoice.setPayer(jsonObject.getStr("payer"));
            invoice.setOtherInfo(jsonObject.getStr("other_info"));
            invoice.setSocialCreditCode(jsonObject.getStr("social_credit_code"));
            invoice.setInvoiceTotal(jsonObject.getStr("total"));
            invoice.setTotalWords(jsonObject.getStr("total_words"));
            invoice.setBlockChain(jsonObject.getStr("block_chain"));
            invoice.setPaymentCode(jsonObject.getStr("payment_code"));
            invoice.setPayeeCode(jsonObject.getStr("payee_code"));
            invoice.setPayerAccountNumber(jsonObject.getStr("payer_account_number"));
            invoice.setPayerAccountOpeningBank(jsonObject.getStr("payer_account_opening_bank"));
            invoice.setReceiverAccountNumber(jsonObject.getStr("receiver_account_number"));
            invoice.setReceiverAccountOpeningBank(jsonObject.getStr("receiver_account_opening_bank"));
            invoice.setHandler(jsonObject.getStr("handler"));


            JSONArray list = jsonObject.getJSONArray("items");
            List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
            if (ObjectUtil.isNotNull(list)) {
                for (Object invoiceDetails : list) {
                    DataOcrDetails e = new DataOcrDetails();
                    LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                    e.setId(IdUtil.fastSimpleUUID());
                    e.setFileId(dataImageFilesInfo.getFileId());
                    e.setDetailAmount(fJson.containsKey("amount") ? fJson.get("amount") : null);
                    e.setRemark(fJson.containsKey("comment") ? fJson.get("comment") : null);
                    e.setProjectName(fJson.containsKey("project_name") ? fJson.get("project_name") : null);
                    e.setProjectCode(fJson.containsKey("project_code") ? fJson.get("project_code") : null);
                    e.setStandardOfCharge(fJson.containsKey("standard_of_charge") ? fJson.get("standard_of_charge") : null);
                    e.setDetailsCount(fJson.containsKey("quantity") ? fJson.get("quantity") : null);
                    e.setUnit(fJson.containsKey("uom") ? fJson.get("uom") : null);
                    ocrDetailsList.add(e);
                }
            }
            invoice.setDetails(ocrDetailsList);

            invoice.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                invoice.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                invoice.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.NON_TAX_REVENUE_RECEIPTS_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.NON_TAX_REVENUE_RECEIPTS_CODE.getCode(), invoice, identifyResults.getExtra(), identifyResults.getMessage()));
        }
        return resultsList;
    }

}
