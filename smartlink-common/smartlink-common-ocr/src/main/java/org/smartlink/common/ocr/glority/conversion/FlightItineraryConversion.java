package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataFlightItinerary;
import org.smartlink.common.entity.domain.business.domain.DataFlightsItineraryDetail;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 航空运输电子客票行程单
 *
 * @author maxuhui
 **/
public class FlightItineraryConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {


    private static class LazyHolder {

        private static final FlightItineraryConversion INSTANCE = new FlightItineraryConversion();
    }

    private FlightItineraryConversion() {
    }

    public static FlightItineraryConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

        JSONObject jsonObject = identifyResults.getDetails();

        DataFlightItinerary flightItinerary = new DataFlightItinerary();
        if (null == jsonObject) {
            return null;
        }
        flightItinerary.setId(IdUtil.simpleUUID());
        flightItinerary.setFileId(dataImageFilesInfo.getFileId());
        flightItinerary.setInsurance(jsonObject.getStr("insurance"));
        flightItinerary.setInvoiceNumber(jsonObject.getStr("number"));
        flightItinerary.setInvoiceDate(jsonObject.getStr("date"));
        flightItinerary.setFuelSurcharge(jsonObject.getStr("fuel_surcharge"));
        flightItinerary.setInvoiceTotal(jsonObject.getStr("total"));
        flightItinerary.setCheckCode(jsonObject.getStr("check_code"));
        flightItinerary.setIssueBy(jsonObject.getStr("issue_by"));
        flightItinerary.setInternationalFlag(jsonObject.getStr("international_flag"));
        flightItinerary.setCaacDevelopmentFund(jsonObject.getStr("caac_development_fund"));
        flightItinerary.setAgentCode(jsonObject.getStr("agentcode"));
        flightItinerary.setFare(jsonObject.getStr("fare"));
        flightItinerary.setTax(jsonObject.getStr("tax"));


            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                flightItinerary.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                flightItinerary.setRegion(null);
            }
            flightItinerary.setOrientation(identifyResults.getOrientation());

            List<DataFlightsItineraryDetail> ocrDetailsList = new ArrayList<>();
            JSONArray list = jsonObject.getJSONArray("flights");
            if (ObjectUtil.isNotNull(list)) {
                for (Object invoiceDetails : list) {
                    DataFlightsItineraryDetail e = new DataFlightsItineraryDetail();
                    LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                    e.setStationGetOff(fJson.containsKey("to") ? fJson.get("to") : null);
                    e.setStationGetOn(fJson.containsKey("from") ? fJson.get("from") : null);
                    e.setSpaceLevel(fJson.containsKey("class_name") ? fJson.get("class_name") : null);
                    e.setFlightNumber(fJson.containsKey("flight_number") ? fJson.get("flight_number") : null);
                    e.setInvoiceDate(fJson.containsKey("date") ? fJson.get("date") : null);
                    e.setSeat(fJson.containsKey("seat") ? fJson.get("seat") : null);
                    e.setCarrier(fJson.containsKey("carrier") ? fJson.get("carrier") : null);
                    e.setAllow(fJson.containsKey("allow") ? fJson.get("allow") : null);
                    e.setFareBasis(fJson.containsKey("fare_basis") ? fJson.get("fare_basis") : null);
                    e.setEffectiveDate(fJson.containsKey("not_valid_before") ? fJson.get("not_valid_before") : null);
                    e.setExpiryDate(fJson.containsKey("not_valid_after") ? fJson.get("not_valid_after") : null);
                    e.setFlightSegment(fJson.containsKey("flight_segment") ? fJson.get("flight_segment") : null);
                    ocrDetailsList.add(e);
                }
            }
            flightItinerary.setDetails(ocrDetailsList);

            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_FLIGHT_ITINERARY_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_FLIGHT_ITINERARY_CODE.getCode(), flightItinerary, identifyResults.getExtra(), identifyResults.getMessage()));
        }
        return resultsList;

    }
}
