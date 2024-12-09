package org.smartlink.server.nc.ocr.service.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataDidiItinerary;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.glority.response.IdentifyResults;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 滴滴行程单
 *
 * @author L
 **/
public class DidiItineraryConversion implements ChangeIdentifyInfo<IdentifyResults> {


    private static class LazyHolder {
        private static final DidiItineraryConversion INSTANCE = new DidiItineraryConversion();
    }

    private DidiItineraryConversion() {
    }

    public static DidiItineraryConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {
        JSONObject jsonObject = identifyResults.getDetails();
        String json = identifyResults.getDetails().toJSONString()
                .replaceAll("date_start", "dateStart")
                .replaceAll("date_end", "dateEnd")
                .replaceAll("car_type", "carType")
                .replaceAll("items", "details")
                .replaceAll("time_geton", "timeGetOn")
                .replaceAll("station_geton", "stationGetOn")
                .replaceAll("station_getoff", "stationGetOff");
        DataDidiItinerary didiItinerary = JSONObject.parseObject(json, DataDidiItinerary.class);
        if (null == didiItinerary) {
            return null;
        }
        didiItinerary.setId(IdUtil.simpleUUID());
        didiItinerary.setFileId(dataImageFilesInfo.getFileId());
        didiItinerary.setInvoiceDate(Convert.toDate(jsonObject.getString("date"), new Date()));
        didiItinerary.setInvoiceTotal(jsonObject.getBigDecimal("total"));
        didiItinerary.setTimeGetOn(jsonObject.getString("date_start"));
        didiItinerary.setTimeGetOff(jsonObject.getString("date_end"));
        didiItinerary.setPhone(jsonObject.getString("phone"));
        didiItinerary.setCoordinate(identifyResults.getRegion());
        didiItinerary.setOrientation(Integer.parseInt("-"+identifyResults.getOrientation()));
        JSONArray list = jsonObject.getJSONArray("items");
        if (ObjectUtil.isNotEmpty(didiItinerary.getDetails())) {
            didiItinerary.getDetails().forEach(e -> {
                for (Object didiDetails : list) {
                    LinkedHashMap<String, String> fJson = (LinkedHashMap) didiDetails;
                    e.setCarType(fJson.get("car_type"));
                    e.setTimeGetOn(fJson.get("time_geton"));
                    e.setTimeGetOff(fJson.get("time_getoff"));
                    e.setCity(fJson.get("city"));
                    e.setStationGetOn(fJson.get("station_geton"));
                    e.setStationGetOff(fJson.get("station_getoff"));
                    e.setMileage(fJson.get("mileage"));
                    e.setProducer(fJson.get("producer"));
                    e.setTimeOrder(fJson.get("time_order"));
                }
            });
        }
        dataImageFilesInfo.setFileType(InvoiceConstants.DIDI_ITINERARY);
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), didiItinerary);
    }
}
