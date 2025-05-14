package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.entity.domain.business.domain.DataSteamerTicket;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 船票
 *
 * @author maxuhui
 **/
public class SteamerTicketConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {


    private static class LazyHolder {
        private static final SteamerTicketConversion INSTANCE = new SteamerTicketConversion();
    }

    private SteamerTicketConversion() {
    }

    public static SteamerTicketConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataSteamerTicket steamerTicket = new DataSteamerTicket();
            if (null == jsonObject) {
                return null;
            }
            steamerTicket.setId(IdUtil.simpleUUID());
            steamerTicket.setFileId(dataImageFilesInfo.getFileId());
            steamerTicket.setInvoiceCode(jsonObject.getStr("code"));
            steamerTicket.setInvoiceNumber(jsonObject.getStr("number"));
            steamerTicket.setInvoiceDate(jsonObject.getStr("date"));
            steamerTicket.setInvoiceTime(jsonObject.getStr("time"));
            steamerTicket.setStationGetOn(jsonObject.getStr("station_geton"));
            steamerTicket.setStationGetOff(jsonObject.getStr("station_getoff"));
            steamerTicket.setCurrencyCode(jsonObject.getStr("currency_code"));
            steamerTicket.setInvoiceTotal(jsonObject.getStr("total"));
            steamerTicket.setName(jsonObject.getStr("name"));
            steamerTicket.setCity(jsonObject.getStr("city"));
            steamerTicket.setProvince(jsonObject.getStr("province"));
            steamerTicket.setKind(jsonObject.getStr("kind"));
            steamerTicket.setUserId(jsonObject.getStr("user_id"));

            steamerTicket.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                steamerTicket.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                steamerTicket.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_STEAMER_TICKET_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_STEAMER_TICKET_CODE.getCode(), steamerTicket, identifyResults.getExtra(), identifyResults.getMessage()));

        }
        return resultsList;

    }
}
