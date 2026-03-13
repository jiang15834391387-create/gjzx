package org.smartlink.business.invoice.autoinvcheck.conversion;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataFlightItinerary;
import org.smartlink.common.entity.domain.business.domain.DataFlightsItineraryDetail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 航空运输电子客票行程单信息转换
 *
 */
@Component
public class AutoinvFlightItineraryConversion {

    public void changeFlightItinerary(JSONObject jsonObject, DataFlightItinerary flightItinerary) {
//        flightItinerary.setInvoiceCode(jsonObject.getStr("Code"));
        flightItinerary.setInvoiceNumber(jsonObject.getStr("No"));
        // 开票日期
        flightItinerary.setInvoiceDate(jsonObject.getStr("Date"));
        // 购买方名称
        flightItinerary.setBuyer(jsonObject.getStr("BuyerName"));
        // 购买方纳税人识别号
        flightItinerary.setBuyerTaxId(jsonObject.getStr("BuyerTaxCode"));
        // 旅客姓名
        flightItinerary.setUserName(jsonObject.getStr("Name"));
        // 身份证号
        flightItinerary.setUserId(jsonObject.getStr("IdNo"));
        // 税额
        flightItinerary.setTax(jsonObject.getStr("TaxAmount"));
        // 总计金额
        flightItinerary.setInvoiceTotal(jsonObject.getStr("SummaryAmount"));
        // 国内国际标志
        flightItinerary.setInternationalFlag(jsonObject.getStr("DomesticOrIntl"));
        // GP订单号
        flightItinerary.setNumberOfGpOrder(jsonObject.getStr("GPNo"));
        // 电子客票号码
        flightItinerary.setReceiptNumber(jsonObject.getStr("ETicketNo"));
        // 备注
        flightItinerary.setRemark(jsonObject.getStr("Remark"));
        // 处理明细信息
        JSONArray items = jsonObject.getJSONArray("Items");
        if (items != null && items.size() > 0) {
            List<DataFlightsItineraryDetail> details = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                DataFlightsItineraryDetail detail = new DataFlightsItineraryDetail();
                // 航段
                detail.setFlightSegment(item.getStr("InvoiceDetailNumber"));
                // 出发站
                detail.setStationGetOn(item.getStr("From"));
                // 目的站
                detail.setStationGetOff(item.getStr("To"));
                // 承运人
                detail.setCarrier(item.getStr("Carrier"));
                // 航班号
                detail.setFlightNumber(item.getStr("FlightNo"));
                // 座位等级
                detail.setSeat(item.getStr("SeatClass"));
                // 承运日期
                detail.setInvoiceTime(item.getStr("Date"));
//                // 起飞时间
//                detail.setInvoiceTime(item.getStr("Time"));
                // 客票级别
                detail.setFareBasis(item.getStr("FareBasis"));
                details.add(detail);
            }
            flightItinerary.setDetails(details);
            flightItinerary.setFlightItineraryDetails(details);
        }
    }
}
