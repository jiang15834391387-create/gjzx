package org.smartlink.web.ocr.service.yesfp.conversion.tmp;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.invoice.DataPassengerTicket;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.springframework.core.convert.ConversionException;

/**
 * <p>Title: PassengerTicketConversion</p>
 * <p>
 * <p>Description:税务云客运汽车票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class PassengerTicketConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final PassengerTicketConversion INSTANCE = new PassengerTicketConversion();
    }

    private PassengerTicketConversion() {}

    public static PassengerTicketConversion getInstance() {
        return LazyHolder.INSTANCE;
    }
    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        String imageId = jsonObject.getString("imageId");
        String token = jsonObject.getString("token");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        dataImageFilesInfo.setNcImageId(imageId);
        DataPassengerTicket passengerTicket = new DataPassengerTicket();
        if(ObjectUtil.isNotEmpty(coordinate)){
            passengerTicket.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
            passengerTicket.setOrientation(Convert.toInt(coordinate.getString("degree")));
            passengerTicket.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
        }
        passengerTicket.setNcImageId(imageId);
        passengerTicket.setId(IdUtil.simpleUUID());
        passengerTicket.setFileId(dataImageFilesInfo.getFileId());
        passengerTicket.setInvoiceCode(data.getString("invoiceCode"));
        passengerTicket.setInvoiceNumber(data.getString("invoiceNum"));
        passengerTicket.setInvoiceDate(Convert.toDate(data.getString("date")));
        passengerTicket.setInvoiceTime(data.getString("time"));
        passengerTicket.setStationGetOn(data.getString("entrance"));
        passengerTicket.setStationGetOff(data.getString("exit"));
        passengerTicket.setName(data.getString("name"));
        passengerTicket.setInvoiceTotal(Convert.toBigDecimal(data.getString("totalAmount")));
        //passengerTicket.setImageId(imageId);
        passengerTicket.setSaveToken(token);

        return new IdentificationData<>(InvoiceConstants.PASSENGER_TICKET, passengerTicket);
    }
}
