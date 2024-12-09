package org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataRailwayTicket;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.springframework.core.convert.ConversionException;

/**
 * <p>Title: RailwayTicketConversion</p>
 * <p>
 * <p>Description:税务云火车票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class RailwayTicketConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final RailwayTicketConversion INSTANCE = new RailwayTicketConversion();
    }

    private RailwayTicketConversion() {}

    public static RailwayTicketConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject ) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        String imageId = jsonObject.getString("imageId");
        String token = jsonObject.getString("token");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        dataImageFilesInfo.setNcImageId(imageId);
        DataRailwayTicket railwayTicket = new DataRailwayTicket();
        if(ObjectUtil.isNotEmpty(coordinate)){
            railwayTicket.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
            railwayTicket.setOrientation(Convert.toInt(coordinate.getString("degree")));
            railwayTicket.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
        }
        railwayTicket.setNcImageId(imageId);
        railwayTicket.setFileId(dataImageFilesInfo.getFileId());
        railwayTicket.setId(IdUtil.simpleUUID());
        railwayTicket.setInvoiceDate(Convert.toDate(data.getString("date")));
        railwayTicket.setStationGetOff(data.getString("destination"));
        railwayTicket.setIdNumber(data.getString("idNumber"));
        railwayTicket.setInvoiceNumber(data.getString("invoiceNum"));
        railwayTicket.setSeat(data.getString("level"));
        railwayTicket.setStationGetOn(data.getString("origin"));
        railwayTicket.setSeatNum(data.getString("seatNo"));
        railwayTicket.setSerialNumber(data.getString("ticketNum"));
        railwayTicket.setInvoiceTime(data.getString("time"));
        railwayTicket.setInvoiceTotal(Convert.toBigDecimal(data.getString("totalAmount")));
        railwayTicket.setTrainNumber(data.getString("trainNum"));
        railwayTicket.setName(data.getString("name"));
        //railwayTicket.setImageId(imageId);
        railwayTicket.setSaveToken(token);

        return new IdentificationData<>(InvoiceConstants.RAILWAY_TICKET,railwayTicket);

    }



}
