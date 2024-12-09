package org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataTaxiTickets;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.springframework.core.convert.ConversionException;

/**
 * <p>Title: TaxiTicketsConversion</p>
 * <p>
 * <p>Description:税务云出租车发票识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class TaxiTicketsConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {

        private static final TaxiTicketsConversion INSTANCE = new TaxiTicketsConversion();
    }

    private TaxiTicketsConversion() {}

    public static TaxiTicketsConversion getInstance() {

        return LazyHolder.INSTANCE;
    }


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        String imageId = jsonObject.getString("imageId");
        String token = jsonObject.getString("token");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        dataImageFilesInfo.setNcImageId(imageId);
        DataTaxiTickets taxiTickets=  new DataTaxiTickets();
        if(ObjectUtil.isNotEmpty(coordinate)){
            taxiTickets.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
            taxiTickets.setOrientation(Convert.toInt(coordinate.getString("degree")));
            taxiTickets.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
        }
        taxiTickets.setNcImageId(imageId);
        taxiTickets.setId(IdUtil.simpleUUID());
        taxiTickets.setFileId(dataImageFilesInfo.getFileId());
        taxiTickets.setInvoiceCode(data.getString("invoiceCode"));
        taxiTickets.setInvoiceNumber(data.getString("invoiceNum"));
        taxiTickets.setInvoiceDate(Convert.toDate(data.getString("date")));
        taxiTickets.setLicensePlate(data.getString("carNum"));
        taxiTickets.setMileage(data.getString("mileage"));
        taxiTickets.setTimeGetOn(data.getString("startTime"));
        taxiTickets.setTimeGetOff(data.getString("endTime"));
        //taxiTickets.setAmount(data.getString("amount"));
        taxiTickets.setInvoiceTotal(Convert.toBigDecimal(data.getString("totalAmount")));
        taxiTickets.setProvince(data.getString("province"));
        taxiTickets.setPlace(data.getString("place"));
        taxiTickets.setCity(data.getString("city"));
        //taxiTickets.setImageId(imageId);
        taxiTickets.setSaveToken(token);

        return new IdentificationData<>(InvoiceConstants.TAXI_TICKETS,taxiTickets);
    }
}
