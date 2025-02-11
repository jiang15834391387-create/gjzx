package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 完税证明
 *
 * @author maxuhui
 **/
public class DutyPaidProofConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {


    private static class LazyHolder {

        private static final DutyPaidProofConversion INSTANCE = new DutyPaidProofConversion();
    }

    private DutyPaidProofConversion() {
    }

    public static DutyPaidProofConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataDutyPaidProof dutyPaidProof = new DataDutyPaidProof();
            if (null == jsonObject) {
                return null;
            }
            dutyPaidProof.setId(IdUtil.simpleUUID());
            dutyPaidProof.setFileId(dataImageFilesInfo.getFileId());
            dutyPaidProof.setInvoiceDate(jsonObject.getStr("date"));
            dutyPaidProof.setBuyerTaxId(jsonObject.getStr("buyer_tax_id"));
            dutyPaidProof.setBuyerName(jsonObject.getStr("buyer"));
            dutyPaidProof.setInvoiceNumber(jsonObject.getStr("number"));
            dutyPaidProof.setTotalUppercase(jsonObject.getStr("total_cn"));
            dutyPaidProof.setInvoiceTotal(jsonObject.getStr("total"));
            dutyPaidProof.setTaxAuthority(jsonObject.getStr("tax_authorities"));
            dutyPaidProof.setSerialNumber(jsonObject.getStr("serial_number"));
            dutyPaidProof.setTitle(jsonObject.getStr("title"));
            dutyPaidProof.setRemark(jsonObject.getStr("remark"));
            dutyPaidProof.setTaxAgencyCode(jsonObject.getStr("tax_agency_code"));
            dutyPaidProof.setBuyerDepositBank(jsonObject.getStr("buyer_deposit_bank"));
            dutyPaidProof.setBuyerAccount(jsonObject.getStr("buyer_account"));
            dutyPaidProof.setTaxPaymentLimitedTime(jsonObject.getStr("tax_payment_limited_time"));
            dutyPaidProof.setReceivingTreasury(jsonObject.getStr("receiving_treasury"));

            JSONArray list = jsonObject.getJSONArray("items");
            List<DataDutyPaidProofDetails> ocrDetailsList = new ArrayList<>();
            for (Object invoiceDetails : list) {
                DataDutyPaidProofDetails e = new DataDutyPaidProofDetails();
                LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                e.setId(IdUtil.fastSimpleUUID());
                e.setFileId(dataImageFilesInfo.getFileId());
                e.setName(fJson.containsKey("name") ? fJson.get("name") : null);
                e.setActualPaidAmount(fJson.containsKey("actual_paid_amount") ? fJson.get("actual_paid_amount") : null);
                e.setAmountPaid(fJson.containsKey("amount_paid") ? fJson.get("amount_paid") : null);
                e.setEntryDate(fJson.containsKey("entry_date") ? fJson.get("entry_date") : null);
                e.setTaxAgency(fJson.containsKey("tax_agency") ? fJson.get("tax_agency") : null);
                e.setTaxPeriod(fJson.containsKey("tax_period") ? fJson.get("tax_period") : null);
                e.setTaxType(fJson.containsKey("tax_type") ? fJson.get("tax_type") : null);
                e.setBudgetAccountCode(fJson.containsKey("budget_account_code") ? fJson.get("budget_account_code") : null);
                e.setBudgetAccountName(fJson.containsKey("budget_account_name") ? fJson.get("budget_account_name") : null);
                e.setBudgetAccountLevel(fJson.containsKey("budget_account_level") ? fJson.get("budget_account_level") : null);
                e.setOriginalNumber(fJson.containsKey("original_number") ? fJson.get("original_number") : null);
                e.setQuantity(fJson.containsKey("quantity") ? fJson.get("quantity") : null);
                e.setTaxRate(fJson.containsKey("tax_rate") ? fJson.get("tax_rate") : null);
                e.setTotal(fJson.containsKey("total") ? fJson.get("total") : null);
                ocrDetailsList.add(e);
            }
            dutyPaidProof.setDetails(ocrDetailsList);

            dutyPaidProof.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                dutyPaidProof.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                dutyPaidProof.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_DUTY_PAID_PROOF_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_DUTY_PAID_PROOF_CODE.getCode(), dutyPaidProof, identifyResults.getExtra()));

        }
        return resultsList;
    }
}
