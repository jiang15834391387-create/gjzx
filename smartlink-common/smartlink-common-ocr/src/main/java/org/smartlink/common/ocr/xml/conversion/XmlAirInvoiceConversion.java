package org.smartlink.common.ocr.xml.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataFlightItinerary;
import org.smartlink.common.entity.domain.business.domain.DataFlightsItineraryDetail;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XML航空运输电子客票行程单转换类
 */
public class XmlAirInvoiceConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final XmlAirInvoiceConversion INSTANCE = new XmlAirInvoiceConversion();
    }

    private XmlAirInvoiceConversion() {
    }

    public static XmlAirInvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject xmlData) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();

        // 获取xbrl节点下的所有数据
        JSONObject xbrlData = xmlData.getJSONObject("xbrli:xbrl");
        if (xbrlData == null) {
            return null;
        }

        // 主表信息
        DataFlightItinerary flightItinerary = new DataFlightItinerary();
        flightItinerary.setId(IdUtil.simpleUUID());
        flightItinerary.setFileId(dataImageFilesInfo.getFileId());
        flightItinerary.setUserName(getContent(xbrlData, "atr:PassengerName"));
        flightItinerary.setReceiptNumber(getContent(xbrlData, "atr:ETicketNumber"));
        flightItinerary.setInvoiceDate(getContent(xbrlData, "atr:IssueDate"));
        flightItinerary.setInvoiceTotal(getContent(xbrlData, "atr:TotalAmount"));
        flightItinerary.setBuyer(getContent(xbrlData, "atr:NameOfPurchaser"));
        flightItinerary.setBuyerTaxId(getContent(xbrlData, "atr:UnifiedSocialCreditCodeOfPurchaser"));
        flightItinerary.setSeller(getContent(xbrlData, "atr:IssueParty"));
        flightItinerary.setCheckCode(getContent(xbrlData, "atr:VerificationCode"));
        // 补充更多字段映射
        flightItinerary.setAgentCode(getContent(xbrlData, "atr:AgentCode"));
        flightItinerary.setCaacDevelopmentFund(getContent(xbrlData, "atr:CivilAviationDevelopmentFund"));
        flightItinerary.setFare(getContent(xbrlData, "atr:Fare"));
        flightItinerary.setFuelSurcharge(getContent(xbrlData, "atr:FuelSurcharge"));
        flightItinerary.setInsurance(getContent(xbrlData, "atr:Insurance"));
        flightItinerary.setInternationalFlag(getContent(xbrlData, "atr:MarkingOfDomesticOrInternational"));
        flightItinerary.setEndorsement(getContent(xbrlData, "atr:Endorsement"));
        flightItinerary.setIssuingStatus(getContent(xbrlData, "atr:IssuingStatus"));
        flightItinerary.setNumberOfGpOrder(getContent(xbrlData, "atr:NumberOfGpOrder"));
        flightItinerary.setPromptInformation(getContent(xbrlData, "atr:PromptInformation"));
        flightItinerary.setOtherTaxes(getContent(xbrlData, "atr:OtherTaxes"));
        flightItinerary.setTaxRate(getContent(xbrlData, "atr:VatRate"));
        flightItinerary.setTax(getContent(xbrlData, "atr:VatTaxAmount"));
        flightItinerary.setUserId(getContent(xbrlData, "atr:ValidIdNumber"));

        // 明细信息列表
        List<DataFlightsItineraryDetail> detailList = new ArrayList<>();
        Object detailObj = xbrlData.get("atr:DetailInformationOfAirTicketTuple");
        if (detailObj instanceof JSONArray) {
            JSONArray details = (JSONArray) detailObj;
            for (Object item : details) {
                if (item instanceof JSONObject) {
                    JSONObject detail = (JSONObject) item;
                    DataFlightsItineraryDetail detailEntity = new DataFlightsItineraryDetail();
                    detailEntity.setId(IdUtil.simpleUUID());
                    detailEntity.setOcrId(flightItinerary.getId());
                    detailEntity.setFileId(dataImageFilesInfo.getFileId());
                    detailEntity.setStationGetOn(getContent(detail, "atr:DepartureStation"));
                    detailEntity.setStationGetOff(getContent(detail, "atr:DestinationStation"));
                    detailEntity.setCarrier(getContent(detail, "atr:Carrier"));
                    detailEntity.setFlightNumber(getContent(detail, "atr:Flight"));
                    detailEntity.setInvoiceTime(getContent(detail, "atr:DepartureTime"));
                    detailEntity.setInvoiceDate(getContent(detail, "atr:CarrierDate"));
                    detailEntity.setSeat(getContent(detail, "atr:Class"));
                    detailEntity.setFareBasis(getContent(detail, "atr:FareBasis"));
                    detailEntity.setAllow(getContent(detail, "atr:FreeBaggageAllowance"));
                    detailEntity.setFlightSegment(getContent(detail, "atr:FlightSegment"));
                    detailEntity.setUserName(flightItinerary.getUserName());
                    detailEntity.setUserId(flightItinerary.getUserId());
                    detailList.add(detailEntity);
                }
            }
        } else if (detailObj instanceof JSONObject) {
            // 处理单个明细的情况
            JSONObject detail = (JSONObject) detailObj;
            DataFlightsItineraryDetail detailEntity = new DataFlightsItineraryDetail();
            detailEntity.setId(IdUtil.simpleUUID());
            detailEntity.setOcrId(flightItinerary.getId());
            detailEntity.setFileId(dataImageFilesInfo.getFileId());
            detailEntity.setStationGetOn(getContent(detail, "atr:DepartureStation"));
            detailEntity.setStationGetOff(getContent(detail, "atr:DestinationStation"));
            detailEntity.setCarrier(getContent(detail, "atr:Carrier"));
            detailEntity.setFlightNumber(getContent(detail, "atr:Flight"));
            detailEntity.setInvoiceTime(getContent(detail, "atr:DepartureTime"));
            detailEntity.setInvoiceDate(getContent(detail, "atr:CarrierDate"));
            detailEntity.setSeat(getContent(detail, "atr:Class"));
            detailEntity.setFareBasis(getContent(detail, "atr:FareBasis"));
            detailEntity.setAllow(getContent(detail, "atr:FreeBaggageAllowance"));
            detailEntity.setFlightSegment(getContent(detail, "atr:FlightSegment"));
            detailEntity.setUserName(flightItinerary.getUserName());
            detailEntity.setUserId(flightItinerary.getUserId());
            detailList.add(detailEntity);
        }

        // 设置查验状态
        dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());

        // 设置明细表
        flightItinerary.setDetails(detailList);
        flightItinerary.setFlightItineraryDetails(detailList);
        
        // 添加主表到结果
        resultsList.add(new IdentificationData<>("flight_itinerary", flightItinerary, detailList, "航空电子客票行程单识别成功"));

        return resultsList;
    }

    /**
     * 从JSON对象中获取指定key的content值
     */
    private String getContent(JSONObject data, String key) {
        if (data == null || !data.containsKey(key)) {
            return null;
        }

        Object value = data.get(key);
        if (value instanceof Map) {
            return ((Map<?, ?>) value).get("content") != null ? ((Map<?, ?>) value).get("content").toString() : null;
        }

        return value != null ? value.toString() : null;
    }
}