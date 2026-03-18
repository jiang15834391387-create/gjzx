package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatment;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 电子医疗票据转换类
 */
public class AutoinvElectronicMedicalReceiptConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvElectronicMedicalReceiptConversion INSTANCE = new AutoinvElectronicMedicalReceiptConversion();
    }

    private AutoinvElectronicMedicalReceiptConversion() {}

    public static AutoinvElectronicMedicalReceiptConversion getInstance() {
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

            DataMedicalTreatment medicalTreatment = new DataMedicalTreatment();
            medicalTreatment.setId(IdUtil.simpleUUID());
            medicalTreatment.setFileId(dataImageFilesInfo.getFileId());

            // 设置电子医疗票据基本信息
            medicalTreatment.setTitle(jsonObject.getStr("title"));
            medicalTreatment.setInvoiceCode(jsonObject.getStr("invoice_code"));
            medicalTreatment.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            medicalTreatment.setCheckCode(jsonObject.getStr("check_code"));
            medicalTreatment.setInvoiceDate(jsonObject.getStr("date"));
            medicalTreatment.setHospital(jsonObject.getStr("medical_institution"));
            medicalTreatment.setPayer(jsonObject.getStr("patient_name"));
            medicalTreatment.setOverallAmount(jsonObject.getStr("overall_amount"));
            medicalTreatment.setInvoiceTotal(jsonObject.getStr("amount_little"));
            medicalTreatment.setPayee(jsonObject.getStr("medical_institution"));
            medicalTreatment.setElectronicMark(jsonObject.getStr("electronic_mark"));

            dataImageFilesInfo.setInvoice(InvoiceConstants.MEDICAL_RECEIPTS_CODE);
            dataImageFilesInfo.setMessage(identifyResult.getStr("msg"));
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.MEDICAL_RECEIPTS_CODE, medicalTreatment, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
