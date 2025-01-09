package org.smartlink.common.check.constant;

import java.util.HashMap;

/**
 * OCR通用常量信息
 *
 * @author 史敦凯Constants
 */
public class InvoiceConstants {
    /**
     * 增值税专用发票
     */
   public static final String GLORITY_TAX_SPECIAL_INVOICE="10100";
    /**
     * 增值税普通发票
     */
    public static final String GLORITY_TAX_INVOICE="10101";
    /**
     * 增值税电子普通发票
     */
    public static final String GLORITY_ELECTRONIC_INVOICE="10102";
    /**
     * 增值税普通发票(卷票)
     */
    public static final String GLORITY_ROLL_TICKET_CODE="10103";
    /**
     * 机动车销售统一发票
     */
    public static final String GLORITY_MOTOR_VEHICLE_SALE_CODE="10104";
    /**
     * 二手车销售统一发票
     */
    public static final String GLORITY_USED_CAR_SALES_CODE ="10105";
    /**
     * 船票
     */
    public static final String GLORITY_STEAMER_TICKET_CODE="10505a" ;
    /**
     * 非税收入类票据
     */
    public static final String  NON_TAX_REVENUE_RECEIPTS_CODE="102015";
    /**
     * 海关进口货物报关单
     */
    public static final String  CUSTOMS_IMPORTED_GOODS_CODE="102018";
    /**
     * 医疗票明细
     */
    public static final String MEDICAL_TICKET_DETAILS_CODE="103015";
    /**
     * 定额发票
     */
    public static final String GLORITY_QUOTA_INVOICE_CODE="10200";
    /**
     * 机打发票
     */
    public static final String  GLORITY_AIRCRAFT_INVOICE_CODE="10400";
    /**
     * 出租车发票
     */
    public static final String  GLORITY_TAXI_TICKETS_CODE="10500";
    /**
     * 火车票
     */
    public static final String  GLORITY_RAILWAY_TICKET_CODE="10503";
    /**
     * 客运汽车票
     */
    public static final String GLORITY_PASSENGER_TICKET_CODE="10505";
    /**
     * 航空运输电子客票行程单
     */
    public static final String GLORITY_FLIGHT_ITINERARY_CODE="10506";
    /**
     * 数电票(增值税专用发票)
     */
    public static final String DIGITAL_INVOICE_VAT_SPECIAL_CODE="10107";
    /**
     * 海关专用缴款书
     */
    public static final String  CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE="102020";
    /**
     * 海关出口货物报关单
     */
    public static final String CUSTOMS_EXPORT_GOODS_CODE="102017";
    /**
     * 货物运输电子收款凭证
     */
    public static final String ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE="102085";
    /**
     * 过路费发票
     */
    public static final String  GLORITY_TOLL_ROADS_CODE="10507";
    /**
     * 可报销其他发票
     */
    public static final String  REIMBURSABLE_OTHER_CODE="10900";
    /**
     * 船票
     */
    public static final String  GLORITY_RECEIPT_CODE="20100";
    /**
     * 出行行程单
     */
    public static final String GLORITY_DIDI_ITINERARY_CODE="20105";
    /**
     * 完税证明
     */
    public static final String  GLORITY_DUTY_PAID_PROOF_CODE="10902";
    /**
     * 数电票(普通发票)
     */
    public static final String  DIGITAL_INVOICE_ORDINARY_INVOICE_CODE="10108";
    /**
     * 医疗票据
     */
    public static final String MEDICAL_RECEIPTS_CODE="10505a";
    /**
     * 增值税发票清单
     */
    public static final String  DIGITAL_INVOICE_LIST="10110";

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


}
