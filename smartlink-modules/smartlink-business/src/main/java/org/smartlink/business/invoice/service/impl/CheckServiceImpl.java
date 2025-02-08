package org.smartlink.business.invoice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.StringUtils;
import org.smartlink.business.enumd.CheckInvoiceStatusEnumd;
import org.smartlink.business.invoice.check.CheckInvoice;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.check.doman.BillRequest;
import org.smartlink.common.check.doman.InvoicePageQuery;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.doman.vo.InvoiceVo;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.FileStatusEnumd;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.*;
import org.smartlink.common.entity.domain.business.response.DataResponseDTO;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Slf4j
@Service
public class CheckServiceImpl implements ICheckService {
    private final DataNonTaxMapper dataNonTaxMapper;
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
    private final DataDutyPaidProofDetailsMapper paidProofDetailsMapper;
    private final DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper;
    private final DataCustomsImxportGoodsMapper customsImportGoodsMapper;
    private final DataCustomsExportGoodsMapper customsExportGoodsMapper;
    private final DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper;
    private final DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper;
    private final DataElectronicTransportationGoodsMapper paymentMapper;
    private final DataOcrInfoMapper ocrInfoMapper;
    private final DataOcrDetailsMapper ocrDetailsMapper;
    public CheckServiceImpl(DataNonTaxMapper dataNonTaxMapper, CheckInvoice checkInvoice, DataImageFilesInfoMapper filesInfoMapper, DataMotorVehicleSaleMapper motorVehicleSaleMapper, DataUsedCarSalesMapper usedCarSalesMapper, DataFlightItineraryMapper flightItineraryMapper, DataFlightsItineraryDetailMapper flightsItineraryDetailMapper, DataSteamerTicketMapper steamerTicketMapper, DataMedicalTreatmentMapper dataMedicalTreatmentMapper, DataMedicalTreatmentDetailMapper dataMedicalTreatmentDetailMapper, DataQuotaInvoiceMapper quotaInvoiceMapper, DataTaxiTicketsMapper taxiTicketsMapper, DataRailwayTicketMapper railwayTicketMapper, DataPassengerCarMapper passengerCarMapper, DataTollRoadsMapper tollRoadsMapper, DataReceiptMapper dataReceiptMapper, DataDidiItineraryMapper didiItineraryMapper, DataDidiItineraryDetailsMapper didiItineraryDetailsMapper, DataDutyPaidProofMapper paidProofMapper, DataDutyPaidProofDetailsMapper paidProofDetailsMapper, DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper, DataCustomsImxportGoodsMapper customsImportGoodsMapper, DataCustomsExportGoodsMapper customsExportGoodsMapper, DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper, DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper, DataElectronicTransportationGoodsMapper paymentMapper, DataOcrInfoMapper ocrInfoMapper, DataOcrDetailsMapper ocrDetailsMapper) {
        this.dataNonTaxMapper = dataNonTaxMapper;
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
        this.paidProofDetailsMapper = paidProofDetailsMapper;
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
            if (StrUtil.isEmpty(invoiceType)){
                return 0;
            }
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
                case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                     removeMedicalTicket(filesInfo);
                     break;
                //非税发票
                case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                     removeNonTaxInvoice(filesInfo);
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
                default: {
                    return 0;
                }
            }
        }
        return 1;
    }
    private void removeOcrDetail(DataImageFilesInfo filesInfo) {
        ocrDetailsMapper.update(new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()).set(DataOcrDetails::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
    }
    //删除非税发票
    private void removeNonTaxInvoice(DataImageFilesInfo filesInfo) {
        dataNonTaxMapper.update(new LambdaUpdateWrapper<DataNonTax>().eq(DataNonTax::getFileId, filesInfo.getFileId()).set(DataNonTax::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        removeOcrDetail(filesInfo);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
    }

    //删除ocr发票
    private void removeOcrInvoice(DataImageFilesInfo filesInfo) {
       ocrInfoMapper.update(new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()).set(DataOcrInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        removeOcrDetail(filesInfo);
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
    }
    //删除货物运输电子收款凭证发票
    private void removeElectronicPaymentGoodsTransportation(DataImageFilesInfo filesInfo) {
       paymentMapper.update(new LambdaUpdateWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getFileId, filesInfo.getFileId()).set(DataElectronicTransportationGoods::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       removeOcrDetail(filesInfo);
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除海关专用缴款书发票
    private void removeCustomsSpecialPayment(DataImageFilesInfo filesInfo) {
      customsSpecialPaymentMapper.update(new LambdaUpdateWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getFileId, filesInfo.getFileId()).set(DataCustomsSpecialPayment::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
      removeOcrDetail(filesInfo);
      filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }
    //删除海关出口货物报关单发票
    private void removeCustomsExportGoods(DataImageFilesInfo filesInfo) {
       customsExportGoodsMapper.update(new LambdaUpdateWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getFileId, filesInfo.getFileId()).set(DataCustomsExportGoods::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       customsExportGoodsDetailMapper.update(new LambdaUpdateWrapper<DataCustomsExportGoodsDetail>().eq(DataCustomsExportGoodsDetail::getFileId, filesInfo.getFileId()).set(DataCustomsExportGoodsDetail::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除海关进口货物报关单发票
    private void removeCustomsImportGoods(DataImageFilesInfo filesInfo) {
       customsImportGoodsMapper.update(new LambdaUpdateWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getFileId, filesInfo.getFileId()).set(DataCustomsImxportGoods::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       customsImportGoodsDetailMapper.update(new LambdaUpdateWrapper<DataCustomsImportGoodsDetail>().eq(DataCustomsImportGoodsDetail::getFileId, filesInfo.getFileId()).set(DataCustomsImportGoodsDetail::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }
    // 删除完税证明发票
    private void removeDutyPaidProof(DataImageFilesInfo filesInfo) {
        paidProofMapper.update(new LambdaUpdateWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getFileId, filesInfo.getFileId()).set(DataDutyPaidProof::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        paidProofDetailsMapper.update(new LambdaUpdateWrapper<DataDutyPaidProofDetails>().eq(DataDutyPaidProofDetails::getFileId, filesInfo.getFileId()).set(DataDutyPaidProofDetails::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }
    // 删除滴滴行程单发票/电子行程单
    private void removeDidiItinerary(DataImageFilesInfo filesInfo) {
       didiItineraryMapper.update(new LambdaUpdateWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getFileId, filesInfo.getFileId()).set(DataDidiItinerary::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       didiItineraryDetailsMapper.update(new LambdaUpdateWrapper<DataDidiItineraryDetails>().eq(DataDidiItineraryDetails::getFileId, filesInfo.getFileId()).set(DataDidiItineraryDetails::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除小票
    private void removeReceipt(DataImageFilesInfo filesInfo) {
        dataReceiptMapper.update(new LambdaUpdateWrapper<DataReceipt>().eq(DataReceipt::getFileId, filesInfo.getFileId()).set(DataReceipt::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除过路费
    private void removeTollRoads(DataImageFilesInfo filesInfo) {
      tollRoadsMapper.update(new LambdaUpdateWrapper<DataTollRoads>().eq(DataTollRoads::getFileId, filesInfo.getFileId()).set(DataTollRoads::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
      filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除客车发票
    private void removePassengerCar(DataImageFilesInfo filesInfo) {
        passengerCarMapper.update(new LambdaUpdateWrapper<DataPassengerCar>().eq(DataPassengerCar::getFileId, filesInfo.getFileId()).set(DataPassengerCar::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除火车票
    private void removeRailwayTicket(DataImageFilesInfo filesInfo) {
       railwayTicketMapper.update(new LambdaUpdateWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getFileId, filesInfo.getFileId()).set(DataRailwayTicket::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除出租车发票
    private void removeTaxiTickets(DataImageFilesInfo filesInfo) {
      taxiTicketsMapper.update(new LambdaUpdateWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getFileId, filesInfo.getFileId()).set(DataTaxiTickets::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
      filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除定额发票
    private void removeQuotaInvoice(DataImageFilesInfo filesInfo) {
       quotaInvoiceMapper.update(new LambdaUpdateWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getFileId, filesInfo.getFileId()).set(DataQuotaInvoice::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除医疗发票
    private void removeMedicalTicket(DataImageFilesInfo filesInfo) {
       dataMedicalTreatmentMapper.update(new LambdaUpdateWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getFileId, filesInfo.getFileId()).set(DataMedicalTreatment::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       removeOcrDetail(filesInfo);
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }
    //船票
    private void removeSteamerTicket(DataImageFilesInfo filesInfo) {
        steamerTicketMapper.update(new LambdaUpdateWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getFileId, filesInfo.getFileId()).set(DataSteamerTicket::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除机票(航空电子客运)发票
    private void removeFilghtItinerary(DataImageFilesInfo filesInfo) {
       flightItineraryMapper.update(new LambdaUpdateWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getFileId, filesInfo.getFileId()).set(DataFlightItinerary::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       flightsItineraryDetailMapper.update(new LambdaUpdateWrapper<DataFlightsItineraryDetail>().eq(DataFlightsItineraryDetail::getFileId, filesInfo.getFileId()).set(DataFlightsItineraryDetail::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }
    //删除二手车发票
    private void removeUserCarSaleInvoice(DataImageFilesInfo filesInfo) {
       usedCarSalesMapper.update(new LambdaUpdateWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, filesInfo.getFileId()).set(DataUsedCarSales::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
    }
    //删除机动车销售发票
    private void removeVehicleSaleInvoice(DataImageFilesInfo filesInfo) {
       motorVehicleSaleMapper.update(new LambdaUpdateWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getFileId, filesInfo.getFileId()).set(DataMotorVehicleSale::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
       filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
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
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                // 处理增值税发票
                 return handleVatInvoice(generalInfo, request.getInvoiceType());
            // 数电票普通发票/机打发票
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
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
            //医疗票明细票/医疗票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                return updateMedicalTicket(generalInfo, details);
            //非税收入单据
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                return updateNonTaxRevenueReceipts(generalInfo,details);
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
                return updateDutyPaidProof(generalInfo,details);
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                return updateCustomsImportGoods(generalInfo, details);
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                return updateCustomsExportGoods(generalInfo, details);
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                return updateCustomsSpecialPayment(generalInfo,details);
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                return updateElectronicPaymentGoodsTransportation(generalInfo, details);
        }
        return 0;
    }
    //修改非税收入单据
    private int updateNonTaxRevenueReceipts(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataNonTax dataNonTax = BeanUtil.toBean(generalInfo, DataNonTax.class);
        int updateResult = dataNonTaxMapper.updateById(dataNonTax);
        if (updateResult <= 0) {
            return 0;
        }
        for (Map<String, Object> item : details) {
            DataOcrDetails ocrDetails = BeanUtil.toBean(item, DataOcrDetails.class);
            int i = ocrDetailsMapper.updateById(ocrDetails);
            if (i <= 0) {
                return 0;
            }
        }
        return 1;
    }

    //修改海关专用缴款书发票
    private int updateCustomsSpecialPayment(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataCustomsSpecialPayment customsSpecialPayment = BeanUtil.toBean(generalInfo, DataCustomsSpecialPayment.class);
        customsSpecialPaymentMapper.updateById(customsSpecialPayment);
        for (Map<String, Object> item : details) {
            DataOcrDetails ocrDetails = BeanUtil.toBean(item, DataOcrDetails.class);
            ocrDetailsMapper.updateById(ocrDetails);
        }
        return 1;
    }
    //修改海关出口货物报关单发票
    private int updateCustomsExportGoods(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataCustomsExportGoods customsExportGoods = BeanUtil.toBean(generalInfo, DataCustomsExportGoods.class);
        customsExportGoodsMapper.updateById(customsExportGoods);
        // 将细节数据转换为实体对象
        List<DataCustomsExportGoodsDetail> customsExportGoodsDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataCustomsExportGoodsDetail.class))
            .toList();
        for (DataCustomsExportGoodsDetail item : customsExportGoodsDetails) {
            customsExportGoodsDetailMapper.updateById(item);
        }
        return 1;
    }

    //修改海关进口货物报关单发票
    private int updateCustomsImportGoods(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        // 将主表数据转换为实体对象
        DataCustomsImxportGoods customsImxportGoods = BeanUtil.toBean(generalInfo, DataCustomsImxportGoods.class);
        // 更新主表信息，并检查是否成功
       customsImportGoodsMapper.updateById(customsImxportGoods);
        List<DataCustomsImportGoodsDetail> customsImportGoodsDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataCustomsImportGoodsDetail.class))
            .toList();
        for (DataCustomsImportGoodsDetail item : customsImportGoodsDetails) {
            customsImportGoodsDetailMapper.updateById(item);

        }
        return 1;
    }
    //修改出行发票/滴滴
    private int updateDidiItinerary(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataDidiItinerary didiItinerary = BeanUtil.toBean(generalInfo, DataDidiItinerary.class);
        didiItineraryMapper.updateById(didiItinerary);
        List<DataDidiItineraryDetails> didiItineraryDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataDidiItineraryDetails.class))
            .toList();
        for (DataDidiItineraryDetails item : didiItineraryDetails) {
           didiItineraryDetailsMapper.updateById(item);
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
        int count=filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, flightItinerary.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES));
        if (count<=0){
            return 0;
        }
        return 1;
    }

    //处理数电票/普通发票修改
    private int updateOrdinaryInvoice(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataOcrInfo ocrInfo = BeanUtil.toBean(generalInfo, DataOcrInfo.class);
        ocrInfoMapper.updateById(ocrInfo);
        List<DataOcrDetails> ocrDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataOcrDetails.class))
            .toList();

        for (DataOcrDetails item : ocrDetails) {
            ocrDetailsMapper.updateById(item);
        }
        //根据file_id修改图片表信息
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, ocrInfo.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }

    //货物运输电子收款凭证发票发票修改
    private int updateElectronicPaymentGoodsTransportation(Map<String, Object> generalInfo, List<Map<String, Object>> details) {
        DataElectronicTransportationGoods transportationGoods = BeanUtil.toBean(generalInfo, DataElectronicTransportationGoods.class);
        int updateResult =paymentMapper.updateById(transportationGoods);
        if (updateResult <= 0){
            return 0;
        }
        List<DataOcrDetails> ocrDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataOcrDetails.class))
            .toList();
        for (DataOcrDetails item : ocrDetails) {
            ocrDetailsMapper.updateById(item);
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
        ocrInfoMapper.update(dataOcrInfo, new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()));
        for (DataOcrDetails detail : arrayList) {
           ocrDetailsMapper.update(detail, new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()).eq(DataOcrDetails::getId, detail.getId()));
        }
        return 1;
    }
    //修改机动车销售发票
    private int updateVehicleSaleInvoice (Map < String, Object > generalInfo){
        DataMotorVehicleSale motorVehicleSale = BeanUtil.toBean(generalInfo, DataMotorVehicleSale.class);
        motorVehicleSaleMapper.updateById(motorVehicleSale);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, motorVehicleSale.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改二手车销售发票
    private int updateCarSaleInvoice (Map < String, Object > generalInfo){
        DataUsedCarSales usedCarSales = BeanUtil.toBean(generalInfo, DataUsedCarSales.class);
        usedCarSalesMapper.updateById(usedCarSales);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, usedCarSales.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改船票
    private int updateSteamerTicket (Map < String, Object > generalInfo){
        DataSteamerTicket steamerTicket = BeanUtil.toBean(generalInfo, DataSteamerTicket.class);
        steamerTicketMapper.updateById(steamerTicket);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, steamerTicket.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改定额发票
    private int updateQuotaInvoice (Map < String, Object > generalInfo){
        DataQuotaInvoice quotaInvoice = BeanUtil.toBean(generalInfo, DataQuotaInvoice.class);
        quotaInvoiceMapper.updateById(quotaInvoice);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, quotaInvoice.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改出租车票
    private int updateTaxiTickets (Map < String, Object > generalInfo){
        DataTaxiTickets taxiTickets = BeanUtil.toBean(generalInfo, DataTaxiTickets.class);
        taxiTicketsMapper.updateById(taxiTickets);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, taxiTickets.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改火车票
    private int updateRailwayTicket (Map < String, Object > generalInfo){
        DataRailwayTicket railwayTicket = BeanUtil.toBean(generalInfo, DataRailwayTicket.class);
        railwayTicketMapper.updateById(railwayTicket);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, railwayTicket.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改汽车票
    private int updatePassengerCar (Map < String, Object > generalInfo){
        DataPassengerCar passengerCar = BeanUtil.toBean(generalInfo, DataPassengerCar.class);
        passengerCarMapper.updateById(passengerCar);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, passengerCar.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改过路费
    private int updateTollRoads (Map < String, Object > generalInfo){
        DataTollRoads tollRoads = BeanUtil.toBean(generalInfo, DataTollRoads.class);
        tollRoadsMapper.updateById(tollRoads);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, tollRoads.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改小票
    private int updateReceipt (Map < String, Object > generalInfo){
        DataReceipt receipt = BeanUtil.toBean(generalInfo, DataReceipt.class);
        dataReceiptMapper.updateById(receipt);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, receipt.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }
    //修改完税证明
    private int updateDutyPaidProof (Map < String, Object > generalInfo, List<Map<String, Object>> details){
        DataDutyPaidProof dutyPaidProof = BeanUtil.toBean(generalInfo, DataDutyPaidProof.class);
        paidProofMapper.updateById(dutyPaidProof);
        List<DataDutyPaidProofDetails> ocrDetails = details.stream()
            .map(item -> BeanUtil.toBean(item, DataDutyPaidProofDetails.class))
            .toList();

        for (DataDutyPaidProofDetails item : ocrDetails) {
            paidProofDetailsMapper.updateById(item);
        }
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, dutyPaidProof.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getCode()));
        return 1;
    }

    /**
     *发票夹列表信息(待报销,已报销,未报销)
     */
    @Override
    public Page<InvoiceVo> getInvoicePage(InvoicePageQuery pageQuery) throws ExecutionException, InterruptedException {
        Page<InvoiceVo> invoiceVoPage = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        LambdaQueryWrapper<DataImageFilesInfo> eq = new LambdaQueryWrapper<DataImageFilesInfo>()
            .eq(DataImageFilesInfo::getCreateBy, pageQuery.getUserId())
            .eq(DataImageFilesInfo::getFileFlowStatus, pageQuery.getStatus());
        List<DataImageFilesInfo> dataImageFilesInfos = filesInfoMapper.selectList(eq);

        if (CollectionUtil.isEmpty(dataImageFilesInfos)) {
            return invoiceVoPage;
        }
        List<InvoiceVo> invoiceVosList = new ArrayList<>();
        int coreCount = Runtime.getRuntime().availableProcessors();
        // 核心线程数设置为 CPU 核心数的 2 倍
        int corePoolSize = coreCount * 2;
        // 最大线程数设置为 CPU 核心数的 4 倍
        int maximumPoolSize = coreCount * 4;
        // 空闲线程存活时间设置为 60 秒
        long keepAliveTime = 60L;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            workQueue);
        List<Callable<List<InvoiceVo>>> tasks = new ArrayList<>();
        tasks.add(() -> transitionOcrInfo(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionCarSaleInvoice(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionSecondCarSaleInvoice(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionAirTicket(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionShipTicket(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionMedicalTicket(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionQuotaInvoice(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionTaxiTickets(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionRailwayTicket(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionPassengerCar(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionTollRoads(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionReceipt(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionTravelInvoice(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionDutyPaidProof(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionCustomsSpecialPayment(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionElectronicTransportationGoods(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionCustomsExportGoods(dataImageFilesInfos, pageQuery.getUserId()));
        tasks.add(() -> transitionCustomsImportGoods(dataImageFilesInfos, pageQuery.getUserId()));
        CompletableFuture<List<InvoiceVo>>[] futures;
        futures = new CompletableFuture[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Callable<List<InvoiceVo>> callable = tasks.get(i);
            Supplier<List<InvoiceVo>> supplier = () -> {
                try {
                    return callable.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            futures[i] = CompletableFuture.supplyAsync(supplier, threadPoolExecutor);
        }
        // 使用allOf等待所有任务完成
        CompletableFuture.allOf(futures).join();
        // 收集所有任务的结果
        for (CompletableFuture<List<InvoiceVo>> future : futures) {
            invoiceVosList.addAll(future.get());
        }
        invoiceVoPage.setRecords(invoiceVosList);
        invoiceVoPage.setTotal(invoiceVosList.size());
        // 计算总计金额
        BigDecimal totalAmount = invoiceVosList.stream()
            .map(InvoiceVo::getMoneyAsBigDecimal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 将BigDecimal类型的totalAmount转换为String类型后赋值给grossAmount
        String totalAmountStr = totalAmount.toString();
        invoiceVosList.forEach(invoiceVo -> invoiceVo.setGrossAmount(totalAmountStr));
        // 进行分页处理
        int startIndex =((pageQuery.getPageNum() - 1) * pageQuery.getPageSize());
        int endIndex = Math.min(startIndex +pageQuery.getPageSize(), invoiceVosList.size());
        List<InvoiceVo> pageData = invoiceVosList.subList(startIndex, endIndex);
        invoiceVoPage.setRecords(pageData);
        invoiceVoPage.setTotal(invoiceVosList.size());
        // 关闭线程池
        threadPoolExecutor.shutdown();
        return invoiceVoPage;
    }
    //查询海关进口货物组成InvoiceVo
    private List<InvoiceVo> transitionCustomsImportGoods(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> customsImportGoodsList;
        customsImportGoodsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataCustomsImxportGoods> customsImportGoods = customsImportGoodsMapper.selectList(new LambdaQueryWrapper<DataCustomsImxportGoods>()
                    .eq(DataCustomsImxportGoods::getFileId, dataImageFilesInfo.getFileId()).eq(DataCustomsImxportGoods::getCreateBy, userId));
                return customsImportGoods.stream().map(
                    dataCustomsImportGoods -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(dataCustomsImportGoods, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(dataCustomsImportGoods.getFreight());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    });
            }
        ).toList();
        return customsImportGoodsList;
    }

    //查询海关出口货物组成InvoiceVo
    private List<InvoiceVo> transitionCustomsExportGoods(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> customsExportGoodsList;
        customsExportGoodsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataCustomsExportGoods> customsExportGoods = customsExportGoodsMapper.selectList(new LambdaQueryWrapper<DataCustomsExportGoods>()
                    .eq(DataCustomsExportGoods::getFileId, dataImageFilesInfo.getFileId()).eq(DataCustomsExportGoods::getCreateBy, userId));
                return customsExportGoods.stream().map(
                    dataCustomsExportGoods -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(dataCustomsExportGoods, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(dataCustomsExportGoods.getFreight());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    });
            }
        ).toList();
        return customsExportGoodsList;
    }

    //查询货物运输电子收款凭证发票组成InvoiceVo
    private List<InvoiceVo> transitionElectronicTransportationGoods(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> electronicTransportationGoodsList;
        electronicTransportationGoodsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataElectronicTransportationGoods> dataElectronicTransportationGoods = paymentMapper.selectList(new LambdaQueryWrapper<DataElectronicTransportationGoods>()
                    .eq(DataElectronicTransportationGoods::getFileId, dataImageFilesInfo.getFileId()).eq(DataElectronicTransportationGoods::getCreateBy, userId));
                return dataElectronicTransportationGoods.stream().map(
                    dataElectronicTransportationGoods1 -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(dataElectronicTransportationGoods1, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(dataElectronicTransportationGoods1.getTotalPrice());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    });
            }).toList();
        return electronicTransportationGoodsList;
    }

    //海关专用缴款书发票组成InvoiceVo
    private List<InvoiceVo> transitionCustomsSpecialPayment(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> customsSpecialPaymentList;
        customsSpecialPaymentList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataCustomsSpecialPayment> dataCustomsSpecialPayment = customsSpecialPaymentMapper.selectList(new LambdaQueryWrapper<DataCustomsSpecialPayment>()
                    .eq(DataCustomsSpecialPayment::getFileId, dataImageFilesInfo.getFileId()).eq(DataCustomsSpecialPayment::getCreateBy, userId));
                return dataCustomsSpecialPayment.stream().map(
                    dataCustomsSpecialPayments -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(dataCustomsSpecialPayments, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(dataCustomsSpecialPayments.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    });
            }).toList();
        return customsSpecialPaymentList;
    }

    //查询完税证明发票组成InvoiceVo
    private List<InvoiceVo> transitionDutyPaidProof(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> dutyPaidProofList;
        dutyPaidProofList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataDutyPaidProof> dataDutyPaidProof = paidProofMapper.selectList(new LambdaQueryWrapper<DataDutyPaidProof>()
                    .eq(DataDutyPaidProof::getFileId, dataImageFilesInfo.getFileId()).eq(DataDutyPaidProof::getCreateBy, userId));
                return dataDutyPaidProof.stream().map(
                    dataDutyPaidProofs -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(dataDutyPaidProofs, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(dataDutyPaidProofs.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    }
                );
            }
        ).toList();
        return dutyPaidProofList;
    }

    //查询出行发票滴滴组成InvoiceVo
    private List<InvoiceVo> transitionTravelInvoice(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> travelInvoiceList;
        travelInvoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataDidiItinerary> dataDidiItinerary = didiItineraryMapper.selectList(new LambdaQueryWrapper<DataDidiItinerary>()
                    .eq(DataDidiItinerary::getFileId, dataImageFilesInfo.getFileId()).eq(DataDidiItinerary::getCreateBy, userId));
                return dataDidiItinerary.stream().map(
                    dataDidiItinerars -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(dataDidiItinerars, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(dataDidiItinerars.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    });
            }).toList();
        return travelInvoiceList;
    }

    //查询小票组成InvoiceVo
    private List<InvoiceVo> transitionReceipt(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> receiptList;
        receiptList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataReceipt> receipt = dataReceiptMapper.selectList(new LambdaQueryWrapper<DataReceipt>()
                    .eq(DataReceipt::getFileId, dataImageFilesInfo.getFileId()).eq(DataReceipt::getCreateBy, userId));
                return receipt.stream().map(
                    receipts -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(receipts, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(receipts.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    }
                );
            }
        ).toList();
        return receiptList;
    }

    //查询过路费发票组成InvoiceVo
    private List<InvoiceVo> transitionTollRoads(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> tollRoadsList;
        tollRoadsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataTollRoads> tollRoads = tollRoadsMapper.selectList(new LambdaQueryWrapper<DataTollRoads>()
                    .eq(DataTollRoads::getFileId, dataImageFilesInfo.getFileId()).eq(DataTollRoads::getCreateBy, userId));
                return tollRoads.stream().map(
                    tollRoad -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(tollRoad, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(tollRoad.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    }
                );
            }
        ).toList();
        return tollRoadsList;
    }

    //查询客运车票组成InvoiceVo
    private List<InvoiceVo> transitionPassengerCar(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> passengerCarsList;
        passengerCarsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataPassengerCar> passengerCars = passengerCarMapper.selectList(new LambdaQueryWrapper<DataPassengerCar>()
                    .eq(DataPassengerCar::getFileId, dataImageFilesInfo.getFileId()).eq(DataPassengerCar::getCreateBy, userId));
                return passengerCars.stream().map(
                    passengerCar -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(passengerCar, InvoiceVo.class);
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(passengerCar.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    }
                );
            }
        ).toList();
        return passengerCarsList;
    }

    //查询火车票组成InvoiceVo
    private List<InvoiceVo> transitionRailwayTicket(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> railwayTicketsList;
        railwayTicketsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataRailwayTicket> railwayTickets = railwayTicketMapper.selectList(new LambdaQueryWrapper<DataRailwayTicket>()
                    .eq(DataRailwayTicket::getFileId, dataImageFilesInfo.getFileId()).eq(DataRailwayTicket::getCreateBy, userId));
                return railwayTickets.stream().map(
                    railwayTicket -> {
                        InvoiceVo invoiceVo = BeanUtil.toBean(railwayTicket, InvoiceVo.class);
                        invoiceVo.setBuyerName(railwayTicket.getBuyer());
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setMoney(railwayTicket.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        return invoiceVo;
                    });
            }).toList();
        return railwayTicketsList;
    }
    //查询出租车票组成InvoiceVo
    private List<InvoiceVo> transitionTaxiTickets(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> taxiTicketsList;
        taxiTicketsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataTaxiTickets> taxiTickets = taxiTicketsMapper.selectList(new LambdaQueryWrapper<DataTaxiTickets>()
                    .eq(DataTaxiTickets::getFileId, dataImageFilesInfo.getFileId()).eq(DataTaxiTickets::getCreateBy, userId));
                return taxiTickets.stream().map(taxiTicket -> {
                    InvoiceVo invoiceVo = BeanUtil.toBean(taxiTicket, InvoiceVo.class);
                    invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setMoney(taxiTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    return invoiceVo;
                });
            }
        ).toList();
        return taxiTicketsList;
    }

    //查询定额发票组成InvoiceVo
    private List<InvoiceVo> transitionQuotaInvoice(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataQuotaInvoice> dataQuotaInvoices = quotaInvoiceMapper.selectList(new LambdaQueryWrapper<DataQuotaInvoice>()
                    .eq(DataQuotaInvoice::getFileId, dataImageFilesInfo.getFileId()).eq(DataQuotaInvoice::getCreateBy, userId));
                return dataQuotaInvoices.stream().map(dataQuotaInvoice -> {
                    InvoiceVo invoiceVo = BeanUtil.toBean(dataQuotaInvoice, InvoiceVo.class);
                    invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setMoney(dataQuotaInvoice.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    return invoiceVo;
                });
            }
        ).toList();
        return invoiceList;
    }

    //查询医疗票/非税票组成InvoiceVo
    private List<InvoiceVo> transitionMedicalTicket(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataMedicalTreatment> dataMedicalTickets = dataMedicalTreatmentMapper.selectList(new LambdaQueryWrapper<DataMedicalTreatment>()
                    .eq(DataMedicalTreatment::getFileId, dataImageFilesInfo.getFileId()).eq(DataMedicalTreatment::getCreateBy, userId));
                return dataMedicalTickets.stream().map(dataMedicalTicket -> {
                    InvoiceVo invoiceVo = BeanUtil.toBean(dataMedicalTicket, InvoiceVo.class);
                    invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setMoney(dataMedicalTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    return invoiceVo;
                });
            }
        ).toList();
        return invoiceList;
    }

    //查询船票并组装成InvoiceVo
    private List<InvoiceVo> transitionShipTicket(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataSteamerTicket> dataShipTickets = steamerTicketMapper.selectList(new LambdaQueryWrapper<DataSteamerTicket>()
                    .eq(DataSteamerTicket::getFileId, dataImageFilesInfo.getFileId()).eq(DataSteamerTicket::getCreateBy, userId));
                return dataShipTickets.stream().map(dataShipTicket -> {
                    InvoiceVo invoiceVo = BeanUtil.toBean(dataShipTicket, InvoiceVo.class);
                    invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setMoney(dataShipTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    return invoiceVo;
                });
            }
        ).toList();
        return invoiceList;
    }

    //查询航空电子客运单票信息并组装成InvoiceVo
    private List<InvoiceVo> transitionAirTicket(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataFlightItinerary> dataAirTickets = flightItineraryMapper.selectList(new LambdaQueryWrapper<DataFlightItinerary>()
                    .eq(DataFlightItinerary::getFileId, dataImageFilesInfo.getFileId()).eq(DataFlightItinerary::getCreateBy, userId));
                return dataAirTickets.stream().map(dataAirTicket -> {
                    InvoiceVo invoiceVo = BeanUtil.toBean(dataAirTicket, InvoiceVo.class);
                    invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setMoney(dataAirTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    return invoiceVo;
                });
            }).toList();
        return invoiceList;
    }

    //查询二手车销售发票信息并组装成InvoiceVo
    private List<InvoiceVo> transitionSecondCarSaleInvoice(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataUsedCarSales> dataSecondCarSaleInvoices = usedCarSalesMapper.selectList(new LambdaQueryWrapper<DataUsedCarSales>()
                    .eq(DataUsedCarSales::getFileId, dataImageFilesInfo.getFileId()).eq(DataUsedCarSales::getCreateBy, userId));
                return dataSecondCarSaleInvoices.stream().map(dataSecondCarSaleInvoice -> {
                    InvoiceVo invoiceVo = BeanUtil.toBean(dataSecondCarSaleInvoice, InvoiceVo.class);
                    invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setMoney(dataSecondCarSaleInvoice.getInvoiceTotal());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    return invoiceVo;
                });

            }).toList();
        return invoiceList;
    }

    //查询机动车销售发票信息并组装成InvoiceVo
    public List<InvoiceVo> transitionCarSaleInvoice(List<DataImageFilesInfo> dataImageFilesInfos,Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataMotorVehicleSale> dataCarSaleInvoices = motorVehicleSaleMapper.selectList(new LambdaQueryWrapper<DataMotorVehicleSale>()
                    .eq(DataMotorVehicleSale::getFileId, dataImageFilesInfo.getFileId()).eq(DataMotorVehicleSale::getCreateBy, userId));
                return dataCarSaleInvoices.stream().map(dataCarSaleInvoice -> {
                    InvoiceVo invoiceVo = BeanUtil.toBean(dataCarSaleInvoice, InvoiceVo.class);
                    invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setMoney(dataCarSaleInvoice.getInvoiceTotal());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    return invoiceVo;
                });
        }).toList();
        return invoiceList;
    }
    //根据fileId查询ocr信息并组装成InvoiceVo
    public List<InvoiceVo> transitionOcrInfo(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(dataImageFilesInfo -> {
            List<DataOcrInfo> dataOcrInfos = ocrInfoMapper.selectList(new LambdaQueryWrapper<DataOcrInfo>()
                .eq(DataOcrInfo::getFileId, dataImageFilesInfo.getFileId()).eq(DataOcrInfo::getCreateBy, userId));
            return dataOcrInfos.stream().map(dataOcrInfo -> {
                InvoiceVo invoiceVo = BeanUtil.toBean(dataOcrInfo, InvoiceVo.class);
                invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                invoiceVo.setMoney(dataOcrInfo.getTotalLowercase());
                invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                return invoiceVo;
            });
        }).toList();
        return invoiceList;
    }
    /*
    * 发票详情信息查询
     */
    @Override
    public R<DataResponseDTO> selectInvoiceDetail(String fileId) {
        //根据fileId查询发票信息
        DataImageFilesInfo res = filesInfoMapper.selectOne(new LambdaQueryWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, fileId));
        if (ObjectUtil.isEmpty(res)){
            return R.fail();
        }
            if (!res.getFileId().isEmpty()) {
                String invoiceType = res.getInvoice();
                Object info = null;
                Object detailInfo = null;
                switch (invoiceType) {
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
                        info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                        detailInfo=ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //机动车
                    case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                        info=motorVehicleSaleMapper.selectOne(new LambdaQueryWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //航空运输电子客票行程单
                    case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                        info=flightItineraryMapper.selectOne(new LambdaQueryWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getFileId, fileId));
                        detailInfo=flightsItineraryDetailMapper.selectList(new LambdaQueryWrapper<DataFlightsItineraryDetail>().eq(DataFlightsItineraryDetail::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //二手车
                    case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                        info=usedCarSalesMapper.selectOne(new LambdaQueryWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //船票
                    case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                        info=steamerTicketMapper.selectOne(new LambdaQueryWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //医疗票明细票
                    case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
                    case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                        info=dataMedicalTreatmentMapper.selectOne(new LambdaQueryWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getFileId, fileId));
                        detailInfo=ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //非税收入类发票
                    case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                        info=dataNonTaxMapper.selectOne(new LambdaQueryWrapper<DataNonTax>().eq(DataNonTax::getFileId, fileId));
                        detailInfo=ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //定额发票
                    case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                        info=quotaInvoiceMapper.selectOne(new LambdaQueryWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //出租车发票
                    case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                        info=taxiTicketsMapper.selectOne(new LambdaQueryWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //火车发票
                    case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                        info=railwayTicketMapper.selectOne(new LambdaQueryWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //客运车发票
                    case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                        info=passengerCarMapper.selectOne(new LambdaQueryWrapper<DataPassengerCar>().eq(DataPassengerCar::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //过路费发票
                    case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                        info=tollRoadsMapper.selectOne(new LambdaQueryWrapper<DataTollRoads>().eq(DataTollRoads::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //小票
                    case InvoiceConstants.GLORITY_RECEIPT_CODE:
                        info=dataReceiptMapper.selectOne(new LambdaQueryWrapper<DataReceipt>().eq(DataReceipt::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, null));
                    //出行发票/滴滴
                    case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                        info=didiItineraryMapper.selectOne(new LambdaQueryWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getFileId, fileId));
                        detailInfo=didiItineraryDetailsMapper.selectList(new LambdaQueryWrapper<DataDidiItineraryDetails>().eq(DataDidiItineraryDetails::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //完税证明发票
                    case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                        info=paidProofMapper.selectOne(new LambdaQueryWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getFileId, fileId));
                        detailInfo=paidProofDetailsMapper.selectList(new LambdaQueryWrapper<DataDutyPaidProofDetails>().eq(DataDutyPaidProofDetails::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //海关进口货物报关单发票
                    case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                        info=customsImportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getFileId, fileId));
                        detailInfo=customsExportGoodsDetailMapper.selectList(new LambdaQueryWrapper<DataCustomsExportGoodsDetail>().eq(DataCustomsExportGoodsDetail::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //海关出口货物报关单发票
                    case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                        info=customsExportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getFileId, fileId));
                        detailInfo=customsExportGoodsDetailMapper.selectList(new LambdaQueryWrapper<DataCustomsExportGoodsDetail>().eq(DataCustomsExportGoodsDetail::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //海关专用缴款书发票
                    case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                        info=customsSpecialPaymentMapper.selectOne(new LambdaQueryWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getFileId, fileId));
                        detailInfo=ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    //货物运输电子收款凭证发票
                    case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                        info=paymentMapper.selectOne(new LambdaQueryWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getFileId, fileId));
                        detailInfo=ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                        return R.ok(new DataResponseDTO(res, info, detailInfo));
                    default: {
                        return R.fail();
                    }
                }
            }
        return R.fail();
    }
}
