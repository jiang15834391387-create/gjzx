package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.util.IdUtil;

import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.entity.domain.business.domain.DataQuotaInvoice;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.List;

/**
 * 定额发票
 *
 * @author maxuhui
 **/
public class QuotaInvoiceConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    /**
     * 电子专票票小秘
     */

    private static class LazyHolder {

        private static final QuotaInvoiceConversion INSTANCE = new QuotaInvoiceConversion();
    }

    private QuotaInvoiceConversion() {
    }

    public static QuotaInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataQuotaInvoice quotaInvoice = new DataQuotaInvoice();
            if (null == jsonObject) {
                return null;
            }
            quotaInvoice.setId(IdUtil.simpleUUID());
            quotaInvoice.setFileId(dataImageFilesInfo.getFileId());
            quotaInvoice.setInvoiceCode(jsonObject.getStr("code"));
            quotaInvoice.setInvoiceNumber(jsonObject.getStr("number"));
            quotaInvoice.setInvoiceTotal(jsonObject.getStr("total"));
            quotaInvoice.setCompanySeal(jsonObject.getStr("company_seal"));
            quotaInvoice.setCompanySeal(jsonObject.getStr("kind"));
            quotaInvoice.setProvince(jsonObject.getStr("province"));
            quotaInvoice.setCity(jsonObject.getStr("city"));
            quotaInvoice.setNonCommercialMark(jsonObject.getStr("non_commercial_mark"));
            quotaInvoice.setTitle(jsonObject.getStr("title"));
            quotaInvoice.setMoneyUppercase(jsonObject.getStr("total_cn"));

            quotaInvoice.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                quotaInvoice.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                quotaInvoice.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_QUOTA_INVOICE_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_QUOTA_INVOICE_CODE.getCode(), quotaInvoice, identifyResults.getExtra()));

        }
        return resultsList;
    }
}
