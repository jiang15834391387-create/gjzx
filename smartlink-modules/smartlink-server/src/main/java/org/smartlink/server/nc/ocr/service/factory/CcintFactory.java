package org.smartlink.server.nc.ocr.service.factory;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.IdentificationFactory;
import org.smartlink.server.nc.ocr.service.ccint.conversion.AcceptanceBillConversion;
import org.smartlink.server.nc.ocr.service.ccint.conversion.CcintOthersConversion;
import org.smartlink.server.nc.ocr.service.ccint.conversion.RollTicketConversion;
import org.smartlink.server.nc.ocr.service.ccint.response.ObjectListBean;
import org.smartlink.server.nc.ocr.service.glority.conversion.DidiItineraryConversion;
import org.smartlink.server.nc.ocr.service.glority.conversion.MotorVehicleSaleConversion;
import org.smartlink.server.nc.ocr.service.glority.conversion.UsedCarSalesConversion;
import org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp.*;

/**
 * @description: 合合工厂转换
 * @author: L
 * @create:
 **/
public class CcintFactory implements IdentificationFactory<ObjectListBean> {
    /**
     * 区块链电子发票
     */
    private final static String CCINT_TYPE_BLOCKCHAIN_ELECTRONIC_INVOICE="blockchain_electronic_invoice";
    /**
     * 增值税电子专票
     */
    private final static String CCINT_TYPE_VAT_ELECTRONIC_SPECIAL_INVOICE="vat_electronic_special_invoice";
    /**
     * 电子发票(普通发票)
     */
    private final static String CCINT_TYPE_VAT_ELECTRONIC_INVOICE_NEW="vat_electronic_invoice_new";
    /**
     * 电子发票(增值税专用发票)
     */
    private final static String CCINT_TYPE_VAT_ELECTRONIC_SPECIAL_INVOICE_NEW="vat_electronic_special_invoice_new";
    /**
     * 机打发票
     */
    private final static String CCINT_TYPE_MACHINE_PRINTED_INVOICE="machine_printed_invoice";
    /**
     * 通用机打发票
     */
    private final static String CCINT_TYPE_GENERAL_MACHINE_INVOICE="general_machine_invoice";
    /**
     * 增值税专用发票
     */
    private final static String CCINT_TYPE_VAT_SPECIAL_INVOICE="vat_special_invoice";
    /**
     * 机动车销售统一发票
     */
    private final static String CCINT_TYPE_MOTOR_VEHICLE_SALE_INVOICE="motor_vehicle_sale_invoice";
    /**
     * 货物运输业增值税专用发票
     */
    private final static String CCINT_TYPE_VAT_TRANSPORT_INVOICE="vat_transport_invoice";
    /**
     * 增值税普通发票
     */
    private final static String CCINT_TYPE_VAT_COMMON_INVOICE="vat_common_invoice";
    /**
     * 增值税电子普通发票
     */
    private final static String CCINT_TYPE_VAT_ELECTRONIC_INVOICE="vat_electronic_invoice";
    /**
     * 增值税普通发票（卷票）
     */
    private final static String CCINT_TYPE_VAT_ROLL_INVOICE="vat_roll_invoice";
    /**
     * 增值税电子普通发票（通行费）
     */
    private final static String CCINT_TYPE_VAT_ELECTRONIC_TOLL_INVOICE="vat_electronic_toll_invoice";
    /**
     * 二手车销售统一发票
     */
    private final static String CCINT_TYPE_USED_CAR_PURCHASE_INVOICE="used_car_purchase_invoice";
    /**
     * 通用定额发票
     */
    private final static String CCINT_TYPE_QUOTA_INVOICE="quota_invoice";
    /**
     * 旅客运输普票
     */
    private final static String CCINT_TYPE_PASSENGER_TRANSPORT_INVOICE="passenger_transport_invoice";
    /**
     * 公路客运发票
     */
    private final static String CCINT_TYPE_HIGHWAY_PASSENGER_INVOICE="highway_passenger_invoice";
    /**
     * 船运客票
     */
    private final static String CCINT_TYPE_SHIPPING_INVOICE="shipping_invoice";
    /**
     * 出租车发票
     */
    private final static String CCINT_TYPE_TAXI_TICKET="taxi_ticket";
    /**
     * 停车费发票
     */
    private final static String CCINT_TYPE_PARKING_INVOICE="parking_invoice";
    /**
     * 过路过桥费发票、汽车通行费
     */
    private final static String CCINT_TYPE_VEHICLE_TOLL="vehicle_toll";
    /**
     * 医疗费收据
     */
    private final static String CCINT_TYPE_MEDICAL_RECEIPT="medical_receipt";
    /**
     * 教育费收据
     */
    private final static String CCINT_TYPE_EDUCATION_RECEIPT="education_receipt";
    /**
     * 行程单
     */
    private final static String CCINT_TYPE_AIR_TRANSPORT="air_transport";
    /**
     * 火车票
     */
    private final static String CCINT_TYPE_TRAIN_TICKET="train_ticket";
    /**
     * 其它类型
     */
    private final static String CCINT_TYPE_OTHER="other";
    /**
     * 滴滴行程单
     */
    private final static String CCINT_TYPE_DIDI_ITINERARY="didi_itinerary";
    /**
     *     承兑汇单
     */
    private final static String  ACCEPTANCE_BILL_CODE ="money_order";
    /**
     *非税收入统一票据
     * */
    private final String FINANCE = "finance";

    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, ObjectListBean objectListBean)  {
        //识别类型
        String classX = objectListBean.getType();
        switch (classX){
            case CCINT_TYPE_VAT_ROLL_INVOICE:
                return RollTicketConversion.getInstance();
            case CCINT_TYPE_VAT_SPECIAL_INVOICE:
            case CCINT_TYPE_VAT_COMMON_INVOICE:
            case CCINT_TYPE_VAT_ELECTRONIC_INVOICE:
            case CCINT_TYPE_VAT_ELECTRONIC_TOLL_INVOICE:
            case CCINT_TYPE_BLOCKCHAIN_ELECTRONIC_INVOICE:
            case CCINT_TYPE_VAT_ELECTRONIC_INVOICE_NEW:
            case CCINT_TYPE_VAT_ELECTRONIC_SPECIAL_INVOICE_NEW:
            case CCINT_TYPE_VAT_ELECTRONIC_SPECIAL_INVOICE:
            case FINANCE:
                return InvoiceConversion.getInstance();
            case CCINT_TYPE_MOTOR_VEHICLE_SALE_INVOICE:
                return MotorVehicleSaleConversion.getInstance();
             /*case CCINT_TYPE_VAT_TRANSPORT_INVOICE:
                return "货物运输业增值税专用发票";*/

                //return RollTicketConversion.getInstance();
            case CCINT_TYPE_USED_CAR_PURCHASE_INVOICE:
                return UsedCarSalesConversion.getInstance();
            case CCINT_TYPE_GENERAL_MACHINE_INVOICE:
            case CCINT_TYPE_MACHINE_PRINTED_INVOICE:
                return AircraftInvoiceConversion.getInstance();
            case CCINT_TYPE_QUOTA_INVOICE:
            case CCINT_TYPE_PARKING_INVOICE:
                return QuotaInvoiceConversion.getInstance();
            case CCINT_TYPE_PASSENGER_TRANSPORT_INVOICE:
            case CCINT_TYPE_HIGHWAY_PASSENGER_INVOICE:
                return PassengerTicketConversion.getInstance();
            case CCINT_TYPE_SHIPPING_INVOICE:
            case CCINT_TYPE_VEHICLE_TOLL:
                return TollRoadsConversion.getInstance();
            case CCINT_TYPE_TAXI_TICKET:
                return TaxiTicketsConversion.getInstance();
            case CCINT_TYPE_AIR_TRANSPORT:
                return FlightItineraryConversion.getInstance();
            case CCINT_TYPE_TRAIN_TICKET:
                return RailwayTicketConversion.getInstance();
            case CCINT_TYPE_DIDI_ITINERARY:
                return DidiItineraryConversion.getInstance();
            case ACCEPTANCE_BILL_CODE:
                return AcceptanceBillConversion.getInstance();
            default:
                return CcintOthersConversion.getInstance();
        }
    }
}
