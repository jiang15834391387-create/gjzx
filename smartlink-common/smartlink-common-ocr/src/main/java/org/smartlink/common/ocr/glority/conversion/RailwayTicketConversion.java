package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.smartlink.common.entity.domain.business.domain.DataTaxiTickets;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: RailwayTicketConversion</p>
 * <p>
 * <p>Description:票小秘火车票类转换为实体类 </p>
 **/
public class RailwayTicketConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final RailwayTicketConversion INSTANCE = new RailwayTicketConversion();
    }

    private RailwayTicketConversion() {
    }

    public static RailwayTicketConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataRailwayTicket railwayTicket = new DataRailwayTicket();
            if (null == jsonObject) {
                return null;
            }
            railwayTicket.setId(IdUtil.simpleUUID());
            railwayTicket.setFileId(dataImageFilesInfo.getFileId());
            railwayTicket.setInvoiceNumber(jsonObject.getStr("number"));
            railwayTicket.setInvoiceTotal(jsonObject.getStr("total"));
            railwayTicket.setInvoiceTime(jsonObject.getStr("time"));
            railwayTicket.setInvoiceDate(jsonObject.getStr("date"));
            railwayTicket.setTrainNumber(jsonObject.getStr("train_number"));
            railwayTicket.setSerialNumber(jsonObject.getStr("serial_number"));
            railwayTicket.setStationGetOn(jsonObject.getStr("station_geton"));
            railwayTicket.setStationGetOff(jsonObject.getStr("station_getoff"));
            railwayTicket.setName(jsonObject.getStr("name"));
            railwayTicket.setIdNumber(jsonObject.getStr("user_id"));
            railwayTicket.setWicket(jsonObject.getStr("gate_number"));
            railwayTicket.setTicketAddress(jsonObject.getStr("pick_up_address"));
            railwayTicket.setSeatNum(jsonObject.getStr("seat_number"));
            railwayTicket.setElectronicMark(jsonObject.getStr("electronic_mark"));
            railwayTicket.setRefundContent(jsonObject.getStr("refund_content"));
            railwayTicket.setTypeOfBusiness(jsonObject.getStr("type_of_business"));
            railwayTicket.setTicketContent(jsonObject.getStr("ticket_content"));
            railwayTicket.setBuyer(jsonObject.getStr("buyer"));
            railwayTicket.setBuyerTaxId(jsonObject.getStr("buyer_tax_id"));
            railwayTicket.setNumberOfOriginalInvoice(jsonObject.getStr("number_of_original_invoice"));
            railwayTicket.setAirConditioning(jsonObject.getStr("air_conditioning"));
            railwayTicket.setTypeOfVoucher(jsonObject.getStr("type_of_voucher"));
            railwayTicket.setTypeOfRailwayTicket(jsonObject.getStr("type_of_railway_ticket"));
            railwayTicket.setDiscountMark(jsonObject.getStr("discount_mark"));
            railwayTicket.setAmountRefunded(jsonObject.getStr("amount_refunded"));
            railwayTicket.setFareOfOriginalRailwayTicket(jsonObject.getStr("fare_of_original_railway_ticket"));
            railwayTicket.setDepartureStationOfOriginalRailwayTicket(jsonObject.getStr("departure_station_of_original_railway_ticket"));
            railwayTicket.setDestinationStationOfOriginalRailwayTicket(jsonObject.getStr("destination_station_of_original_railway_ticket"));
            railwayTicket.setBuyerAddrTel(jsonObject.getStr("buyer_addr_tel"));
            railwayTicket.setBuyerBankAccount(jsonObject.getStr("buyer_bank_account"));
            railwayTicket.setDateOfIssue(jsonObject.getStr("date_of_issue"));
            railwayTicket.setPhonicsOfDepartureStation(jsonObject.getStr("phonics_of_departure_station"));
            railwayTicket.setPhonicsOfDestinationStation(jsonObject.getStr("phonics_of_destination_station"));
            railwayTicket.setRemark(jsonObject.getStr("remark"));


            railwayTicket.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                railwayTicket.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                railwayTicket.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_RAILWAY_TICKET_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_RAILWAY_TICKET_CODE.getCode(), railwayTicket, identifyResults.getExtra(), identifyResults.getMessage()));

        }
        return resultsList;
    }
}
