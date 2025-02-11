package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatment;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


//医疗票
public class DataMedicalTreatmentDetailsConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final DataMedicalTreatmentDetailsConversion INSTANCE = new DataMedicalTreatmentDetailsConversion();
    }

    private DataMedicalTreatmentDetailsConversion() {
    }

    public static DataMedicalTreatmentDetailsConversion getInstance() {
        return DataMedicalTreatmentDetailsConversion.LazyHolder.INSTANCE;
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
            invoice.setInvoiceNumber(jsonObject.getStr("bill_number"));
            invoice.setInvoiceDate(jsonObject.getStr("date"));
            invoice.setKind(jsonObject.getStr("kind"));
            invoice.setPayee(jsonObject.getStr("payee"));
            invoice.setPayer(jsonObject.getStr("payer"));

            JSONArray list = jsonObject.getJSONArray("items");
            List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
            for (Object invoiceDetails : list) {
                DataOcrDetails e = new DataOcrDetails();
                LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                e.setId(IdUtil.fastSimpleUUID());
                e.setFileId(dataImageFilesInfo.getFileId());
                e.setRemark(fJson.containsKey("comment") ? fJson.get("comment") : null);
                e.setProjectName(fJson.containsKey("project_name") ? fJson.get("project_name") : null);
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
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.MEDICAL_TICKET_DETAILS_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.MEDICAL_TICKET_DETAILS_CODE.getCode(), invoice, identifyResults.getExtra()));
        }
        return resultsList;
    }

}
