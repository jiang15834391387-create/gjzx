package org.smartlink.common.check.enumd;


import org.smartlink.common.check.constant.InvoiceConstants;

/**
 * 睿真的发票类型和系统发票类型对应枚举
 *
 * @author maxuhui
 */
public enum RuiZhenInvoiceTypeEnum {

    /**
     * 增值税专用发票
     */
    TAX_SPECIAL_INVOICE(InvoiceConstants.TAX_SPECIAL_INVOICE, "01"),
    /**
     * 增值税普通发票
     */
    TAX_INVOICE(InvoiceConstants.TAX_INVOICE, "04"),
    /**
     * 电子专票
     */
    TAX_SPECIAL_ELECTRONIC_INVOICE(InvoiceConstants.ELECTRONIC_OFD_INVOICE, "08"),
    /**
     * 电子普票
     */
    ELECTRONIC_INVOICE(InvoiceConstants.ELECTRONIC_INVOICE, "10"),
    /**
     * 卷票
     */
    ROLL_TICKET(InvoiceConstants.ROLL_TICKET, "11"),
    /**
     * 通行费发票
     */
    TOLL_INVOICE(InvoiceConstants.TOLL_ROADS, "14"),
    /**
     * 机动车
     */
    MOTOR_VEHICLE_SALE(InvoiceConstants.MOTOR_VEHICLE_SALE, "03"),
    /**
     * 二手车
     */
    USED_CAR_SALES(InvoiceConstants.USED_CAR_SALES, "15"),
    ;

    private final String ruiZhenType;
    private final String systemType;

    public static String getSystemType(String ruiZhenType) {
        final RuiZhenInvoiceTypeEnum[] values = values();
        for (RuiZhenInvoiceTypeEnum ocrTypeRequestTypeEnum : values) {
            if (ocrTypeRequestTypeEnum.ruiZhenType.equals(ruiZhenType)) {
                return ocrTypeRequestTypeEnum.systemType;
            }
        }
        return null;
    }

    public String getRuiZhenType() {
        return ruiZhenType;
    }

    public String getSystemType() {
        return systemType;
    }

    RuiZhenInvoiceTypeEnum(String systemType, String ruiZhenType) {
        this.ruiZhenType = ruiZhenType;
        this.systemType = systemType;
    }
}
