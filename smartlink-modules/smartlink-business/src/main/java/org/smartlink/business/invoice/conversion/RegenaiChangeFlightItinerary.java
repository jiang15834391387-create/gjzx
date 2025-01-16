package org.smartlink.business.invoice.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.entity.domain.business.domain.DataFlightItinerary;
import org.springframework.stereotype.Component;
/**
 * 航空运输电子客运行程单基本信息转换
 *
 */
@Component
public class RegenaiChangeFlightItinerary {
    public void changeFlightItinerary(JSONObject jsonObject, DataFlightItinerary dataFlightItinerary) {
        //发票号码
        dataFlightItinerary.setInvoiceNumber(jsonObject.getStr("receipt_number"));
        //开票日期
        dataFlightItinerary.setInvoiceDate(jsonObject.getStr("date"));
        //税额
        dataFlightItinerary.setTax(jsonObject.getStr("tax"));
        //总额
        dataFlightItinerary.setInvoiceTotal(jsonObject.getStr("total"));
        //购买方名称
        dataFlightItinerary.setBuyer(jsonObject.getStr("buyer"));
        //购买方纳税人识别号
        dataFlightItinerary.setBuyerTaxId(jsonObject.getStr("buyer_tax_id"));
        //旅客名字
        dataFlightItinerary.setUserName(jsonObject.getStr("user_name"));
        //国内国际标签
        dataFlightItinerary.setInternationalFlag(jsonObject.getStr("international_flag"));
        //Gp订单号
        dataFlightItinerary.setNumberOfGpOrder(jsonObject.getStr("number_of_gp_order"));
    }
}
