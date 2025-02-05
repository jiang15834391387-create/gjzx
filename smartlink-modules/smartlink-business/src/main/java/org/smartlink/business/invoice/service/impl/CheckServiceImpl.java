package org.smartlink.business.invoice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.StringUtils;
import org.smartlink.business.enumd.CheckInvoiceStatusEnumd;
import org.smartlink.business.invoice.check.CheckInvoice;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.check.doman.BillRequest;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.*;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CheckServiceImpl implements ICheckService {
    private final CheckInvoice checkInvoice;
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
    private final DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper;
    private final DataCustomsImxportGoodsMapper customsImportGoodsMapper;
    private final DataCustomsExportGoodsMapper customsExportGoodsMapper;
    private final DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper;
    private final DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper;
    private final DataElectronicTransportationGoodsMapper paymentMapper;
    private final DataOcrInfoMapper ocrInfoMapper;
    private final DataOcrDetailsMapper ocrDetailsMapper;

    public CheckServiceImpl(CheckInvoice checkInvoice, DataImageFilesInfoMapper filesInfoMapper, DataMotorVehicleSaleMapper motorVehicleSaleMapper, DataUsedCarSalesMapper usedCarSalesMapper, DataFlightItineraryMapper flightItineraryMapper, DataFlightsItineraryDetailMapper flightsItineraryDetailMapper, DataSteamerTicketMapper steamerTicketMapper, DataMedicalTreatmentMapper dataMedicalTreatmentMapper, DataMedicalTreatmentDetailMapper dataMedicalTreatmentDetailMapper, DataQuotaInvoiceMapper quotaInvoiceMapper, DataTaxiTicketsMapper taxiTicketsMapper, DataRailwayTicketMapper railwayTicketMapper, DataPassengerCarMapper passengerCarMapper, DataTollRoadsMapper tollRoadsMapper, DataReceiptMapper dataReceiptMapper, DataDidiItineraryMapper didiItineraryMapper, DataDidiItineraryDetailsMapper didiItineraryDetailsMapper, DataDutyPaidProofMapper paidProofMapper, DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper, DataCustomsImxportGoodsMapper customsImportGoodsMapper, DataCustomsExportGoodsMapper customsExportGoodsMapper, DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper, DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper, DataElectronicTransportationGoodsMapper paymentMapper, DataOcrInfoMapper ocrInfoMapper, DataOcrDetailsMapper ocrDetailsMapper) {
        this.checkInvoice = checkInvoice;
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
    public int deleteWithValidByIds(Collection<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return 0;
        }
        //循环遍历查询图片表信息数据
        for (String id : ids) {
            DataImageFilesInfo filesInfo = filesInfoMapper.selectOne(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, id));
            if (ObjUtil.isEmpty(filesInfo)) {
                return 0;
            }
            String invoiceType = filesInfo.getInvoice();
            //根据发票类型判断,调用不同的发票删除方法,使用switch
            switch (invoiceType) {
                //机动车销售发票
                case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                    return removeVehicleSaleInvoice(filesInfo);
                //二手车发票
                case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                    return removeUserCarSaleInvoice(filesInfo);
                //机票
                case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                    return removeFilghtItinerary(filesInfo);
                //船票
                case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                    return removeSteamerTicket(filesInfo);
                //医疗票明细票 非税收入类发票
                case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
                case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                    return removeMedicalTicket(filesInfo);
                //定额发票
                case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                    return removeQuotaInvoice(filesInfo);
                //出租车发票
                case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                    return removeTaxiTickets(filesInfo);
                //火车发票
                case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                    return removeRailwayTicket(filesInfo);
                //客运车发票
                case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                    return removePassengerCar(filesInfo);
                //过路费发票
                case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                    return removeTollRoads(filesInfo);
                //小票
                case InvoiceConstants.GLORITY_RECEIPT_CODE:
                    return removeReceipt(filesInfo);
                //出行发票
                case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                    return removeDidiItinerary(filesInfo);
                //完税证明发票
                case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                    return removeDutyPaidProof(filesInfo);
                //海关进口货物报关单发票
                case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                    return removeCustomsImportGoods(filesInfo);
                //海关出口货物报关单发票
                case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                    return removeCustomsExportGoods(filesInfo);
                //海关专用缴款书发票
                case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                    return removeCustomsSpecialPayment(filesInfo);
                //货物运输电子收款凭证发票
                case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                    return removeElectronicPaymentGoodsTransportation(filesInfo);
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
                    return removeOcrInvoice(filesInfo);

            }
        }
        return 0;
    }

    //删除ocr发票
    private int removeOcrInvoice(DataImageFilesInfo filesInfo) {
       ocrInfoMapper.update(new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()).set(DataOcrInfo::getFileId, InvoiceConstants.ONE));
       ocrDetailsMapper.update(new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()).set(DataOcrDetails::getFileId, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }
    //删除货物运输电子收款凭证发票
    private int removeElectronicPaymentGoodsTransportation(DataImageFilesInfo filesInfo) {
       paymentMapper.update(new LambdaUpdateWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getFileId, filesInfo.getFileId()).set(DataElectronicTransportationGoods::getFileId, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }

    //删除海关专用缴款书发票
    private int removeCustomsSpecialPayment(DataImageFilesInfo filesInfo) {
      customsSpecialPaymentMapper.update(new LambdaUpdateWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getFileId, filesInfo.getFileId()).set(DataCustomsSpecialPayment::getFileId, InvoiceConstants.ONE));
      filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
      return 1;
    }
    //删除海关出口货物报关单发票
    private int removeCustomsExportGoods(DataImageFilesInfo filesInfo) {
       customsExportGoodsMapper.update(new LambdaUpdateWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getFileId, filesInfo.getFileId()).set(DataCustomsExportGoods::getDeleteFlag, InvoiceConstants.ONE));
       customsExportGoodsDetailMapper.update(new LambdaUpdateWrapper<DataCustomsExportGoodsDetail>().eq(DataCustomsExportGoodsDetail::getFileId, filesInfo.getFileId()).set(DataCustomsExportGoodsDetail::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }

    //删除海关进口货物报关单发票
    private int removeCustomsImportGoods(DataImageFilesInfo filesInfo) {
       customsImportGoodsMapper.update(new LambdaUpdateWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getFileId, filesInfo.getFileId()).set(DataCustomsImxportGoods::getDeleteFlag, InvoiceConstants.ONE));
       customsImportGoodsDetailMapper.update(new LambdaUpdateWrapper<DataCustomsImportGoodsDetail>().eq(DataCustomsImportGoodsDetail::getFileId, filesInfo.getFileId()).set(DataCustomsImportGoodsDetail::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }
    // 删除完税证明发票
    private int removeDutyPaidProof(DataImageFilesInfo filesInfo) {
        paidProofMapper.update(new LambdaUpdateWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getFileId, filesInfo.getFileId()).set(DataDutyPaidProof::getDeleteFlag, InvoiceConstants.ONE));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
        return 1;
    }
    // 删除滴滴行程单发票/电子行程单
    private int removeDidiItinerary(DataImageFilesInfo filesInfo) {
       didiItineraryMapper.update(new LambdaUpdateWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getFileId, filesInfo.getFileId()).set(DataDidiItinerary::getDeleteFlag, InvoiceConstants.ONE));
       didiItineraryDetailsMapper.update(new LambdaUpdateWrapper<DataDidiItineraryDetails>().eq(DataDidiItineraryDetails::getFileId, filesInfo.getFileId()).set(DataDidiItineraryDetails::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }

    //删除小票
    private int removeReceipt(DataImageFilesInfo filesInfo) {
        dataReceiptMapper.update(new LambdaUpdateWrapper<DataReceipt>().eq(DataReceipt::getFileId, filesInfo.getFileId()).set(DataReceipt::getDeleteFlag, InvoiceConstants.ONE));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
        return 1;
    }

    //删除过路费
    private int removeTollRoads(DataImageFilesInfo filesInfo) {
      tollRoadsMapper.update(new LambdaUpdateWrapper<DataTollRoads>().eq(DataTollRoads::getFileId, filesInfo.getFileId()).set(DataTollRoads::getDeleteFlag, InvoiceConstants.ONE));
      filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
      return 1;
    }

    //删除客车发票
    private int removePassengerCar(DataImageFilesInfo filesInfo) {
        passengerCarMapper.update(new LambdaUpdateWrapper<DataPassengerCar>().eq(DataPassengerCar::getFileId, filesInfo.getFileId()).set(DataPassengerCar::getDeleteFlag, InvoiceConstants.ONE));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
        return 1;
    }

    //删除火车票
    private int removeRailwayTicket(DataImageFilesInfo filesInfo) {
       railwayTicketMapper.update(new LambdaUpdateWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getFileId, filesInfo.getFileId()).set(DataRailwayTicket::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }

    //删除出租车发票
    private int removeTaxiTickets(DataImageFilesInfo filesInfo) {
      taxiTicketsMapper.update(new LambdaUpdateWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getFileId, filesInfo.getFileId()).set(DataTaxiTickets::getDeleteFlag, InvoiceConstants.ONE));
      filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
      return 1;
    }

    //删除定额发票
    private int removeQuotaInvoice(DataImageFilesInfo filesInfo) {
       quotaInvoiceMapper.update(new LambdaUpdateWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getFileId, filesInfo.getFileId()).set(DataQuotaInvoice::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }

    //删除医疗发票
    private int removeMedicalTicket(DataImageFilesInfo filesInfo) {
       dataMedicalTreatmentMapper.update(new LambdaUpdateWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getFileId, filesInfo.getFileId()).set(DataMedicalTreatment::getDeleteFlag, InvoiceConstants.ONE));
       dataMedicalTreatmentDetailMapper.update(new LambdaUpdateWrapper<DataMedicalTreatmentDetail>().eq(DataMedicalTreatmentDetail::getFileId, filesInfo.getFileId()).set(DataMedicalTreatmentDetail::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }
    //船票
    private int removeSteamerTicket(DataImageFilesInfo filesInfo) {
        steamerTicketMapper.update(new LambdaUpdateWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getFileId, filesInfo.getFileId()).set(DataSteamerTicket::getDeleteFlag, InvoiceConstants.ONE));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
        return 1;
    }

    //删除机票(航空电子客运)发票
    private int removeFilghtItinerary(DataImageFilesInfo filesInfo) {
       flightItineraryMapper.update(new LambdaUpdateWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getFileId, filesInfo.getFileId()).set(DataFlightItinerary::getDeleteFlag, InvoiceConstants.ONE));
       flightsItineraryDetailMapper.update(new LambdaUpdateWrapper<DataFlightsItineraryDetail>().eq(DataFlightsItineraryDetail::getFileId, filesInfo.getFileId()).set(DataFlightsItineraryDetail::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }
    //删除二手车发票
    private int removeUserCarSaleInvoice(DataImageFilesInfo filesInfo) {
       usedCarSalesMapper.update(new LambdaUpdateWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, filesInfo.getFileId()).set(DataUsedCarSales::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }
    //删除机动车销售发票
    private int removeVehicleSaleInvoice(DataImageFilesInfo filesInfo) {
       motorVehicleSaleMapper.update(new LambdaUpdateWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getFileId, filesInfo.getFileId()).set(DataMotorVehicleSale::getDeleteFlag, InvoiceConstants.ONE));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, InvoiceConstants.ONE));
       return 1;
    }

    /**
     * 发票修改
     */
    @Override
    public int invoiceAlter(InvoiceRequest request) throws Exception {
        String invoiceType = request.getInvoiceType();
        if (StrUtil.isEmpty(invoiceType)) {
            return 0;
        }
        log.info("修改发票类型：{}", invoiceType);
        Map<String, Object> generalInfo = request.getGeneralInfo();
        log.info("修改传入的基本发票信息：{}", generalInfo);
        BillRequest billRequest = request.getBillRequest();
        if (ObjUtil.isEmpty(billRequest)){
            return 0;
        }
        List<Map<String, Object>> details = billRequest.getDetails();
        log.info("修改传入的发票明细信息：{}", details);
        // 根据发票类型处理逻辑
        switch (invoiceType) {
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                // 处理增值税发票
                 return handleVatInvoice(generalInfo, request.getInvoiceType());
            // 数电票普通发票/机打发票
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                return updateOrdinaryInvoice(generalInfo, details);
            //机动车销售发票
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                return updateVehicleSaleInvoice(generalInfo);
            //二手车发票
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                return updateCarSaleInvoice(generalInfo);
            //航空电子客运单
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                return updateFilghtItinerary(generalInfo, details);
            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                return updateSteamerTicket(generalInfo);
            //医疗票明细票/医疗票/非税票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                return updateMedicalTicket(generalInfo, details);
            //定额发票
            case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                return updateQuotaInvoice(generalInfo);
            //出租车发票
            case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                return updateTaxiTickets(generalInfo);
            //火车发票
            case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                return updateRailwayTicket(generalInfo);
            //客运车发票
            case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                return updatePassengerCar(generalInfo);
            //过路费发票
            case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                return updateTollRoads(generalInfo);
            //小票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
                return updateReceipt(generalInfo);
            //出行发票/滴滴
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                return updateDidiItinerary(generalInfo, details);
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                return updateDutyPaidProof(generalInfo);
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                return updateCustomsImportGoods(generalInfo, details);
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                return updateCustomsExportGoods(generalInfo, details);
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                return updateCustomsSpecialPayment(generalInfo);
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                return updateElectronicPaymentGoodsTransportation(generalInfo);
        }
        return 0;
    }
    //修改海关专用缴款书发票
    private int updateCustomsSpecialPayment(Map<String, Object> generalInfo) {
        DataCustomsSpecialPayment customsSpecialPayment = BeanUtil.toBean(generalInfo, DataCustomsSpecialPayment.class);
        int updateResult = customsSpecialPaymentMapper.updateById(customsSpecialPayment);
        if (updateResult <= 0) {
           return 0;
        }
        return 1;
    }
    //修改海关出口货物报关单发票
    private int updateCustomsExportGoods(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataCustomsExportGoods customsExportGoods = BeanUtil.toBean(generalInfo, DataCustomsExportGoods.class);
        int updateResult = customsExportGoodsMapper.updateById(customsExportGoods);
        if (updateResult <= 0) {
            return 0;
        }
        // 将细节数据转换为实体对象
        List<DataCustomsExportGoodsDetail> customsExportGoodsDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataCustomsExportGoodsDetail.class))
            .toList();
        for (DataCustomsExportGoodsDetail item : customsExportGoodsDetails) {
            int i = customsExportGoodsDetailMapper.updateById(item);
            if (i <= 0) {
                return 0;
            }
        }
        return 1;
    }

    //修改海关进口货物报关单发票
    private int updateCustomsImportGoods(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        // 将主表数据转换为实体对象
        DataCustomsImxportGoods customsImxportGoods = BeanUtil.toBean(generalInfo, DataCustomsImxportGoods.class);
        // 更新主表信息，并检查是否成功
        int updateResult = customsImportGoodsMapper.updateById(customsImxportGoods);
        if (updateResult <= 0) {
            return 0;
        }
        List<DataCustomsImportGoodsDetail> customsImportGoodsDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataCustomsImportGoodsDetail.class))
            .toList();
        for (DataCustomsImportGoodsDetail item : customsImportGoodsDetails) {
            int i = customsImportGoodsDetailMapper.updateById(item);
            if (i <= 0) {
                return 0;
            }
        }
        return 1;
    }
    //修改出行发票/滴滴
    private int updateDidiItinerary(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataDidiItinerary didiItinerary = BeanUtil.toBean(generalInfo, DataDidiItinerary.class);
        int updateResult = didiItineraryMapper.updateById(didiItinerary);
        if (updateResult <= 0) {
            return 0;
        }
        List<DataDidiItineraryDetails> didiItineraryDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataDidiItineraryDetails.class))
            .toList();
        for (DataDidiItineraryDetails item : didiItineraryDetails) {
            int i = didiItineraryDetailsMapper.updateById(item);
            if (i <= 0) {
                return 0;
            }
        }
        return 1;
    }
    //修改医疗票
    private int updateMedicalTicket(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataMedicalTreatment medicalTreatment = BeanUtil.toBean(generalInfo, DataMedicalTreatment.class);
        int updateResult = dataMedicalTreatmentMapper.updateById(medicalTreatment);
        if (updateResult <= 0) {
            return 0;
        }
        List<DataMedicalTreatmentDetail> medicalTreatmentDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataMedicalTreatmentDetail.class))
            .toList();
        for (DataMedicalTreatmentDetail item : medicalTreatmentDetails) {
            int i = dataMedicalTreatmentDetailMapper.updateById(item);
            if (i <= 0) {
                return 0;
            }
        }
        return 1;
    }

    //修改航空电子客运发票
    private int updateFilghtItinerary(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataFlightItinerary flightItinerary = BeanUtil.toBean(generalInfo, DataFlightItinerary.class);
        int updateResult = flightItineraryMapper.updateById(flightItinerary);
        if (updateResult <= 0) {
            return 0;
        }
        List<DataFlightsItineraryDetail> flightItineraryDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataFlightsItineraryDetail.class))
            .toList();
        for (DataFlightsItineraryDetail item : flightItineraryDetails) {
            int i = flightsItineraryDetailMapper.updateById(item);
            if (i <= 0) {
                return 0;
            }
        }
        return 1;
    }

    //处理数电票/普通发票修改
    private int updateOrdinaryInvoice(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataOcrInfo ocrInfo = BeanUtil.toBean(generalInfo, DataOcrInfo.class);
        int updateResult = ocrInfoMapper.updateById(ocrInfo);
        if (updateResult <= 0) {
            return 0;
        }
        List<DataOcrDetails> ocrDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataOcrDetails.class))
            .toList();

        for (DataOcrDetails item : ocrDetails) {
            int i = ocrDetailsMapper.updateById(item);
            if (i <= 0) {
                return 0;
            }
        }
        return 1;
    }

    //货物运输电子收款凭证发票发票修改
    private int updateElectronicPaymentGoodsTransportation(Map<String, Object> generalInfo) {
        DataElectronicTransportationGoods transportationGoods = BeanUtil.toBean(generalInfo, DataElectronicTransportationGoods.class);
        int updateResult =paymentMapper.updateById(transportationGoods);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }

    //处理增值税发票修改
    private int handleVatInvoice(Map<String, Object> generalInfo, String invoiceType) throws Exception {
        // DTO 初始化
        InvoiceCheckParamDTO dto = new InvoiceCheckParamDTO();
        // 处理区块链标记及通用信息
        handleGeneralInfo(dto, generalInfo, invoiceType);
        // 根据发票类型分类处理
        if (isDigitalInvoice(invoiceType)) {
            // 数电票处理
            handleDigitalInvoice(dto, generalInfo, invoiceType);
        } else if (isSpecialVatInvoice(invoiceType)) {
            // 增值税专用发票处理
            handleSpecialVatInvoice(dto, generalInfo, invoiceType);
        } else {
            // 发票处理
            handleRegularInvoice(dto, generalInfo, invoiceType);
        }
        // 调用查验方法
      return validateInvoice(dto, generalInfo);

    }

    //增值税专用发票处理
    private void handleSpecialVatInvoice(InvoiceCheckParamDTO dto, Map<String, Object> generalInfo, String invoiceType) {
        dto.setCode((String) generalInfo.get("invoiceCode"));
        dto.setNumber((String) generalInfo.get("invoiceNumber"));
        dto.setDate((String) generalInfo.get("invoiceDate"));
        dto.setType(invoiceType);
        //电子票标识
        dto.setElectron_mark(Integer.valueOf((String) generalInfo.get("electronicMark")));
        //金额
        dto.setPretax_amount((String) generalInfo.get("pretaxAmount"));
    }

    // 处理区块链标记及通用信息
    private void handleGeneralInfo(InvoiceCheckParamDTO dto, Map<String, Object> generalInfo,String invoiceType) {
        // 区块链标记
        if (StrUtil.isNotBlank((String) generalInfo.get("blockChain"))) {
            String blockChain = (String) generalInfo.get("blockChain");
            if (InvoiceConstants.ONE.equals(blockChain)) {
                dto.setBlock_chain(Integer.valueOf(blockChain));
            }
        }
        // 纳税人识别号
        if (StrUtil.isNotBlank((String) generalInfo.get("sellerNo"))) {
            dto.setSeller_tax_id((String) generalInfo.get("sellerNo"));
        }
        // 地区信息
        if (StrUtil.isNotBlank((String) generalInfo.get("province"))) {
            dto.setArea((String) generalInfo.get("province"));
        }
        dto.setCode((String) generalInfo.get("invoiceCode"));
        dto.setNumber((String) generalInfo.get("invoiceNumber"));
        dto.setDate((String) generalInfo.get("invoiceDate"));
        dto.setType(invoiceType);
    }

    // 判断是否为数电票
    private boolean isDigitalInvoice(String invoiceType) {
        return InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE.equals(invoiceType) ||
            InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE.equals(invoiceType);
    }

    // 判断是否为增值税专用发票
    private boolean isSpecialVatInvoice(String invoiceType) {
        return InvoiceConstants.GLORITY_TAX_SPECIAL_CODE.equals(invoiceType);
    }

    // 处理数电票类型
    private void handleDigitalInvoice(InvoiceCheckParamDTO dto, Map<String, Object> generalInfo, String invoiceType) {
        dto.setNumber((String) generalInfo.get("invoiceNumber"));
        dto.setTotal((String) generalInfo.get("totalLowercase"));
        dto.setDate((String) generalInfo.get("invoiceDate"));
        dto.setType(invoiceType);
    }

    // 处理增值税普通发票(电子)/卷票/类型
    private void handleRegularInvoice(InvoiceCheckParamDTO dto, Map<String, Object> generalInfo, String invoiceType) {
        dto.setCode((String) generalInfo.get("invoiceCode"));
        dto.setNumber((String) generalInfo.get("invoiceNumber"));
        dto.setDate((String) generalInfo.get("invoiceDate"));
        dto.setType(invoiceType);

        // 校验码截取（后 6 位）
        String checkCodes = (String) generalInfo.get("checkCode");
        if (StringUtils.hasText(checkCodes) && checkCodes.length() > 5) {
            checkCodes = checkCodes.substring(checkCodes.length() - 6);
        }
        dto.setCheck_code(checkCodes);
    }

    //发票查验
    private int validateInvoice(InvoiceCheckParamDTO dto, Map<String, Object> generalInfo) throws Exception {
        // 获取 fileId
        String fileId = (String) generalInfo.get("fileId");
        if (StrUtil.isEmpty(fileId)) {
            log.error("fileId 为空");
           return 0;
        }
        DataImageFilesInfo filesInfo = filesInfoMapper.selectOne(
            new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, fileId)
        );
        if (ObjUtil.isEmpty(filesInfo)) {
            log.error("根据 fileId 查询图片信息为空");
            return 0;
        }
        // 调用查验方法
        BaseEntity baseEntity = checkInvoice.checkInvoice(filesInfo, dto);
        //查验成功
        if (filesInfo.getFileStatus().equals(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode()) && filesInfo.getCheckStatus().equals(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode())) {
            //如果是增值税（专用/普通/电子专用）或增值税电子普通发票 或区块链电子发票  或机打发票 或增值税普通发票(卷票)或数电票(增值税专用发票/普通发票)
            if (filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_TAX_SPECIAL_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_TAX_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_ELECTRONIC_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_ROLL_TICKET_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE)) {
                //转换后增值税发票进行修改
               return ocrConversionAlter(baseEntity, filesInfo);
            }
        }
        return 1;
    }
    //修改发票后查验成功 修改发票信息
    private int ocrConversionAlter(BaseEntity baseEntity, DataImageFilesInfo filesInfo) {
        if (ObjectUtil.isEmpty(baseEntity)){
            log.error("ocr查验转换后结果为空");
            return 0;
        }
        DataOcrInfo dataOcrInfo = BeanUtil.toBean(baseEntity, DataOcrInfo.class);
        log.info("修改查验转换后的OCR信息：{}", dataOcrInfo);
        List<DataOcrDetails> arrayList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(dataOcrInfo.getDetails())) {
            List<DataOcrDetails> details = dataOcrInfo.getDetails();
            for (DataOcrDetails ocrDetails : details) {
                DataOcrDetails ocrDetail = BeanUtil.toBean(ocrDetails, DataOcrDetails.class);
                ocrDetail.setFileId(filesInfo.getFileId());
                arrayList.add(ocrDetail);
            }
        }
        //根据file_id修改ocr基本信息
        int updated=ocrInfoMapper.update(dataOcrInfo, new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()));
        if (updated<=0){
            return 0;
        }
        for (DataOcrDetails detail : arrayList) {
            int update=ocrDetailsMapper.update(detail, new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()).eq(DataOcrDetails::getId, detail.getId()));
            if (update<=0){
               return 0;
            }
        }
        return 1;
    }

    //修改机动车销售发票
    private int updateVehicleSaleInvoice (Map < String, Object > generalInfo){
        DataMotorVehicleSale motorVehicleSale = BeanUtil.toBean(generalInfo, DataMotorVehicleSale.class);
        int updateResult = motorVehicleSaleMapper.updateById(motorVehicleSale);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改二手车销售发票
    private int updateCarSaleInvoice (Map < String, Object > generalInfo){
        DataUsedCarSales usedCarSales = BeanUtil.toBean(generalInfo, DataUsedCarSales.class);
        int updateResult=usedCarSalesMapper.updateById(usedCarSales);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改船票
    private int updateSteamerTicket (Map < String, Object > generalInfo){
        DataSteamerTicket steamerTicket = BeanUtil.toBean(generalInfo, DataSteamerTicket.class);
        int updateResult =steamerTicketMapper.updateById(steamerTicket);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改定额发票
    private int updateQuotaInvoice (Map < String, Object > generalInfo){
        DataQuotaInvoice quotaInvoice = BeanUtil.toBean(generalInfo, DataQuotaInvoice.class);
        int updateResult =quotaInvoiceMapper.updateById(quotaInvoice);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改出租车票
    private int updateTaxiTickets (Map < String, Object > generalInfo){
        DataTaxiTickets taxiTickets = BeanUtil.toBean(generalInfo, DataTaxiTickets.class);
        int updateResult =taxiTicketsMapper.updateById(taxiTickets);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改火车票
    private int updateRailwayTicket (Map < String, Object > generalInfo){
        DataRailwayTicket railwayTicket = BeanUtil.toBean(generalInfo, DataRailwayTicket.class);
        int updateResult =railwayTicketMapper.updateById(railwayTicket);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改汽车票
    private int updatePassengerCar (Map < String, Object > generalInfo){
        DataPassengerCar passengerCar = BeanUtil.toBean(generalInfo, DataPassengerCar.class);
        int updateResult =passengerCarMapper.updateById(passengerCar);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改过路费
    private int updateTollRoads (Map < String, Object > generalInfo){
        DataTollRoads tollRoads = BeanUtil.toBean(generalInfo, DataTollRoads.class);
        int updateResult =tollRoadsMapper.updateById(tollRoads);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改小票
    private int updateReceipt (Map < String, Object > generalInfo){
        DataReceipt receipt = BeanUtil.toBean(generalInfo, DataReceipt.class);
        int updateResult =dataReceiptMapper.updateById(receipt);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }
    //修改完税证明
    private int updateDutyPaidProof (Map < String, Object > generalInfo){
        DataDutyPaidProof dutyPaidProof = BeanUtil.toBean(generalInfo, DataDutyPaidProof.class);
        int updateResult =paidProofMapper.updateById(dutyPaidProof);
        if (updateResult <= 0){
            return 0;
        }
        return 1;
    }

}

