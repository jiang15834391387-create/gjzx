package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatment;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


//医疗票
public class DataMedicalTreatmentConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final DataMedicalTreatmentConversion INSTANCE = new DataMedicalTreatmentConversion();
    }

    private DataMedicalTreatmentConversion() {
    }

    public static DataMedicalTreatmentConversion getInstance() {
        return DataMedicalTreatmentConversion.LazyHolder.INSTANCE;
    }


    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataMedicalTreatment invoice = new DataMedicalTreatment();
            if (null == jsonObject) {
                return null;
            }
            invoice.setId(IdUtil.simpleUUID());
            invoice.setFileId(dataImageFilesInfo.getFileId());
            invoice.setInvoiceCode(jsonObject.getStr("bill_code"));
            invoice.setCheckCode(jsonObject.getStr("check_code"));
            invoice.setInvoiceNumber(jsonObject.getStr("bill_number"));
            invoice.setInvoiceDate(jsonObject.getStr("date"));
            invoice.setElectronicMark(jsonObject.getStr("electronic_mark"));
            invoice.setKind(jsonObject.getStr("kind"));
            invoice.setOtherInfo(jsonObject.getStr("other_info"));
            invoice.setPayee(jsonObject.getStr("payee"));
            invoice.setPayer(jsonObject.getStr("payer"));
            invoice.setSocialCreditCode(jsonObject.getStr("social_credit_code"));
            invoice.setTitle(jsonObject.getStr("title"));
            invoice.setInvoiceTotal(jsonObject.getStr("total"));
            invoice.setTotalWords(jsonObject.getStr("totalWords"));
            invoice.setHospital(jsonObject.getStr("hospital"));
            invoice.setOverallAmount(jsonObject.getStr("overall_amount"));
            invoice.setMedicalRecordNumber(jsonObject.getStr("medical_record_number"));
            invoice.setInpatientNumber(jsonObject.getStr("inpatient_number"));
            invoice.setOutpatientNumber(jsonObject.getStr("outpatient_number"));
            invoice.setMedicalInsuranceNumber(jsonObject.getStr("medical_insurance_number"));
            invoice.setVisitDate(jsonObject.getStr("visit_date"));
            invoice.setMedicalInstitutionType(jsonObject.getStr("medical_institution_type"));
            invoice.setMedicalInstitutionType(jsonObject.getStr("medical_insurance_type"));
            invoice.setGende(jsonObject.getStr("gender"));
            invoice.setOtherPayments(jsonObject.getStr("other_payments"));
            invoice.setPersonalAccountPayment(jsonObject.getStr("personal_account_payment"));
            invoice.setCashPayment(jsonObject.getStr("cash_payment"));
            invoice.setPersonalExpense(jsonObject.getStr("personal_expense"));
            invoice.setPersonalPayment(jsonObject.getStr("personal_payment"));
            invoice.setInpatientDepartment(jsonObject.getStr("inpatient_department"));
            invoice.setAdmissionDate(jsonObject.getStr("admission_date"));
            invoice.setDischargeDate(jsonObject.getStr("discharge_date"));
            invoice.setAnnualHealthInsuranceCoverage(jsonObject.getStr("annual_health_insurance_coverage"));
            invoice.setAnnualOutpatientCatastrophicPayment(jsonObject.getStr("annual_outpatient_catastrophic_payment"));


            JSONArray list = jsonObject.getJSONArray("items");
            List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
            for (Object invoiceDetails : list) {
                DataOcrDetails e = new DataOcrDetails();
                LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                e.setId(IdUtil.fastSimpleUUID());
                e.setFileId(dataImageFilesInfo.getFileId());
                e.setRemark(fJson.containsKey("comment") ? fJson.get("comment") : null);
                e.setProjectCode(fJson.containsKey("project_code") ? fJson.get("project_code") : null);
                e.setProjectName(fJson.containsKey("project_name") ? fJson.get("project_name") : null);
                e.setUnit(fJson.containsKey("unit") ? fJson.get("unit") : null);
                e.setDetailAmount(fJson.containsKey("amount") ? fJson.get("amount") : null);
                e.setDetailsCount(fJson.containsKey("quantity") ? fJson.get("quantity") : null);
                ocrDetailsList.add(e);
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
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.MEDICAL_RECEIPTS_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.MEDICAL_RECEIPTS_CODE.getCode(), invoice, identifyResults.getExtra()));
        }
        return resultsList;
    }

}
