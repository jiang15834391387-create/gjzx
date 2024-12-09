package org.smartlink.server.nc.enumd;

import cn.hutool.core.util.ArrayUtil;
import org.smartlink.server.nc.constant.InvoiceConstants;


/**
 * @author L
 */
public enum NccBipInvoiceAllTypeEnum {

    //    INVOICE   增值税发票
    //    MACHINE   机打发票
    //    TRAIN     火车票
    //    AIR       航空客票
    //    PASSENGER 客运汽车票
    //    TAXI      出租车票
    //    TOLLS     过路费票
    //    QUOTA     定额发票
    //    OTHER     其他发票


    /**
     * 增值税发票
     */
    INVOICE("invoice", new String[]{InvoiceConstants.TAX_SPECIAL_INVOICE,InvoiceConstants.TAX_INVOICE,InvoiceConstants.ELECTRONIC_INVOICE,InvoiceConstants.ROLL_TICKET,InvoiceConstants.ELECTRONIC_OFD_INVOICE,InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY,InvoiceConstants.MOTOR_VEHICLE_SALE}),
    /**
     * 机打发票
     */
    MACHINE("machine", new String[]{InvoiceConstants.AIRCRAFT_INVOICE}),
    /**
     * 火车票
     */
    TRAIN("train", new String[]{InvoiceConstants.RAILWAY_TICKET}),
    /**
     * 航空客票
     */
    AIR("air", new String[]{InvoiceConstants.FLIGHT_ITINERARY}),
    /**
     * 客运汽车票
     */
    PASSENGER("passenger", new String[]{InvoiceConstants.PASSENGER_TICKET}),
    /**
     * 出租车票
     */
    TAXI("taxi",new String[]{InvoiceConstants.TAXI_TICKETS}),
    /**
     * 过路费票
     */
    TOLLS("tolls", new String[]{InvoiceConstants.TOLL_ROADS}),
    /**
     * 定额发票
     */
    QUOTA("quota", new String[]{InvoiceConstants.QUOTA_INVOICE}),
    /**
     * 海关进口增值税专用缴款书
     */
    CUSTOMS("custbook", new String[]{InvoiceConstants.CUSTOMS}),
    /**
     * 其他发票
     */
    OTHER("other", new String[]{InvoiceConstants.IMAGE_OTHERS,InvoiceConstants.DOCUMENT_TXT,InvoiceConstants.DOCUMENT_PDF,InvoiceConstants.DOCUMENT_WORD,InvoiceConstants.DOCUMENT_EXCEL,InvoiceConstants.DOCUMENT_PPT,InvoiceConstants.DOCUMENT_PRES,InvoiceConstants.DOCUMENT_OFD,InvoiceConstants.DOCUMENT_TIF,InvoiceConstants.DOCUMENT_GIT,InvoiceConstants.DOCUMENT_BMP});

    private final String bipInvoiceType;
    private final String[] sysType;


    public static String getSysType(String sysType) {
        final NccBipInvoiceAllTypeEnum[] values = values();
        for (NccBipInvoiceAllTypeEnum nccBipInvoiceAllTypeEnum : values) {
            if (ArrayUtil.containsIgnoreCase(nccBipInvoiceAllTypeEnum.sysType,sysType)) {
                return nccBipInvoiceAllTypeEnum.bipInvoiceType;
            }
        }
        return null;
    }

    public String getBipInvoiceType() {
        return bipInvoiceType;
    }

    public String[] getSysType() {
        return sysType;
    }

    NccBipInvoiceAllTypeEnum(String bipInvoiceType, String[] sysType) {
        this.bipInvoiceType=bipInvoiceType;
        this.sysType=sysType;
    }
}
