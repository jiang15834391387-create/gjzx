package org.smartlink.server.nc.constant;

/**
 * NCC查验返回发票类型常量类
 *
 * @author L
 * @date
 */
public interface NcCheckInvoiceTypeConstant {
    /**
     * 增值税电子普通发票
     */
    String NCC_VAT_ELECTRONIC_INVOICE = "1";

    /**
     * 增值税电子专用发票
     */
    String NCC_VAT_ELECTRONIC_SPECIAL_INVOICE = "2";

    /**
     * 增值税普通发票
     * @return
     */
    String NCC_VAT_INVOICE = "3";

    /**
     * 增值税专用发票
     */
    String NCC_VAT_SPECIAL_INVOICE = "4";

    /**
     * 机动车销售统一发票
     */
    String NCC_MOTOR_VEHICLE_SALES = "5";

    /**
     * 货物运输业增值税专用发票
     */
    String NCC_VAT_SPECIAL_INVOICE_CAR_TRANSPORTATION = "6";

    /**
     * 增值税电子普通发票(成品油)
     */
    String NCC_VAT_ELECTRONIC_INVOICE_REFINED_OIL = "8";

    /**
     * 成品油普通发票(卷式)
     */
    String NCC_VAT__INVOICE_REFINED_OIL_ROLL = "9";

    /**
     * 增值税普通发票(成品油)
     */
    String NCC_VAT_INVOICE_OIL_ROLL = "10";

    /**
     * 增值税专用发票(成品油)
     */
    String NCC_VAT_SPECIAL_INVOICE_OIL = "11";

    /**
     * 增值税普通发票(卷式)
     */
    String NCC_VAT_INVOICE_ROLL = "12";

    /**
     * 通行费增值税电子普通发票
     */
    String NCC_TOLL_VAT_ELECTRONIC_INVOICE = "14";

    /**
     * 全电增值税电子普通发票
     */
    String NEW_NCC_VAT_ELECTRONIC_INVOICE = "32";

    /**
     * 全电增值税电子专用发票
     */
    String NEW_NCC_VAT_ELECTRONIC_SPECIAL_INVOICE = "31";

    /**
     * 纸质版本全电增值税电子专用发票
     */
    String NEW_TRANSI_NCC_VAT_ELECTRONIC_SPECIAL_INVOICE = "33";

    /**
     * 纸质版本全电增值税电子普通发票
     */
    String NEW_TRANSI_NCC_VAT_ELECTRONIC_INVOICE = "34";

}
