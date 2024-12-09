package org.smartlink.server.nc.ocr.service.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataDutyPaidProof;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.glority.response.IdentifyResults;

import java.util.Date;

/**
 * 完税证明
 *
 * @author L
 **/
public class DutyPaidProofConversion implements ChangeIdentifyInfo<IdentifyResults> {


    private static class LazyHolder {

        private static final DutyPaidProofConversion INSTANCE = new DutyPaidProofConversion();
    }

    private DutyPaidProofConversion() {
    }

    public static DutyPaidProofConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {
        JSONObject jsonObject = identifyResults.getDetails();
        DataDutyPaidProof dutyPaidProof = JSONObject.parseObject(jsonObject.toJSONString(), DataDutyPaidProof.class);
        if (null == dutyPaidProof) {
            return null;
        }
        dutyPaidProof.setId(IdUtil.simpleUUID());
        dutyPaidProof.setFileId(dataImageFilesInfo.getFileId());
        dataImageFilesInfo.setFileType(InvoiceConstants.DUTY_PAID_PROOF);
        dutyPaidProof.setInvoiceDate(Convert.toDate(jsonObject.getString("date"), new Date()));
        dutyPaidProof.setBuyerTaxId(jsonObject.getString("buyer_tax_id"));
        dutyPaidProof.setBuyerName(jsonObject.getString("buyer"));
        dutyPaidProof.setInvoiceNumber(jsonObject.getString("number"));
        dutyPaidProof.setTotalUppercase(jsonObject.getString("total_cn"));
        dutyPaidProof.setInvoiceTotal(Convert.toBigDecimal(jsonObject.getString("total")));
        dutyPaidProof.setTaxAuthority(jsonObject.getString("tax_authorities"));
        dutyPaidProof.setCoordinate(identifyResults.getRegion());
        dutyPaidProof.setOrientation(Integer.parseInt("-"+identifyResults.getOrientation()));
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), dutyPaidProof);
    }
}
