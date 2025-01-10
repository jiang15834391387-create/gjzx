package org.smartlink.common.ocr.factory;

import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.core.IdentificationFactory;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.conversion.InvoiceConversion;
import org.smartlink.common.ocr.glority.response.IdentifyResults;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

/**
 * 票小蜜识别工厂
 *
 */
public class GlorityFactory implements IdentificationFactory<IdentifyResults> {

    /**
     * 增值税专用发票
     */
    private final static String GLORITY_TAX_SPECIAL_INVOICE = "10100";

    /**
     * 增值税普通发票
     */
    private final static String GLORITY_TAX_INVOICE = "10101";

    /**
     * 增值税电子普通发票
     */
    private final static String GLORITY_ELECTRONIC_INVOICE = "10102";

    /**
     * 定额发票
     */
    private final static String GLORITY_QUOTA_INVOICE_CODE = "10200";

    /**
     * 机打发票
     */
    private final static String GLORITY_AIRCRAFT_INVOICE_CODE = "10400";

    /**
     * 出租车发票
     */
    private final static String GLORITY_TAXI_TICKETS_CODE = "10500";

    /**
     * 增值税普通发票(卷票)
     */
    private final static String GLORITY_ROLL_TICKET_CODE = "10103";

    /**
     * 火车票
     */
    private final static String GLORITY_RAILWAY_TICKET_CODE = "10503";

    /**
     * 国际小票
     */
    private final static String GLORITY_RECEIPT_CODE = "20100";

    /**
     * 机动车销售统一发票
     */
    private final static String GLORITY_MOTOR_VEHICLE_SALE_CODE = "10104";

    /**
     * 客运汽车
     */
    private final static String GLORITY_PASSENGER_TICKET_CODE = "10505";

    /**
     * 滴滴出行行程单
     */
    private final static String GLORITY_DIDI_ITINERARY_CODE = "20105";

    /**
     * 航空运输电子客票行程单
     */
    private final static String GLORITY_FLIGHT_ITINERARY_CODE = "10506";

    /**
     * 过路费发票
     */
    private final static String GLORITY_TOLL_ROADS_CODE = "10507";

    /**
     * 二手车销售统一发票
     */
    private final static String GLORITY_USED_CAR_SALES_CODE = "10105";

    /**
     * 完税证明
     */
    private final static String GLORITY_DUTY_PAID_PROOF_CODE = "10902";

    /**
     * 非税收入类票据
     */
    private final static String NON_TAX_REVENUE_RECEIPTS_CODE = "102015";

    /**
     * 船票
     */
    private final static String GLORITY_STEAMER_TICKET_CODE = "10505a";

    /**
     * 海关进口货物报关单
     */
    private final static String CUSTOMS_IMPORTED_GOODS_CODE = "102018";

    // 2022新版电子普通发票
    private final static String GLORITY_TAX_ELECTRONIC_NO_INVOICE_CODE_INVOICE = "10108";
    // 2022新版电子专用发票
    private final static String GLORITY_SPECIAL_ELECTRONIC_NO_INVOICE_CODE_INVOICE = "10107";

    //可报销其他发票

    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {
        switch (identifyResults.getType()) {
            case GLORITY_TAX_SPECIAL_INVOICE:
            case GLORITY_TAX_INVOICE:
            case GLORITY_ELECTRONIC_INVOICE:
            case GLORITY_ROLL_TICKET_CODE:
            case GLORITY_TAX_ELECTRONIC_NO_INVOICE_CODE_INVOICE:
            case GLORITY_SPECIAL_ELECTRONIC_NO_INVOICE_CODE_INVOICE:
                return InvoiceConversion.getInstance();
//            case GLORITY_QUOTA_INVOICE_CODE:
//                return QuotaInvoiceConversion.getInstance();
//            case GLORITY_AIRCRAFT_INVOICE_CODE:
//                return AircraftInvoiceConversion.getInstance();
//            case GLORITY_TAXI_TICKETS_CODE:
//                return TaxiTicketsConversion.getInstance();
//            case GLORITY_RAILWAY_TICKET_CODE:
//                return RailwayTicketConversion.getInstance();
//            case GLORITY_RECEIPT_CODE:
//                return ReceiptConversion.getInstance();
//            case GLORITY_MOTOR_VEHICLE_SALE_CODE:
//                return MotorVehicleSaleConversion.getInstance();
//            case GLORITY_PASSENGER_TICKET_CODE:
//                return PassengerTicketConversion.getInstance();
//            case GLORITY_DIDI_ITINERARY_CODE:
//                return DidiItineraryConversion.getInstance();
//            case GLORITY_USED_CAR_SALES_CODE:
//                return UsedCarSalesConversion.getInstance();
//            case GLORITY_FLIGHT_ITINERARY_CODE:
//                return FlightItineraryConversion.getInstance();
//            case GLORITY_DUTY_PAID_PROOF_CODE:
//                return DutyPaidProofConversion.getInstance();
//            case GLORITY_STEAMER_TICKET_CODE:
//                return SteamerTicketConversion.getInstance();
//            case GLORITY_TOLL_ROADS_CODE:
//                return TollRoadsConversion.getInstance();
            default: {
                return null;
            }
        }
    }
}
