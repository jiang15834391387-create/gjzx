package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataFlightItinerary;
import org.smartlink.common.entity.domain.business.domain.DataFlightsItineraryDetail;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 航空运输电子客票行程单转换类
 */
public class AutoinvFlightItineraryConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvFlightItineraryConversion INSTANCE = new AutoinvFlightItineraryConversion();
    }

    private AutoinvFlightItineraryConversion() {}

    public static AutoinvFlightItineraryConversion getInstance() {
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

            DataFlightItinerary flightItinerary = new DataFlightItinerary();
            flightItinerary.setId(IdUtil.simpleUUID());
            flightItinerary.setFileId(dataImageFilesInfo.getFileId());

            // 设置行程单基本信息
            flightItinerary.setInvoiceNumber(jsonObject.getStr("e_ticket_no"));
            flightItinerary.setInvoiceDate(jsonObject.getStr("date_of_issue"));
            flightItinerary.setUserName(jsonObject.getStr("name"));
            flightItinerary.setInvoiceTotal(jsonObject.getStr("amount_little"));
            flightItinerary.setFare(jsonObject.getStr("fare"));
            flightItinerary.setTax(jsonObject.getStr("tax_amount"));
            flightItinerary.setTaxRate(jsonObject.getStr("tax_rate"));
            flightItinerary.setInsurance(jsonObject.getStr("insurance"));
            flightItinerary.setFuelSurcharge(jsonObject.getStr("fuel_surcharge"));
            flightItinerary.setOtherTaxes(jsonObject.getStr("other_taxes"));
            flightItinerary.setCaacDevelopmentFund(jsonObject.getStr("caac_development_fund"));
            flightItinerary.setUserId(jsonObject.getStr("id"));
            flightItinerary.setPrintNumber(jsonObject.getStr("serial_number"));
            flightItinerary.setAgentCode(jsonObject.getStr("agent_code"));
            flightItinerary.setEndorsement(jsonObject.getStr("endorsements"));
            flightItinerary.setCheckCode(jsonObject.getStr("ck"));
            flightItinerary.setIssueBy(jsonObject.getStr("issued_by"));

            // 处理航班详情
            List<DataFlightsItineraryDetail> flightsList = new ArrayList<>();
            JSONArray flightsArray = jsonObject.getJSONArray("flights");
            if (flightsArray != null && flightsArray.size() > 0) {
                for (int i = 0; i < flightsArray.size(); i++) {
                    JSONObject flightObj = flightsArray.getJSONObject(i);
                    DataFlightsItineraryDetail flightDetail = new DataFlightsItineraryDetail();
                    flightDetail.setId(IdUtil.simpleUUID());
                    flightDetail.setOcrId(flightItinerary.getId());
                    flightDetail.setFileId(dataImageFilesInfo.getFileId());

                    // 映射航班详情字段
                    flightDetail.setCarrier(flightObj.getStr("carrier"));
                    flightDetail.setInvoiceDate(flightObj.getStr("date"));
                    flightDetail.setFlightNumber(flightObj.getStr("flight_no"));
                    flightDetail.setStationGetOn(flightObj.getStr("from"));
                    flightDetail.setStationGetOff(flightObj.getStr("to"));
                    flightDetail.setSeat(flightObj.getStr("seat_class"));
                    flightDetail.setInvoiceTime(flightObj.getStr("time"));
                    flightDetail.setFareBasis(flightObj.getStr("fare_basis"));
                    flightDetail.setAllow(flightObj.getStr("allow"));
                    flightDetail.setEffectiveDate(flightObj.getStr("not_valid_before"));
                    flightDetail.setExpiryDate(flightObj.getStr("not_valid_after"));
                    flightDetail.setFlightSegment(String.valueOf(i + 1));

                    flightsList.add(flightDetail);
                }
            }

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE, flightItinerary, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
