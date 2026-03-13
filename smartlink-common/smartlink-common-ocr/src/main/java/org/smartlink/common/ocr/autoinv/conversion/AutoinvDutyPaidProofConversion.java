package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataDutyPaidProof;
import org.smartlink.common.entity.domain.business.domain.DataDutyPaidProofDetails;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 完税证明转换类
 */
public class AutoinvDutyPaidProofConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvDutyPaidProofConversion INSTANCE = new AutoinvDutyPaidProofConversion();
    }

    private AutoinvDutyPaidProofConversion() {}

    public static AutoinvDutyPaidProofConversion getInstance() {
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

            DataDutyPaidProof dutyPaidProof = new DataDutyPaidProof();
            dutyPaidProof.setId(IdUtil.simpleUUID());
            dutyPaidProof.setFileId(dataImageFilesInfo.getFileId());

            // 设置完税证明基本信息
            dutyPaidProof.setTitle(jsonObject.getStr("title"));
            dutyPaidProof.setInvoiceDate(jsonObject.getStr("date"));
            dutyPaidProof.setBuyerName(jsonObject.getStr("name"));
            dutyPaidProof.setBuyerTaxId(jsonObject.getStr("tax_id"));
            dutyPaidProof.setTaxAuthority(jsonObject.getStr("tax_authority"));
            dutyPaidProof.setTotalUppercase(jsonObject.getStr("amount_big"));
            dutyPaidProof.setInvoiceTotal(jsonObject.getStr("amount_little"));
            dutyPaidProof.setRemark(jsonObject.getStr("notes"));

            // 处理完税证明明细
            List<DataDutyPaidProofDetails> detailsList = new ArrayList<>();
            JSONArray taxProofDetailsArray = jsonObject.getJSONArray("tax_proof_details");
            if (taxProofDetailsArray != null && taxProofDetailsArray.size() > 0) {
                for (int i = 0; i < taxProofDetailsArray.size(); i++) {
                    JSONObject detailObj = taxProofDetailsArray.getJSONObject(i);
                    DataDutyPaidProofDetails detail = new DataDutyPaidProofDetails();
                    detail.setId(IdUtil.simpleUUID());
                    detail.setFileId(dataImageFilesInfo.getFileId());

                    // 映射明细字段
                    detail.setEntryDate(detailObj.getStr("inout_store_date"));
                    detail.setTaxPeriod(detailObj.getStr("tax_valid_date"));
                    detail.setOriginalNumber(detailObj.getStr("origin_proof_no"));
                    detail.setName(detailObj.getStr("project_name"));
                    detail.setTaxType(detailObj.getStr("tax_type"));
                    detail.setAmountPaid(detailObj.getStr("paid_amount"));

                    detailsList.add(detail);
                }
            }

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE, dutyPaidProof, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
