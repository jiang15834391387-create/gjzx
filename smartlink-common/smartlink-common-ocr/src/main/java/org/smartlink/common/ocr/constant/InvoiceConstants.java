package org.smartlink.common.ocr.constant;

import java.util.HashMap;

/**
 * OCR通用常量信息
 *
 * @author lqm
 */
public class InvoiceConstants {
    /**
     * 增值税专用发票
     */
    public static final String TAX_SPECIAL_INVOICE = "100";

    /**
     * 增值税普通发票
     */
    public static final String TAX_INVOICE = "101";

    /**
     * 增值税电子普通发票
     */
    public static final String ELECTRONIC_INVOICE = "102";

    /**
     * 增值税普通发票(卷票)
     */
    public static final String ROLL_TICKET = "103";

    /**
     * 增值税电子专用发票
     */
    public static final String ELECTRONIC_OFD_INVOICE = "104";

    /**
     * 增值税电子普通发票(通行费)
     */
    public static final String ELECTRONIC_INVOICE_ITINERARY = "105";

    /**
     * 机动车销售统一发票
     */
    public static final String MOTOR_VEHICLE_SALE = "106";

    /**
     * 二手车销售统一发票
     */
    public static final String USED_CAR_SALES = "107";

    /**
     * 定额发票
     */
    public static final String QUOTA_INVOICE = "108";

    /**
     * 通用机打发票
     */
    public static final String AIRCRAFT_INVOICE = "109";

    /**
     * 出租车发票
     */
    public static final String TAXI_TICKETS = "110";

    /**
     * 火车票
     */
    public static final String RAILWAY_TICKET = "111";

    /**
     * 客运汽车票
     */
    public static final String PASSENGER_TICKET = "112";

    /**
     * 航空运输电子客票行程单
     */
    public static final String FLIGHT_ITINERARY = "113";

    /**
     * 船票
     */
    public static final String STEAMER_TICKET = "114";

    /**
     * 过路费
     */
    public static final String TOLL_ROADS = "115";

    /**
     * 小票
     */
    public static final String RECEIPT = "116";

    /**
     * 滴滴出行行程单
     */
    public static final String DIDI_ITINERARY = "117";

    /**
     * 完税证明
     */
    public static final String DUTY_PAID_PROOF = "118";

    /**
     * 区块链发票
     */
    public static final String ELECTRONIC_INVOICE_QUKUAILIAN = "119";

    /**
     * ocr信息
     */
    public static final String OCR_INFORMATION = "122";

    /**
     * ocr信息明细
     */
    public static final String OCR_INFORMATION_DETAILS = "123";

    /**
     * 多OCR信息发票文件
     */
    public static final String INVOICE_MUCH_NCC = "124";


    /**
     * 其他发票
     */
    public static final String INVOICE_OTHERS = "125";

    /**
     * 承兑汇单
     */
    public static final String ACCEPTANCE_BILL = "126";

    /**
     * 其他
     */
    public static final String IMAGE_OTHERS = "444";

    /**
     * txt文件
     */
    public static final String DOCUMENT_TXT = "555";

    /**
     * pdf文件
     */
    public static final String DOCUMENT_PDF = "666";

    /**
     * word文件
     */
    public static final String DOCUMENT_WORD = "777";

    /**
     * excel文件
     */
    public static final String DOCUMENT_EXCEL = "888";

    /**
     * ppt文件
     */
    public static final String DOCUMENT_PPT = "999";

    /**
     * pres 压缩文件
     */
    public static final String DOCUMENT_PRES = "1000";

    /**
     * OFD普通文件
     */
    public static final String DOCUMENT_OFD = "1111";

    /**
     * tif文件
     */
    public static final String DOCUMENT_TIF = "901";

    /**
     * gif文件
     */
    public static final String DOCUMENT_GIT = "902";

    /**
     * bmp文件
     */
    public static final String DOCUMENT_BMP = "903";

    /**
     * 事后补扫
     */
    public static final String AFTER_FILE = "121";

    /**
     * 友报账文件类型
     */
    public static final String YOUBAOZHANG_DOC = "211";

    /**
     * 友报账图片类型
     */
    public static final String YOUBAOZHANG_IMG = "210";

    /**
     * 发票预览
     */
    public static final String INVOICE_PREVIEW = "212";


    public static final HashMap<String,String> INVOICE_ClASS_TYPE = new HashMap<>();

    static {
        INVOICE_ClASS_TYPE.put(TAX_SPECIAL_INVOICE,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(TAX_INVOICE,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(ELECTRONIC_INVOICE,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(ROLL_TICKET,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(ELECTRONIC_OFD_INVOICE,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(ELECTRONIC_INVOICE_ITINERARY,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(OCR_INFORMATION,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(INVOICE_OTHERS,"com.datafly.common.domain.invoice.DataOcrInfo");
        //区块链
        INVOICE_ClASS_TYPE.put(ELECTRONIC_INVOICE_QUKUAILIAN,"com.datafly.common.domain.invoice.DataOcrInfo");
        INVOICE_ClASS_TYPE.put(MOTOR_VEHICLE_SALE,"com.datafly.common.domain.invoice.DataMotorVehicleSale");
        INVOICE_ClASS_TYPE.put(USED_CAR_SALES,"com.datafly.common.domain.invoice.DataUsedCarSales");
        INVOICE_ClASS_TYPE.put(QUOTA_INVOICE,"com.datafly.common.domain.invoice.DataQuotaInvoice");
        INVOICE_ClASS_TYPE.put(AIRCRAFT_INVOICE,"com.datafly.common.domain.invoice.DataAircraftInvoice");
        INVOICE_ClASS_TYPE.put(TAXI_TICKETS,"com.datafly.common.domain.invoice.DataTaxiTickets");
        INVOICE_ClASS_TYPE.put(RAILWAY_TICKET,"com.datafly.common.domain.invoice.DataRailwayTicket");
        INVOICE_ClASS_TYPE.put(PASSENGER_TICKET,"com.datafly.common.domain.invoice.DataPassengerTicket");
        INVOICE_ClASS_TYPE.put(FLIGHT_ITINERARY,"com.datafly.common.domain.invoice.DataFlightItinerary");
        INVOICE_ClASS_TYPE.put(STEAMER_TICKET,"com.datafly.common.domain.invoice.DataSteamerTicket");
        INVOICE_ClASS_TYPE.put(TOLL_ROADS,"com.datafly.common.domain.invoice.DataTollRoads");
        INVOICE_ClASS_TYPE.put(RECEIPT,"com.datafly.common.domain.invoice.DataReceipt");
        INVOICE_ClASS_TYPE.put(DIDI_ITINERARY,"com.datafly.common.domain.invoice.DataDidiItinerary");
        INVOICE_ClASS_TYPE.put(DUTY_PAID_PROOF,"com.datafly.common.domain.invoice.DataDutyPaidProof");
        INVOICE_ClASS_TYPE.put(ACCEPTANCE_BILL,"com.datafly.common.domain.invoice.DataAcceptanceBill");
    }

}
