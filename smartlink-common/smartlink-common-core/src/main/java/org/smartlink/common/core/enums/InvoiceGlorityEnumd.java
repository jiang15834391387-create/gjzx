package org.smartlink.common.core.enums;


import lombok.Getter;

/**
 * 票小秘枚举
 *
 */
@Getter
public enum InvoiceGlorityEnumd {


    /**
     * 增值税专用发票
     */
    GLORITY_TAX_SPECIAL_CODE("10100","增值税专用发票"),
    /**
     * 增值税电子专用发票
     */
    GLORITY_ELECTRON_TAX_SPECIAL_CODE("10100_1","增值税电子专用发票"),
    /**
     * 增值税普通发票
     */
    GLORITY_TAX_CODE("10101","增值税普通发票"),
    /**
     * 增值税电子普通发票
     */
    GLORITY_ELECTRONIC_CODE("10102","增值税电子普通发票"),
    /**
     * 区块链电子发票
     */
    GLORITY_ELECTRONIC_QUKUAILIAN_CODE("10102_1","区块链电子发票"),
    /**
     * 收费公路通行费增值税电子普通发票
     */
    GLORITY_ELECTRONIC_ROAD_TOLLS_CODE("10102_2","收费公路通行费增值税电子普通发票"),
    /**
     * 增值税普通发票(卷票)
     */
    GLORITY_ROLL_TICKET_CODE("10103","增值税普通发票(卷票)"),
    /**
     * 机动车销售统一发票
     */
    GLORITY_MOTOR_VEHICLE_SALE_CODE("10104","机动车销售统一发票"),
    /**
     * 二手车销售统一发票
     */
    GLORITY_USED_CAR_SALES_CODE("10105","二手车销售统一发票"),
    /**
     * 船票
     */
    GLORITY_STEAMER_TICKET_CODE("10505a","船票"),
    /**
     * 非税收入类票据
     */
    NON_TAX_REVENUE_RECEIPTS_CODE("102015","非税收入类票据"),
    /**
     * 海关进口货物报关单
     */
    CUSTOMS_IMPORTED_GOODS_CODE("102018","海关进口货物报关单"),
    /**
     * 医疗票明细
     */
    MEDICAL_TICKET_DETAILS_CODE("103015","医疗票明细"),
    /**
     * 定额发票
     */
    GLORITY_QUOTA_INVOICE_CODE("10200","定额发票"),
    /**
     * 机打发票
     */
    GLORITY_AIRCRAFT_INVOICE_CODE("10400","机打发票"),
    /**
     * 出租车发票
     */
    GLORITY_TAXI_TICKETS_CODE("10500","出租车发票"),
    /**
     * 火车票
     */
    GLORITY_RAILWAY_TICKET_CODE("10503","火车票"),
    /**
     * 客运汽车票
     */
    GLORITY_PASSENGER_TICKET_CODE("10505","客运汽车票"),
    /**
     * 航空运输电子客票行程单
     */
    GLORITY_FLIGHT_ITINERARY_CODE("10506","航空运输电子客票行程单"),
    /**
     * 数电票(增值税专用发票)
     */
    DIGITAL_INVOICE_VAT_SPECIAL_CODE("10107","数电票(增值税专用发票)"),
    /**
     * 海关专用缴款书
     */
    CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE("102020","海关专用缴款书"),
    /**
     * 海关出口货物报关单
     */
    CUSTOMS_EXPORT_GOODS_CODE("102017","海关出口货物报关单"),
    /**
     * 货物运输电子收款凭证
     */
    ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE("102085","货物运输电子收款凭证"),
    /**
     * 过路费发票
     */
    GLORITY_TOLL_ROADS_CODE("10507","过路费发票"),
    /**
     * 可报销其他发票
     */
    REIMBURSABLE_OTHER_CODE("10900","可报销其他发票"),
    /**
     * 小票
     */
    GLORITY_RECEIPT_CODE("20100","小票"),
    /**
     * 出行行程单
     */
    GLORITY_DIDI_ITINERARY_CODE("20105","出行行程单"),
    /**
     * 完税证明
     */
    GLORITY_DUTY_PAID_PROOF_CODE("10902","完税证明"),
    /**
     * 数电票(普通发票)
     */
    DIGITAL_INVOICE_ORDINARY_INVOICE_CODE("10108","数电票(普通发票)"),
    /**
     * 医疗票据
     */
    MEDICAL_RECEIPTS_CODE("102021","医疗票据"),
    /**
     * 增值税发票清单
     */
    DIGITAL_INVOICE_LIST("10110","增值税发票清单"),
//    /**
//     * 通用手工发票
//     */
//    GLORITY_MANUAL_INVOICE_CODE("10903","通用手工发票"),
    ;

    InvoiceGlorityEnumd(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    private String code;
    private String desc;

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public static InvoiceGlorityEnumd getByCode(String code){
        InvoiceGlorityEnumd[] values = InvoiceGlorityEnumd.values();
        for (InvoiceGlorityEnumd typeEnum:values) {
            if(typeEnum.code.equals(code)){
                return typeEnum;
            }
        }
        return null;
    }

}
