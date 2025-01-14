package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;

import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.smartlink.common.entity.domain.business.domain.DataReceipt;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 小票
 *
 * @author maxuhui
 **/
public class ReceiptConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {


    private static class LazyHolder {

        private static final ReceiptConversion INSTANCE = new ReceiptConversion();
    }

    private ReceiptConversion() {
    }

    public static ReceiptConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataReceipt receipt = new DataReceipt();
            if (null == jsonObject) {
                return null;
            }
            receipt.setId(IdUtil.simpleUUID());
            receipt.setFileId(dataImageFilesInfo.getFileId());
            receipt.setInvoiceTotal(jsonObject.getStr("total"));
            receipt.setInvoiceDate(jsonObject.getStr("date"));
            receipt.setStoreName(jsonObject.getStr("store_name"));
            receipt.setCurrencyCode(jsonObject.getStr("currency_code"));
            receipt.setSubTotal(jsonObject.getStr("subtotal"));
            receipt.setTax(jsonObject.getStr("tax"));
            receipt.setDiscount(jsonObject.getStr("discount"));
            receipt.setTime(jsonObject.getStr("time"));
            receipt.setTips(jsonObject.getStr("tips"));
            receipt.setCurrencyCode(jsonObject.getStr("currency_code"));
            receipt.setKind(jsonObject.getStr("type"));
            receipt.setInternationalMark(jsonObject.getStr("international_mark"));

            receipt.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                receipt.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                receipt.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_RECEIPT_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_RECEIPT_CODE.getCode(), receipt, identifyResults.getExtra()));

        }
        return resultsList;
    }
}
