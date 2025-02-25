package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 滴滴行程单
 *
 * @author maxuhui
 **/
public class DidiItineraryConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {


    private static class LazyHolder {
        private static final DidiItineraryConversion INSTANCE = new DidiItineraryConversion();
    }

    private DidiItineraryConversion() {
    }

    public static DidiItineraryConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataDidiItinerary didiItinerary = new DataDidiItinerary();
            if (null == jsonObject) {
                return null;
            }
            didiItinerary.setId(IdUtil.simpleUUID());
            didiItinerary.setFileId(dataImageFilesInfo.getFileId());
            didiItinerary.setInvoiceDate(jsonObject.getStr("date"));
            didiItinerary.setInvoiceTotal(jsonObject.getStr("total"));
            didiItinerary.setTimeGetOn(jsonObject.getStr("date_start"));
            didiItinerary.setTimeGetOff(jsonObject.getStr("date_end"));
            didiItinerary.setPhone(jsonObject.getStr("phone"));
            didiItinerary.setTitle(jsonObject.getStr("title"));
            didiItinerary.setKind(jsonObject.getStr("kind"));

            JSONArray list = jsonObject.getJSONArray("items");
            List<DataDidiItineraryDetails> ocrDetailsList = new ArrayList<>();
            if (ObjectUtil.isNotNull(list)) {
                for (Object invoiceDetails : list) {
                    DataDidiItineraryDetails e = new DataDidiItineraryDetails();
                    LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                    e.setId(IdUtil.fastSimpleUUID());
                    e.setFileId(dataImageFilesInfo.getFileId());
                    e.setCarType(fJson.containsKey("car_type") ? fJson.get("car_type") : null);
                    e.setTimeGetOn(fJson.containsKey("time_geton") ? fJson.get("time_geton") : null);
                    e.setTimeGetOff(fJson.containsKey("time_getoff") ? fJson.get("time_getoff") : null);
                    e.setCity(fJson.containsKey("city") ? fJson.get("city") : null);
                    e.setStationGetOn(fJson.containsKey("station_geton") ? fJson.get("station_geton") : null);
                    e.setStationGetOff(fJson.containsKey("station_getoff") ? fJson.get("station_getoff") : null);
                    e.setMileage(fJson.containsKey("mileage") ? fJson.get("mileage") : null);
                    e.setProducer(fJson.containsKey("producer") ? fJson.get("producer") : null);
                    e.setTimeOrder(fJson.containsKey("time_order") ? fJson.get("time_order") : null);
                    ocrDetailsList.add(e);
                }
            }
            didiItinerary.setDetails(ocrDetailsList);

            didiItinerary.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                didiItinerary.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                didiItinerary.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_DIDI_ITINERARY_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_DIDI_ITINERARY_CODE.getCode(), didiItinerary, identifyResults.getExtra(), identifyResults.getMessage()));

        }
        return resultsList;
    }

}
