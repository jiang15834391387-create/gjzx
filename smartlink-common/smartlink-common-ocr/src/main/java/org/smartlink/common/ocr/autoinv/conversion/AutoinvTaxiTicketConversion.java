package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataTaxiTickets;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 出租车票转换类
 */
public class AutoinvTaxiTicketConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvTaxiTicketConversion INSTANCE = new AutoinvTaxiTicketConversion();
    }

    private AutoinvTaxiTicketConversion() {}

    public static AutoinvTaxiTicketConversion getInstance() {
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

            DataTaxiTickets taxiTicket = new DataTaxiTickets();
            taxiTicket.setId(IdUtil.simpleUUID());
            taxiTicket.setFileId(dataImageFilesInfo.getFileId());

            // 设置出租车票基本信息
            taxiTicket.setInvoiceCode(jsonObject.getStr("invoice_code"));
            taxiTicket.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            taxiTicket.setInvoiceDate(jsonObject.getStr("date"));
            taxiTicket.setInvoiceTotal(jsonObject.getStr("amount_little"));
            taxiTicket.setFare(jsonObject.getStr("fare"));
            taxiTicket.setMileage(jsonObject.getStr("mileage"));
            taxiTicket.setTimeGetOn(jsonObject.getStr("time_get_on"));
            taxiTicket.setTimeGetOff(jsonObject.getStr("time_get_off"));
            taxiTicket.setLicensePlate(jsonObject.getStr("taxi_num"));
            taxiTicket.setInvoiceTotal(jsonObject.getStr("price_per_km"));
            taxiTicket.setFuelSurcharge(jsonObject.getStr("fuel_oil_surcharge"));
            taxiTicket.setProvince(jsonObject.getStr("province"));
            taxiTicket.setCity(jsonObject.getStr("place"));

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_TAXI_TICKETS_CODE, taxiTicket, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
