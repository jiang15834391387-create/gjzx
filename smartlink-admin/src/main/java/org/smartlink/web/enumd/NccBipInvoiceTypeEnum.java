package org.smartlink.web.enumd;


import cn.hutool.core.util.ArrayUtil;
import org.smartlink.web.constant.InvoiceConstants;


public enum NccBipInvoiceTypeEnum {

    //1	 增值税电子普通发票
    //2	 增值税电子专用发票
    //3	 增值税普通发票
    //4	 增值税专用发票
    //5	 机动车销售统一发票
    //6	 货物运输业增值税专用发票
    //8	 增值税电子普通发票(成品油)
    //9	 成品油普通发票(卷式)
    //10 增值税普通发票(成品油)
    //11 增值税专用发票(成品油)
    //12 增值税普通发票(卷式)
    //14 通行费增值税电子普通发票
    //31 增值税专用发票(全电票)

    /**
     * 增值税专用发票
     */
    TAX_SPECIAL_INVOICE(InvoiceConstants.TAX_SPECIAL_INVOICE, new String[]{"4", "6","11","33"}),
    /**
     * 增值税普通发票
     */
    TAX_INVOICE(InvoiceConstants.TAX_INVOICE, new String[]{"3", "9","10","12","34"}),
    /**
     * 电子专票
     */
    TAX_SPECIAL_ELECTRONIC_INVOICE(InvoiceConstants.ELECTRONIC_OFD_INVOICE, new String[]{"2"}),
    /**
     * 全电专票
     */
    ELECTRIC_SPECIAL_TICKET(InvoiceConstants.INVOICE_ELECTRIC_SPECIAL_TICKET_NCC, new String[]{"31"}),
    /**
     * 电子普票
     */
    ELECTRONIC_INVOICE(InvoiceConstants.ELECTRONIC_INVOICE, new String[]{"1", "8","14"}),
    /**
     * 全电普票
     */
    ELECTRIC_UNIVERSAL_TICKET(InvoiceConstants.INVOICE_ELECTRIC_UNIVERSAL_TICKET_NCC, new String[]{"32"}),
    /**
     * 机动车
     */
    MOTOR_VEHICLE_SALE(InvoiceConstants.MOTOR_VEHICLE_SALE, new String[]{"5"}),
    /**
     * 二手车
     */
    USED_CAR_SALES(InvoiceConstants.USED_CAR_SALES, new String[]{"15"}),
    ;

    private final String[] bipInvoiceType;
    private final String systemType;

    public static String getSystemType(String nccBipType) {
        final NccBipInvoiceTypeEnum[] values = values();
        for (NccBipInvoiceTypeEnum ocrTypeRequestTypeEnum : values) {
            if (ArrayUtil.containsIgnoreCase(ocrTypeRequestTypeEnum.bipInvoiceType,nccBipType)) {
                return ocrTypeRequestTypeEnum.systemType;
            }
        }
        return null;
    }

    public String[] getBipInvoiceType() {
        return bipInvoiceType;
    }

    public String getSystemType() {
        return systemType;
    }

    NccBipInvoiceTypeEnum(String systemType, String[] hangXinType) {
        this.bipInvoiceType = hangXinType;
        this.systemType = systemType;
    }
}
