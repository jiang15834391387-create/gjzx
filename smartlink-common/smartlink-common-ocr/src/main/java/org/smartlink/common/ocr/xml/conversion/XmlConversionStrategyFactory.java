package org.smartlink.common.ocr.xml.conversion;

import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.exception.OcrException;
import cn.hutool.json.JSONObject;

/**
 * XML发票转换策略工厂
 */
public class XmlConversionStrategyFactory {

    private XmlConversionStrategyFactory() {
    }

    /**
     * 根据发票类型获取对应的转换策略
     * @param invoiceType 发票类型
     * @return 转换策略实例
     * @throws OcrException 当不支持的发票类型时抛出异常
     */
    public static ChangeIdentifyInfo<JSONObject> getConversionStrategy(String invoiceType) throws OcrException {
//        if (invoiceType == null) {
//            // 默认使用增值税发票转换策略
//            return XmlInvoiceConversion.getInstance();
//        }

        // 根据发票类型选择不同的转换策略
        switch (invoiceType) {
            case "增值税电子专用发票":
            case "增值税专用发票":
            case "增值税电子普通发票":
            case "增值税普通发票":
                return XmlInvoiceConversion.getInstance();
            case "铁路电子客票":
            case "火车票":
            case "铁路":
                return XmlRailwayInvoiceConversion.getInstance();
            case "航空运输电子客票行程单":
            case "航空":
            case "机票":
                return XmlAirInvoiceConversion.getInstance();

            default:
                throw new OcrException("不支持的XML发票类型: " + invoiceType);
        }
    }

    /**
     * 根据XML数据内容自动判断并获取对应的转换策略
     * @param xmlData XML数据
     * @return 转换策略实例
     * @throws OcrException 当不支持的XML发票类型时抛出异常
     */
    public static ChangeIdentifyInfo<JSONObject> getConversionStrategyByContent(JSONObject xmlData) throws OcrException {
        if (xmlData == null) {
            return XmlInvoiceConversion.getInstance();
        }



        JSONObject xbrlData = xmlData.getJSONObject("xbrli:xbrl");
        if (xbrlData == null) {
            return getConversionStrategy("增值税电子专用发票");
        }

        // 检查是否为航空发票（包含atr命名空间）
        boolean isAir = false;
        for (String key : xbrlData.keySet()) {
            if (key.startsWith("atr:")) {
                isAir = true;
                break;
            }
        }

        if (isAir) {
            return getConversionStrategy("航空运输电子客票行程单");
        }

        // 检查是否为铁路发票（包含TypeOfVoucher字段）
        if (xbrlData.containsKey("inv:TypeOfVoucher")) {
            return getConversionStrategy("铁路电子客票");
        }

        // 默认使用增值税发票转换策略
        return getConversionStrategy("增值税电子专用发票");
    }
}
