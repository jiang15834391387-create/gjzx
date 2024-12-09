package org.smartlink.server.nc.constant;

import cn.hutool.core.collection.ListUtil;

import java.util.List;

/**
 * NC配置常量
 *
 * @author L
 */
public interface NcConstant {

    /**
     * NC模块KEY
     */
    String SYS_NC_KEY = "sys_nc:";

    /**
     * NC配置KEY
     */
    String NC_CONFIG_KEY = "NcConfig";

    /**
     * 缓存配置KEY
     */
    String CACHE_CONFIG_KEY =  SYS_NC_KEY+ NC_CONFIG_KEY;

    /**
     * NCC走全票种处理接口
     */
    String ALL_INVOICE_INTERFACE = "1";

    /**
     * NCC走增值税票种处理接口
     */
    String TAX_INVOICE_INTERFACE = "0";

    /**
     * NC正常上传发票逻辑标识
     */
    String NORMAL_UPLOAD_INVOICE = "1";

    /**
     * NC手动修改入台账标识
     */
    String MANUAL_UPLOAD_INVOICE = "0";

    /**
     * NC推送台账成功标识
     */
    String PUSH_BUSINESS_INFO_SUCCESS = "1";
    /**
     * NCC全票种发票类型
     */
    List<String> ALL_INVOICE_LIST = ListUtil.toList(InvoiceConstants.TAX_SPECIAL_INVOICE,
            InvoiceConstants.TAX_INVOICE,
            InvoiceConstants.ELECTRONIC_INVOICE,
            InvoiceConstants.ROLL_TICKET,
            InvoiceConstants.ELECTRONIC_OFD_INVOICE,
            InvoiceConstants.MOTOR_VEHICLE_SALE,
            InvoiceConstants.QUOTA_INVOICE,
            InvoiceConstants.AIRCRAFT_INVOICE,
            InvoiceConstants.TAXI_TICKETS,
            InvoiceConstants.RAILWAY_TICKET,
            InvoiceConstants.PASSENGER_TICKET,
            InvoiceConstants.FLIGHT_ITINERARY,
            InvoiceConstants.TOLL_ROADS,
            InvoiceConstants.INVOICE_OTHERS,
            InvoiceConstants.INVOICE_MUCH_NCC
    );

    /**
     * NCC增值税发票类型
     */
    List<String> TAX_INVOICE_LIST = ListUtil.toList(InvoiceConstants.TAX_SPECIAL_INVOICE,
            InvoiceConstants.TAX_INVOICE,
            InvoiceConstants.ELECTRONIC_INVOICE,
            InvoiceConstants.ROLL_TICKET,
            InvoiceConstants.ELECTRONIC_OFD_INVOICE,
            InvoiceConstants.MOTOR_VEHICLE_SALE
    );

}
