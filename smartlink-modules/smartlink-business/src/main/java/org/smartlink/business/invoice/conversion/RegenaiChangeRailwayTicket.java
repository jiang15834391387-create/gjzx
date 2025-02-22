package org.smartlink.business.invoice.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.springframework.stereotype.Component;
/**
 *火车票
 */
@Component
public class RegenaiChangeRailwayTicket{
    public void changeRailwayTicket(JSONObject jsonObject, DataRailwayTicket dataRailwayTicket) {
        dataRailwayTicket.setInvoiceNumber(jsonObject.getStr("number"));
        //乘车日期
        dataRailwayTicket.setInvoiceDate(jsonObject.getStr("date"));
        // 总金额
        dataRailwayTicket.setInvoiceTotal(jsonObject.getStr("total"));
        //购买方名称
        dataRailwayTicket.setBuyer(jsonObject.getStr("buyer"));
        //社会信用代码
        dataRailwayTicket.setBuyerTaxId(jsonObject.getStr("buyer_tax_id"));
        //身份证号码
        dataRailwayTicket.setIdNumber(jsonObject.getStr("user_id"));
        //上车车站
        dataRailwayTicket.setStationGetOn(jsonObject.getStr("station_geton"));
        //下车车站
        dataRailwayTicket.setStationGetOff(jsonObject.getStr("station_getoff"));
        //乘车时间
        dataRailwayTicket.setInvoiceTime(jsonObject.getStr("time"));
        //姓名
        dataRailwayTicket.setName(jsonObject.getStr("name"));
        //车次
        dataRailwayTicket.setTrainNumber(jsonObject.getStr("train_number"));
        //座位类型
        dataRailwayTicket.setSeat(jsonObject.getStr("seat"));
        //座位号
        dataRailwayTicket.setSeatNum(jsonObject.getStr("seat_number"));

    }

}
