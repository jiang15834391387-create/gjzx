package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;

import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataPassengerCar;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客运汽车票
 *
 **/
public class PassengerTicketConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final PassengerTicketConversion INSTANCE = new PassengerTicketConversion();
    }

    private PassengerTicketConversion() {
    }

    public static PassengerTicketConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataPassengerCar passengerTicket = new DataPassengerCar();
            if (null == jsonObject) {
                return null;
            }
            passengerTicket.setId(IdUtil.simpleUUID());
            passengerTicket.setFileId(dataImageFilesInfo.getFileId());
            passengerTicket.setInvoiceCode(jsonObject.getStr("code"));
            passengerTicket.setInvoiceNumber(jsonObject.getStr("number"));
            passengerTicket.setInvoiceTotal(jsonObject.getStr("total"));
            passengerTicket.setInvoiceTime(jsonObject.getStr("time"));
            passengerTicket.setInvoiceDate(jsonObject.getStr("date"));
            passengerTicket.setStationGeton(jsonObject.getStr("station_geton"));
            passengerTicket.setStationGetoff(jsonObject.getStr("station_getoff"));
            passengerTicket.setUserId(jsonObject.getStr("user_id"));
            passengerTicket.setTitle(jsonObject.getStr("title"));
            passengerTicket.setKind(jsonObject.getStr("kind"));
            passengerTicket.setName(jsonObject.getStr("name"));
            passengerTicket.setCompanySeal(jsonObject.getStr("company_seal"));
            passengerTicket.setBusNumber(jsonObject.getStr("bus_number"));


            passengerTicket.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                passengerTicket.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                passengerTicket.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_PASSENGER_TICKET_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_PASSENGER_TICKET_CODE.getCode(), passengerTicket, identifyResults.getExtra(), identifyResults.getMessage()));


        }
        return resultsList;
    }
}
