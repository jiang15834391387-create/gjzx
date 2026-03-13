package org.smartlink.business.invoice.autoinvcheck.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.springframework.stereotype.Component;

/**
 * 铁路电子客票信息转换
 *
 */
@Component
public class AutoinvRailwayTicketConversion {

    public void changeRailwayTicket(JSONObject jsonObject, DataRailwayTicket railwayTicket) {
//        railwayTicket.setInvoiceCode(jsonObject.getStr("Code"));
        railwayTicket.setInvoiceNumber(jsonObject.getStr("No"));
        // 开票日期
        railwayTicket.setInvoiceDate(jsonObject.getStr("Date"));
        // 合计金额
        railwayTicket.setInvoiceTotal(jsonObject.getStr("SummaryAmount"));
        // 购买方名称
        railwayTicket.setBuyer(jsonObject.getStr("BuyerName"));
        // 社会统一信用代码
        railwayTicket.setBuyerTaxId(jsonObject.getStr("BuyerTaxCode"));
        // 姓名
        railwayTicket.setName(jsonObject.getStr("Name"));
        // 证件号码
        railwayTicket.setIdNumber(jsonObject.getStr("IdNo"));
        // 出发站
        railwayTicket.setStationGetOn(jsonObject.getStr("StationFrom"));
        // 到达站
        railwayTicket.setStationGetOff(jsonObject.getStr("StationTo"));
        // 车次号
        railwayTicket.setTrainNumber(jsonObject.getStr("TrainNo"));
        // 乘车日期
        if (StringUtils.isNotBlank(jsonObject.getStr("DepartureDate"))) {
            railwayTicket.setInvoiceDate(jsonObject.getStr("DepartureDate"));
        }
        // 出发时间
        railwayTicket.setInvoiceTime(jsonObject.getStr("DepartureTime"));
        // 席别
        railwayTicket.setSeat(jsonObject.getStr("SeatClass"));
        // 车厢
        railwayTicket.setSeatNum(jsonObject.getStr("CarriageNo"));
        // 席位
        railwayTicket.setSeatNum(railwayTicket.getSeatNum() + " " + jsonObject.getStr("Seat"));
        // 票种
        railwayTicket.setTypeOfRailwayTicket(jsonObject.getStr("TicketType"));
        // 电子客票号
        railwayTicket.setSerialNumber(jsonObject.getStr("ETicketNo"));
        // 空调类型
        railwayTicket.setAirConditioning(jsonObject.getStr("AirConditionType"));
        // 备注
        railwayTicket.setRemark(jsonObject.getStr("Remark"));
        // 业务类型
        railwayTicket.setTypeOfBusiness(jsonObject.getStr("BusinessType"));
    }
}
