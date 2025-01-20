package org.smartlink.business.invoice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.*;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class CheckServiceImpl implements ICheckService {
    private final DataImageFilesInfoMapper filesInfoMapper;
    private final DataMotorVehicleSaleMapper motorVehicleSaleMapper;
    private final DataUsedCarSalesMapper usedCarSalesMapper;
    private final DataFlightItineraryMapper flightItineraryMapper;
    private final DataFlightsItineraryDetailMapper flightsItineraryDetailMapper;
    private final DataSteamerTicketMapper steamerTicketMapper;
    private final DataMedicalTreatmentMapper dataMedicalTreatmentMapper;
    private final DataMedicalTreatmentDetailMapper dataMedicalTreatmentDetailMapper;
    private final DataQuotaInvoiceMapper quotaInvoiceMapper;
    private final DataTaxiTicketsMapper taxiTicketsMapper;
    private final DataRailwayTicketMapper railwayTicketMapper;
    private final DataPassengerCarMapper passengerCarMapper;
    private final DataTollRoadsMapper tollRoadsMapper;
    private final DataReceiptMapper dataReceiptMapper;
    private final DataDidiItineraryMapper didiItineraryMapper;
    private final DataDidiItineraryDetailsMapper didiItineraryDetailsMapper;
    private final DataDutyPaidProofMapper paidProofMapper;
    private final DataNonTaxMapper nonTaxMapper;
    private final DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper;
    private final DataCustomsImxportGoodsMapper customsImportGoodsMapper;
    private final DataCustomsExportGoodsMapper customsExportGoodsMapper;
    private final DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper;
    private final DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper;
    private final DataElectronicTransportationGoodsMapper paymentMapper;
    private final DataOcrInfoMapper ocrInfoMapper;
    private final DataOcrDetailsMapper ocrDetailsMapper;
    public CheckServiceImpl(DataImageFilesInfoMapper filesInfoMapper, DataMotorVehicleSaleMapper motorVehicleSaleMapper, DataUsedCarSalesMapper usedCarSalesMapper, DataFlightItineraryMapper  flightItineraryMapper, DataFlightsItineraryDetailMapper flightsItineraryDetailMapper, DataSteamerTicketMapper steamerTicketMapper, DataMedicalTreatmentMapper dataMedicalTreatmentMapper, DataMedicalTreatmentDetailMapper dataMedicalTreatmentDetailMapper, DataQuotaInvoiceMapper quotaInvoiceMapper, DataTaxiTicketsMapper taxiTicketsMapper, DataRailwayTicketMapper railwayTicketMapper, DataPassengerCarMapper passengerCarMapper, DataTollRoadsMapper tollRoadsMapper, DataReceiptMapper dataReceiptMapper, DataDidiItineraryMapper didiItineraryMapper, DataDidiItineraryDetailsMapper didiItineraryDetailsMapper, DataDutyPaidProofMapper paidProofMapper, DataNonTaxMapper nonTaxMapper, DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper, DataCustomsImxportGoodsMapper customsImportGoodsMapper, DataCustomsExportGoodsMapper customsExportGoodsMapper, DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper, DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper, DataElectronicTransportationGoodsMapper paymentMapper, DataOcrInfoMapper ocrInfoMapper, DataOcrDetailsMapper ocrDetailsMapper) {
        this.filesInfoMapper = filesInfoMapper;
        this.motorVehicleSaleMapper = motorVehicleSaleMapper;
        this.usedCarSalesMapper = usedCarSalesMapper;
        this.flightItineraryMapper = flightItineraryMapper;
        this.flightsItineraryDetailMapper = flightsItineraryDetailMapper;
        this.steamerTicketMapper = steamerTicketMapper;
        this.dataMedicalTreatmentMapper = dataMedicalTreatmentMapper;
        this.dataMedicalTreatmentDetailMapper = dataMedicalTreatmentDetailMapper;
        this.quotaInvoiceMapper = quotaInvoiceMapper;
        this.taxiTicketsMapper = taxiTicketsMapper;
        this.railwayTicketMapper = railwayTicketMapper;
        this.passengerCarMapper = passengerCarMapper;
        this.tollRoadsMapper = tollRoadsMapper;
        this.dataReceiptMapper = dataReceiptMapper;
        this.didiItineraryMapper = didiItineraryMapper;
        this.didiItineraryDetailsMapper = didiItineraryDetailsMapper;
        this.paidProofMapper = paidProofMapper;
        this.nonTaxMapper = nonTaxMapper;
        this.customsImportGoodsDetailMapper = customsImportGoodsDetailMapper;
        this.customsImportGoodsMapper = customsImportGoodsMapper;
        this.customsExportGoodsMapper = customsExportGoodsMapper;
        this.customsExportGoodsDetailMapper = customsExportGoodsDetailMapper;
        this.customsSpecialPaymentMapper = customsSpecialPaymentMapper;
        this.paymentMapper = paymentMapper;
        this.ocrInfoMapper = ocrInfoMapper;
        this.ocrDetailsMapper = ocrDetailsMapper;
    }

    //批量删除
    @Override
    public R deleteWithValidByIds(Collection<String> ids) {
        if (CollectionUtil.isEmpty(ids)){
            return R.fail("请选择要删除的发票");
        }
        //循环遍历查询图片表信息数据
        for (String id : ids) {
            DataImageFilesInfo filesInfo = filesInfoMapper.selectOne(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, id));
            if (ObjUtil.isEmpty(filesInfo)){
                return R.fail();
            }
            String invoiceType = filesInfo.getInvoice();
            //根据发票类型判断,调用不同的发票删除方法,使用switch
            switch (invoiceType) {
                //机动车销售发票
                case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                    removeVehicleSaleInvoice(filesInfo);
                    break;
                //二手车发票
                case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                    removeUserCarSaleInvoice(filesInfo);
                    break;
                //机票
                case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                    removeFilghtItinerary(filesInfo);
                    break;
                //船票
                case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                    removeSteamerTicket(filesInfo);
                    break;
                //医疗票明细票
                case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
                    //医疗票
                case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                    removeMedicalTicket(filesInfo);
                    break;
                //定额发票
                case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                    removeQuotaInvoice(filesInfo);
                    break;
                //出租车发票
                case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                    removeTaxiTickets(filesInfo);
                    break;
                //火车发票
                case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                    removeRailwayTicket(filesInfo);
                    break;
                //客运车发票
                case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                    removePassengerCar(filesInfo);
                    break;
                //过路费发票
                case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                    removeTollRoads(filesInfo);
                    break;
                //小票
                case InvoiceConstants.GLORITY_RECEIPT_CODE:
                    removeReceipt(filesInfo);
                    break;
                //出行发票
                case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                    removeDidiItinerary(filesInfo);
                    break;
                //完税证明发票
                case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                    removeDutyPaidProof(filesInfo);
                    break;
                //非税收入类发票
                case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                    removeNonTax(filesInfo);
                    break;
                //海关进口货物报关单发票
                case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                    removeCustomsImportGoods(filesInfo);
                    break;
                //海关出口货物报关单发票
                case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                    removeCustomsExportGoods(filesInfo);
                    break;
                //海关专用缴款书发票
                case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                    removeCustomsSpecialPayment(filesInfo);
                    break;
                //货物运输电子收款凭证发票
                case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                    removeElectronicPaymentGoodsTransportation(filesInfo);
                    break;
                //增值税、机打
                case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
                case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
                case InvoiceConstants.GLORITY_TAX_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
                case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_LIST:
                case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                    removeOcrInvoice(filesInfo);
                    break;

            }
        }

        return R.ok();
    }
    //删除ocr发票
    private void removeOcrInvoice(DataImageFilesInfo filesInfo) {
        ocrInfoMapper.update(new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()).set(DataOcrInfo::getFileId,  InvoiceConstants.ONE));
        ocrDetailsMapper.update(new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()).set(DataOcrDetails::getFileId,  InvoiceConstants.ONE));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));

    }
    //货物运输电子收款凭证发票
    private void removeElectronicPaymentGoodsTransportation(DataImageFilesInfo filesInfo) {
        paymentMapper.update(new LambdaUpdateWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getFileId, filesInfo.getFileId()).set(DataElectronicTransportationGoods::getFileId,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //海关专用缴款书发票
    private void removeCustomsSpecialPayment(DataImageFilesInfo filesInfo) {
        customsSpecialPaymentMapper.update(new LambdaUpdateWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getFileId, filesInfo.getFileId()).set(DataCustomsSpecialPayment::getFileId,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //海关出口货物报关单发票
    private void removeCustomsExportGoods(DataImageFilesInfo filesInfo) {
        customsExportGoodsMapper.update(new LambdaUpdateWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getFileId, filesInfo.getFileId()).set(DataCustomsExportGoods::getDeleteFlag,  InvoiceConstants.ONE));
        customsExportGoodsDetailMapper.update(new LambdaUpdateWrapper<DataCustomsExportGoodsDetail>().eq(DataCustomsExportGoodsDetail::getFileId, filesInfo.getFileId()).set(DataCustomsExportGoodsDetail::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }
    //海关进口货物报关单发票
    private void removeCustomsImportGoods(DataImageFilesInfo filesInfo) {
        customsImportGoodsMapper.update(new LambdaUpdateWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getFileId, filesInfo.getFileId()).set(DataCustomsImxportGoods::getDeleteFlag, InvoiceConstants.ONE));
        customsImportGoodsDetailMapper.update(new LambdaUpdateWrapper<DataCustomsImportGoodsDetail>().eq(DataCustomsImportGoodsDetail::getFileId, filesInfo.getFileId()).set(DataCustomsImportGoodsDetail::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
    }

    // 非税收入类发票
    private void removeNonTax(DataImageFilesInfo filesInfo) {
        nonTaxMapper.update(new LambdaUpdateWrapper<DataNonTax>().eq(DataNonTax::getFileId, filesInfo.getFileId()).set(DataNonTax::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));

    }

    // 完税证明发票
    private void removeDutyPaidProof(DataImageFilesInfo filesInfo) {
        paidProofMapper.update(new LambdaUpdateWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getFileId, filesInfo.getFileId()).set(DataDutyPaidProof::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    private void removeDidiItinerary(DataImageFilesInfo filesInfo) {
        didiItineraryMapper.update(new LambdaUpdateWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getFileId, filesInfo.getFileId()).set(DataDidiItinerary::getDeleteFlag,  InvoiceConstants.ONE));
        didiItineraryDetailsMapper.update(new LambdaUpdateWrapper<DataDidiItineraryDetails>().eq(DataDidiItineraryDetails::getFileId, filesInfo.getFileId()).set(DataDidiItineraryDetails::getDeleteFlag, InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //删除小票
    private void removeReceipt(DataImageFilesInfo filesInfo) {
        dataReceiptMapper.update(new LambdaUpdateWrapper<DataReceipt>().eq(DataReceipt::getFileId, filesInfo.getFileId()).set(DataReceipt::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //删除过路费
    private void removeTollRoads(DataImageFilesInfo filesInfo) {
        tollRoadsMapper.update(new LambdaUpdateWrapper<DataTollRoads>().eq(DataTollRoads::getFileId, filesInfo.getFileId()).set(DataTollRoads::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //删除客车发票
    private void removePassengerCar(DataImageFilesInfo filesInfo) {
        passengerCarMapper.update(new LambdaUpdateWrapper<DataPassengerCar>().eq(DataPassengerCar::getFileId, filesInfo.getFileId()).set(DataPassengerCar::getDeleteFlag, InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
    }

    //删除火车票
    private void removeRailwayTicket(DataImageFilesInfo filesInfo) {
        railwayTicketMapper.update(new LambdaUpdateWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getFileId, filesInfo.getFileId()).set(DataRailwayTicket::getDeleteFlag,  InvoiceConstants.ONE));
        //图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }



    //删除出租车发票
    private void removeTaxiTickets(DataImageFilesInfo filesInfo) {
        taxiTicketsMapper.update(new LambdaUpdateWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getFileId, filesInfo.getFileId()).set(DataTaxiTickets::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //删除定额发票
    private void removeQuotaInvoice(DataImageFilesInfo filesInfo) {
        quotaInvoiceMapper.update(new LambdaUpdateWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getFileId, filesInfo.getFileId()).set(DataQuotaInvoice::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //删除医疗发票
    private void removeMedicalTicket(DataImageFilesInfo filesInfo) {
        dataMedicalTreatmentMapper.update(new LambdaUpdateWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getFileId, filesInfo.getFileId()).set(DataMedicalTreatment::getDeleteFlag,  InvoiceConstants.ONE));
        dataMedicalTreatmentDetailMapper.update(new LambdaUpdateWrapper<DataMedicalTreatmentDetail>().eq(DataMedicalTreatmentDetail::getFileId, filesInfo.getFileId()).set(DataMedicalTreatmentDetail::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //船票
    private void removeSteamerTicket(DataImageFilesInfo filesInfo) {
        steamerTicketMapper.update(new LambdaUpdateWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getFileId, filesInfo.getFileId()).set(DataSteamerTicket::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //删除机票(航空电子客运)发票
    private void removeFilghtItinerary(DataImageFilesInfo filesInfo) {
        flightItineraryMapper.update(new LambdaUpdateWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getFileId, filesInfo.getFileId()).set(DataFlightItinerary::getDeleteFlag,  InvoiceConstants.ONE));
        flightsItineraryDetailMapper.update(new LambdaUpdateWrapper<DataFlightsItineraryDetail>().eq(DataFlightsItineraryDetail::getFileId, filesInfo.getFileId()).set(DataFlightsItineraryDetail::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }

    //删除二手车发票
    private void removeUserCarSaleInvoice(DataImageFilesInfo filesInfo) {
        usedCarSalesMapper.update(new LambdaUpdateWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, filesInfo.getFileId()).set(DataUsedCarSales::getDeleteFlag, InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));


    }

    //删除机动车销售发票
    private void removeVehicleSaleInvoice(DataImageFilesInfo filesInfo) {
        motorVehicleSaleMapper.update(new LambdaUpdateWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getFileId, filesInfo.getFileId()).set(DataMotorVehicleSale::getDeleteFlag,  InvoiceConstants.ONE));
        //删除图片信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag,  InvoiceConstants.ONE));
    }
}
