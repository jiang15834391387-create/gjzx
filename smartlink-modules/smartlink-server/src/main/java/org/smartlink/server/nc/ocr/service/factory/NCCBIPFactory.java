package org.smartlink.server.nc.ocr.service.factory;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.IdentificationFactory;
import org.smartlink.server.nc.ocr.service.nccbip.conversion.CustomsInvoiceConversion;
import org.smartlink.server.nc.ocr.service.nccbip.conversion.NccBipOthersConversion;
import org.smartlink.server.nc.ocr.service.nccbip.conversion.ReceiptConversion;
import org.smartlink.server.nc.ocr.service.nccbip.response.NccBipOcrResponse;
import org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp.*;

/**
 * @program: YuYing
 * @description: 税务云工厂转换
 * @author: L
 * @create:
 **/
public class NCCBIPFactory implements IdentificationFactory<NccBipOcrResponse.OneDataDTO.DatasDTO> {

    /**
     * 1    增值税发票
     */
    private final static String  YESFP_INVOICE ="invoice";
    /**
     * 6	过路费
     */
    private final static String  YESFP_TOLL_ROADS_CODE ="tolls";

    /**
     * 4	机打发票
     */
    private final static String YESFP_AIRCRAFT_INVOICE_CODE="machine";

    /**
     * 8	航空电子行程单
     */
    private final static String YESFP__FLIGHT_ITINERARY_CODE="air";

    /**
     * 3	火车票
     */
    private final static String  YESFP_RAILWAY_TICKET_CODE ="train";

    /**
     * 2	出租车发票
     */
    private final static String  YESFP_TAXI_TICKETS_CODE ="taxi";

    /**
     * 5	定额发票
     */
    private final static String   YESFP_QUOTA_INVOICE_CODE="quota";


    /**
     * 7	客运发票
     */
    private final static String  YESFP_PASSENGER_TICKET_CODE ="passenger";

    /**
     * 12	财政非税票据	票据，非发票，发票场景不需考虑
     */
    private final static String  YESFP_NONTAX_CODE ="nontax";

    /**
     * 	海关进口增值税专用缴款书
     */
    private final static String  YESFP_CUSTBOOK_CODE ="custbook";

    /**
     * 9    其他发票类似小票
     */
    private final static String  YESFP_OTHER_CODE ="other";


    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, NccBipOcrResponse.OneDataDTO.DatasDTO datasDTO)  {
        switch (datasDTO.getBillType()) {

            case YESFP_INVOICE:

                return InvoiceConversion.getInstance();

            case YESFP_CUSTBOOK_CODE:

                return CustomsInvoiceConversion.getInstance();

            case YESFP_AIRCRAFT_INVOICE_CODE:

                return AircraftInvoiceConversion.getInstance();

            case YESFP__FLIGHT_ITINERARY_CODE:

                return FlightItineraryConversion.getInstance();

            case YESFP_RAILWAY_TICKET_CODE:

                return RailwayTicketConversion.getInstance();

            case YESFP_TAXI_TICKETS_CODE:

                return TaxiTicketsConversion.getInstance();

            case YESFP_QUOTA_INVOICE_CODE:

                return QuotaInvoiceConversion.getInstance();

            case YESFP_TOLL_ROADS_CODE:

                return TollRoadsConversion.getInstance();

            case YESFP_PASSENGER_TICKET_CODE:

                return PassengerTicketConversion.getInstance();

            case YESFP_OTHER_CODE:

                return ReceiptConversion.getInstance();

            default:

                return NccBipOthersConversion.getInstance();
        }

    }

}
