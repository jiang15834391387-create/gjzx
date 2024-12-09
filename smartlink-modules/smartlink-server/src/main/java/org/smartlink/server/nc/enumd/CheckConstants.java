package org.smartlink.server.nc.enumd;


/**
 * OCR通用常量信息
 *
 * @author L
 */
public class CheckConstants {
    /**
     * 工厂所在位置
     */
    public static final String FACTORY_PATH = "com.datafly.dataflinvoice.factory.";
    /**
     * 工厂配置名称前缀
     */
    public static final String POMSET_PATH = "ocr.ocr—platform";
    /**
     * 航信增值税专用发票
     */
    public static final String HANG_XIN_TAX_SPECIAL_INVOICE = "01";
    /**
     * 航信机动车销售统一发票
     */
    public static final String HANG_XIN_MOTOR_VEHICLE_SALE = "03";

    /**
     * 航信增值税普通发票
     */
    public static final String HANG_XIN_TAX_INVOICE = "04";

    /**
     * 航信增值税专用发票(电子)
     */
    public static final String HANG_XIN_TAX_SPECIAL_ELECTRONIC_INVOICE = "08";

    /**
     * 航信增值税普通发票(电子)
     */
    public static final String HANG_XIN_ELECTRONIC_INVOICE = "10";
    /**
     * 航信增值税普通发票(卷式)
     */
    public static final String HANG_XIN_ROLL_TICKET = "11";
    /**
     * 航信通行费发票
     */
    public static final String HANG_XIN_TOLL_INVOICE = "14";
    /**
     * 航信二手车发票
     */
    public static final String HANG_XIN_USED_CAR_SALES = "15";
    /**
     * 已查验
     */
    public static final String INVOKE_CHECKED_CODE = "1";

    /**
     * 航信返回失败的code
     */
    public static final String HANG_XIN_ERROR_RESULT_CODE = "0001";
    /**
     * 航信返回失败的code
     */
    public static final String HANG_XIN_ERROR_RETURN_CODE = "0000";

    /**
     * NC65成功的状态码
     */
    public final static String YESFP_NC_SIX_FIVE_ERROR_RESULT_CODE = "0000";

}
