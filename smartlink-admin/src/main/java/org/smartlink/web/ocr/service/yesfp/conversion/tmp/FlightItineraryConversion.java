package org.smartlink.web.ocr.service.yesfp.conversion.tmp;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.invoice.DataFlightItinerary;
import org.smartlink.web.domain.invoice.DataFlights;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.smartlink.web.ocr.service.yesfp.utils.YesfpCoordinateUtil;
import org.springframework.core.convert.ConversionException;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: FlightItineraryConversion</p>
 * <p>
 * <p>Description:税务云飞机行程单识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class FlightItineraryConversion implements ChangeIdentifyInfo<JSONObject> {


    private static class LazyHolder {
        private static final FlightItineraryConversion INSTANCE = new FlightItineraryConversion();
    }

    private FlightItineraryConversion() {
    }

    public static FlightItineraryConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject jsonObject) throws ConversionException {
        JSONObject data = jsonObject.getJSONObject("data");
        String imageId = jsonObject.getString("imageId");
        String token = jsonObject.getString("token");
        JSONObject coordinate = jsonObject.getJSONObject("coordinate");
        JSONArray itemList = data.getJSONArray("itemList");
        List<DataFlights> details = new ArrayList<>();
        dataImageFilesInfo.setNcImageId(imageId);
        DataFlightItinerary flightItinerary = new DataFlightItinerary();
        if(ObjectUtil.isNotEmpty(coordinate)){
            flightItinerary.setCoordinate(YesfpCoordinateUtil.getCoordinateArr(coordinate));
            flightItinerary.setOrientation(Convert.toInt(coordinate.getString("degree")));
            flightItinerary.setCoordinateStr(YesfpCoordinateUtil.getCoordinateStr(coordinate));
        }
        flightItinerary.setNcImageId(imageId);
        flightItinerary.setId(IdUtil.simpleUUID());
        flightItinerary.setFileId(dataImageFilesInfo.getFileId());
        flightItinerary.setAgentCode(data.getString("agentCode"));
        flightItinerary.setCaacDevelopmentFund(data.getString("caacDevelopFund"));
        flightItinerary.setCheckCode(data.getString("checkCode"));
        flightItinerary.setInvoiceDate(Convert.toDate(data.getString("date")));
        flightItinerary.setFare(Convert.toBigDecimal(data.getString("fare")));
        flightItinerary.setFuelSurcharge(Convert.toBigDecimal(data.getString("fuelSurcharge")));
        flightItinerary.setInsurance(Convert.toBigDecimal(data.getString("insurance")));
        flightItinerary.setInvoiceNumber(data.getString("invoiceNum"));
        flightItinerary.setIssueBy(data.getString("issueBy"));
        flightItinerary.setTax(Convert.toBigDecimal(data.getString("tax")));
        flightItinerary.setInvoiceTotal(Convert.toBigDecimal(data.getString("totalAmount")));
        Integer airportType = Convert.toInt(data.getString("airportType"));
        flightItinerary.setInternationalFlag(String.valueOf(airportType));
        //flightItinerary.setImageId(imageId);
        flightItinerary.setSaveToken(token);

        itemList.forEach((item)->{
            JSONObject detail = (JSONObject) (item);
            DataFlights flight = new DataFlights();
            flight.setUserId(data.getString("userId"));
            flight.setId(IdUtil.simpleUUID());
            flight.setFileId(dataImageFilesInfo.getFileId());
            flight.setCarrier(detail.getString("carrier"));
            flight.setInvoiceDate(Convert.toDate(detail.getString("date")));
            flight.setFlightNumber(detail.getString("flightNumber"));
            flight.setStationGetOn(detail.getString("from"));
            flight.setSeat(detail.getString("seat"));
            flight.setUserName(data.getString("userName"));
            flight.setInvoiceTime(detail.getString("time"));
            flight.setStationGetOff(detail.getString("to"));
            details.add(flight);
        });
        flightItinerary.setDataFlights(details);

        return new IdentificationData<>(InvoiceConstants.FLIGHT_ITINERARY, flightItinerary);
    }
}
