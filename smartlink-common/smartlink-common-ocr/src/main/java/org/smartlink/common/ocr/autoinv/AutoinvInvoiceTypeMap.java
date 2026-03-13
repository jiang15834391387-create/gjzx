package org.smartlink.common.ocr.autoinv;

import org.smartlink.common.ocr.constant.InvoiceConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * autoinv 发票类型映射类
 * 将autoinv的发票类型编码映射到系统统一的编码
 */
public class AutoinvInvoiceTypeMap {
    private static final Map<String, String> TYPE_MAP = new HashMap<>();
    
    static {
        // 增值税专用发票
        TYPE_MAP.put("20101", InvoiceConstants.GLORITY_TAX_SPECIAL_CODE);
        // 增值税普通发票
        TYPE_MAP.put("20102", InvoiceConstants.GLORITY_TAX_CODE);
        // 增值税电子普通发票
        TYPE_MAP.put("20103", InvoiceConstants.GLORITY_ELECTRONIC_CODE);
        // 增值税普通发票(卷票)
        TYPE_MAP.put("20104", InvoiceConstants.GLORITY_ROLL_TICKET_CODE);
        // 机动车销售统一发票
        TYPE_MAP.put("20105", InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE);
        // 二手车销售统一发票
        TYPE_MAP.put("20106", InvoiceConstants.GLORITY_USED_CAR_SALES_CODE);
        // 增值税发票销货清单
        TYPE_MAP.put("20107", InvoiceConstants.DIGITAL_INVOICE_LIST);
        // 增值税电子专用发票
        TYPE_MAP.put("20108", InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE);
        // 区块链发票
        TYPE_MAP.put("20109", InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE);
        // 电子发票(数电发票)
        TYPE_MAP.put("20110", InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE);
        // 火车票
        TYPE_MAP.put("20201", InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE);
        // 出租车票
        TYPE_MAP.put("20202", InvoiceConstants.GLORITY_TAXI_TICKETS_CODE);
        // 航空运输电子客票行程单
        TYPE_MAP.put("20203", InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE);
        // 客运票
        TYPE_MAP.put("20204", InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE);
        // 过路费发票
        TYPE_MAP.put("20205", InvoiceConstants.GLORITY_TOLL_ROADS_CODE);
        // 定额发票
        TYPE_MAP.put("20301", InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE);
        // 机打发票
        TYPE_MAP.put("20401", InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE);
        // 通用手工发票
        TYPE_MAP.put("20601", InvoiceConstants.GLORITY_MANUAL_INVOICE_CODE);
        // 政府非税收入
        TYPE_MAP.put("20602", InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE);
    }
    
    /**
     * 根据autoinv发票类型获取系统统一发票类型
     * @param autoinvType autoinv发票类型编码
     * @return 系统统一发票类型编码
     */
    public static String getSystemType(String autoinvType) {
        return TYPE_MAP.getOrDefault(autoinvType, null);
    }
}