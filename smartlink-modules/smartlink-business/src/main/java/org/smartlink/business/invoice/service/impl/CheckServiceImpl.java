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
import org.smartlink.business.doman.vo.InvoiceVo;
import org.smartlink.business.doman.vo.InvoiceWriteBackVo;
import org.smartlink.business.enumd.CheckInvoiceStatusEnumd;
import org.smartlink.business.invoice.check.CheckInvoice;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.check.doman.InvoicePageQuery;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.FileStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.*;
import org.smartlink.common.entity.domain.business.response.DataResponseDTO;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public R<Void> deleteWithValidByIds(Collection<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return R.fail(500, "请选择需要删除的数据");
        }
        //循环遍历查询图片表信息数据
        for (String id : ids) {
            DataImageFilesInfo filesInfo = filesInfoMapper.selectOne(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, id));
            if (ObjUtil.isEmpty(filesInfo)) {
                return R.fail(500, "图片表信息不存在");
            }
            String invoiceType = filesInfo.getInvoice();
            if (StrUtil.isEmpty(invoiceType)) {
                if (filesInfo.getFileStatus().equals(FileStatusEnumd.OCR_FAILED.getCode())) {
                    //修改文件状态为已删除
                    int a = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileStatus, ""));
                    if (a <= 0) {
                        return R.fail(500, "删除失败");
                    }
                    return R.ok();
                }
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
                //小票/可报销其他发票
                case InvoiceConstants.GLORITY_RECEIPT_CODE:
                case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
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
                case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                    removeOcrInvoice(filesInfo);
                    break;
                default: {
                    return R.fail(500, "发票类型错误");
                }
            }
        }
        return R.ok();
    }

    private void removeOcrDetail(DataImageFilesInfo filesInfo) {
        ocrDetailsMapper.update(new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()).set(DataOcrDetails::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
    }

    //删除非税发票
    private void removeNonTaxInvoice(DataImageFilesInfo filesInfo) {
        dataNonTaxMapper.update(new LambdaUpdateWrapper<DataNonTax>().eq(DataNonTax::getFileId, filesInfo.getFileId()).set(DataNonTax::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        removeOcrDetail(filesInfo);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
    }

    //删除ocr发票
    private void removeOcrInvoice(DataImageFilesInfo filesInfo) {
        ocrInfoMapper.update(new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()).set(DataOcrInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()));
        removeOcrDetail(filesInfo);
        filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, filesInfo.getFileId()).set(DataImageFilesInfo::getDeleteFlag, FileStatusEnumd.DELETED.getCode()).set(DataImageFilesInfo::getFileFlowStatus, FileStatusEnumd.DELETED.getCode()));
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
    public R<Void> invoiceAlter(InvoiceRequest request) throws Exception {
        String invoiceType = request.getInvoiceType();
        if (StrUtil.isEmpty(invoiceType)) {
            return R.fail(500, "发票类型不能为空");
        }
        log.info("修改发票类型：{}", invoiceType);
        Map<String, Object> generalInfo = request.getGeneralInfo();
        log.info("修改传入的基本发票信息：{}", generalInfo);
        // BillRequest billRequest = request.getBillRequest();
//        if (ObjUtil.isEmpty(billRequest)){
//            return R.fail(500, "发票明细对象不能为空");
//        }
//        List<Map<String, Object>> details = billRequest.getDetails();
//        log.info("修改传入的发票明细信息：{}", details);
        // 根据发票类型处理逻辑
        switch (invoiceType) {
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                // 处理增值税发票
                return updateOcrInvoice(generalInfo, request.getInvoiceType());
            //机打发票/增值税发票清单
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
                return updateOrdinaryInvoice(generalInfo);
            //机动车销售发票
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                return updateVehicleSaleInvoice(generalInfo);
            //二手车发票
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                return updateCarSaleInvoice(generalInfo);
            //航空电子客运单
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                return updateFilghtItinerary(generalInfo);
            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                return updateSteamerTicket(generalInfo);
            //医疗票明细票/医疗票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                return updateMedicalTicket(generalInfo);
            //非税收入单据
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                return updateNonTaxRevenueReceipts(generalInfo);
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
            //小票/可报销其他发票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                return updateReceipt(generalInfo);
            //出行发票/滴滴
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                return updateDidiItinerary(generalInfo);
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                return updateDutyPaidProof(generalInfo);
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                return updateCustomsImportGoods(generalInfo);
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                return updateCustomsExportGoods(generalInfo);
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                return updateCustomsSpecialPayment(generalInfo);
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                return updateElectronicPaymentGoodsTransportation(generalInfo);
        }
        return R.fail(500, "发票类型错误");
    }

    //修改非税收入单据
    private R<Void> updateNonTaxRevenueReceipts(Map<String, Object> generalInfo) {
        DataNonTax dataNonTax = BeanUtil.toBean(generalInfo, DataNonTax.class);
        int updateResult = dataNonTaxMapper.updateById(dataNonTax);
        if (updateResult <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        for (Map<String, Object> item : details) {
//            DataOcrDetails ocrDetails = BeanUtil.toBean(item, DataOcrDetails.class);
//            int i = ocrDetailsMapper.updateById(ocrDetails);
//            if (i <= 0) {
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int counts = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, dataNonTax.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改海关专用缴款书发票
    private R<Void> updateCustomsSpecialPayment(Map<String, Object> generalInfo) {
        DataCustomsSpecialPayment customsSpecialPayment = BeanUtil.toBean(generalInfo, DataCustomsSpecialPayment.class);
        int counts = customsSpecialPaymentMapper.updateById(customsSpecialPayment);
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        for (Map<String, Object> item : details) {
//            DataOcrDetails ocrDetails = BeanUtil.toBean(item, DataOcrDetails.class);
//            int cc=ocrDetailsMapper.updateById(ocrDetails);
//            if (cc<=0){
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int count = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, customsSpecialPayment.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (count <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改海关出口货物报关单发票
    private R<Void> updateCustomsExportGoods(Map<String, Object> generalInfo) {
        DataCustomsExportGoods customsExportGoods = BeanUtil.toBean(generalInfo, DataCustomsExportGoods.class);
        int b = customsExportGoodsMapper.updateById(customsExportGoods);
        if (b <= 0) {
            return R.fail(500, "修改发票失败");
        }
        // 将细节数据转换为实体对象
//        List<DataCustomsExportGoodsDetail> customsExportGoodsDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataCustomsExportGoodsDetail.class))
//            .toList();
//        for (DataCustomsExportGoodsDetail item : customsExportGoodsDetails) {
//            int num=customsExportGoodsDetailMapper.updateById(item);
//            if (num<=0){
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int counts = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, customsExportGoods.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改海关进口货物报关单发票
    private R<Void> updateCustomsImportGoods(Map<String, Object> generalInfo) {
        // 将主表数据转换为实体对象
        DataCustomsImxportGoods customsImxportGoods = BeanUtil.toBean(generalInfo, DataCustomsImxportGoods.class);
        // 更新主表信息，并检查是否成功
        int a = customsImportGoodsMapper.updateById(customsImxportGoods);
        if (a <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        List<DataCustomsImportGoodsDetail> customsImportGoodsDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataCustomsImportGoodsDetail.class))
//            .toList();
//        for (DataCustomsImportGoodsDetail item : customsImportGoodsDetails) {
//            int num=customsImportGoodsDetailMapper.updateById(item);
//            if (num<=0){
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int counts = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, customsImxportGoods.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改出行发票/滴滴
    private R<Void> updateDidiItinerary(Map<String, Object> generalInfo) {
        DataDidiItinerary didiItinerary = BeanUtil.toBean(generalInfo, DataDidiItinerary.class);
        int c = didiItineraryMapper.updateById(didiItinerary);
        if (c <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        List<DataDidiItineraryDetails> didiItineraryDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataDidiItineraryDetails.class))
//            .toList();
//        for (DataDidiItineraryDetails item : didiItineraryDetails) {
//           int count=didiItineraryDetailsMapper.updateById(item);
//           if (count<=0){
//               return R.fail(500,"修改发票失败");
//           }
//        }
        int counts = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, didiItinerary.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改医疗票
    private R<Void> updateMedicalTicket(Map<String, Object> generalInfo) {
        DataMedicalTreatment medicalTreatment = BeanUtil.toBean(generalInfo, DataMedicalTreatment.class);
        int updateResult = dataMedicalTreatmentMapper.updateById(medicalTreatment);
        if (updateResult <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        List<DataOcrDetails> ocrDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataOcrDetails.class))
//            .toList();
//        for (DataOcrDetails item : ocrDetails) {
//            int i = ocrDetailsMapper.updateById(item);
//            if (i <= 0) {
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int counts = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, medicalTreatment.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改航空电子客运发票
    private R<Void> updateFilghtItinerary(Map<String, Object> generalInfo) {
        DataFlightItinerary flightItinerary = BeanUtil.toBean(generalInfo, DataFlightItinerary.class);
        int updateResult = flightItineraryMapper.updateById(flightItinerary);
        if (updateResult <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        List<DataFlightsItineraryDetail> flightItineraryDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataFlightsItineraryDetail.class))
//            .toList();
//        for (DataFlightsItineraryDetail item : flightItineraryDetails) {
//            int i = flightsItineraryDetailMapper.updateById(item);
//            if (i <= 0) {
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int count = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, flightItinerary.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (count <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改增值税发票
    private R<Void> updateOcrInvoice(Map<String, Object> generalInfo, String invoiceType) throws Exception {
        DataOcrInfo ocrInfo = BeanUtil.toBean(generalInfo, DataOcrInfo.class);
        //根据file_id查询ocr信息
        DataOcrInfo ocrInfos = ocrInfoMapper.selectOne(new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, ocrInfo.getFileId()));
        ocrInfo.setVersion(ocrInfos.getVersion());
        int updateResult = ocrInfoMapper.updateById(ocrInfo);
        if (updateResult <= 0) {
            return R.fail(500, "发票修改失败");
        }
//        List<DataOcrDetails> ocrDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataOcrDetails.class))
//            .toList();
//
//        for (DataOcrDetails item : ocrDetails) {
//            int i =ocrDetailsMapper.updateById(item);
//            if (i<=0){
//                return R.fail(500,"发票修改失败");
//            }
//        }
        //根据file_id修改图片表信息
        int count = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, ocrInfo.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (count <= 0) {
            return R.fail(500, "发票修改失败");
        }
        //处理调用查验方法
        return handleVatInvoice(generalInfo, invoiceType);
    }

    //处理数电票/普通发票修改
    private R<Void> updateOrdinaryInvoice(Map<String, Object> generalInfo) {
        DataOcrInfo ocrInfo = BeanUtil.toBean(generalInfo, DataOcrInfo.class);
        int updateResult = ocrInfoMapper.updateById(ocrInfo);
        if (updateResult <= 0) {
            return R.fail(500, "发票修改失败");
        }
//        List<DataOcrDetails> ocrDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataOcrDetails.class))
//            .toList();
//
//        for (DataOcrDetails item : ocrDetails) {
//            int i =ocrDetailsMapper.updateById(item);
//            if (i<=0){
//                return R.fail(500,"发票修改失败");
//            }
//        }
        //根据file_id修改图片表信息
        int count = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, ocrInfo.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (count <= 0) {
            return R.fail(500, "发票修改失败");
        }
        return R.ok();
    }

    //货物运输电子收款凭证发票发票修改
    private R<Void> updateElectronicPaymentGoodsTransportation(Map<String, Object> generalInfo) {
        DataElectronicTransportationGoods transportationGoods = BeanUtil.toBean(generalInfo, DataElectronicTransportationGoods.class);
        int updateResult = paymentMapper.updateById(transportationGoods);
        if (updateResult <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        List<DataOcrDetails> ocrDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataOcrDetails.class))
//            .toList();
//        for (DataOcrDetails item : ocrDetails) {
//            int a=ocrDetailsMapper.updateById(item);
//            if (a<=0){
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int counts = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, transportationGoods.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //处理增值税发票修改
    private R<Void> handleVatInvoice(Map<String, Object> generalInfo, String invoiceType) throws Exception {
        // DTO 初始化
        InvoiceCheckParamDTO dto = new InvoiceCheckParamDTO();
        // 增值税电子普通区块链
        if (StrUtil.isNotBlank((String) generalInfo.get("blockChain"))) {
            String blockChain = (String) generalInfo.get("blockChain");
            if (InvoiceConstants.ONE.equals(blockChain)) {
                handleGeneralInfo(dto, generalInfo, invoiceType);
                // 调用查验方法
                return validateInvoice(dto, generalInfo);
            }

        }
        // 数电票(增值税专用)
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
        //dto.setElectron_mark(Integer.valueOf((String) generalInfo.get("electronicMark")));
        Object value = generalInfo.get("electronicMark");
        String electronicMarkStr = value.toString();
        // 将字符串转换为 Integer 类型
        Integer electronMark = Integer.valueOf(electronicMarkStr);
        // 设置电子票标识
        dto.setElectron_mark(electronMark);
        //金额
        dto.setPretax_amount((String) generalInfo.get("pretaxAmount"));
    }

    // 增值税电子普通区块链
    private void handleGeneralInfo(InvoiceCheckParamDTO dto, Map<String, Object> generalInfo, String invoiceType) {
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
        if (StrUtil.isNotBlank((String) generalInfo.get("area"))) {
            dto.setArea((String) generalInfo.get("area"));
        }
        dto.setCode((String) generalInfo.get("invoiceCode"));
        dto.setNumber((String) generalInfo.get("invoiceNumber"));
        dto.setDate((String) generalInfo.get("invoiceDate"));
        dto.setType(invoiceType);
        // 校验码截取（后 6 位）
        String checkCodes = (String) generalInfo.get("checkCode");
        if (StringUtils.hasText(checkCodes) && checkCodes.length() > 5) {
            checkCodes = checkCodes.substring(checkCodes.length() - 6);
            dto.setCheck_code(checkCodes);
        }
        String checkCodess = (String) generalInfo.get("checkCode");
        //如果长度小于6就不截取
        if (StringUtils.hasText(checkCodess) && checkCodess.length() < 6) {
            dto.setCheck_code(checkCodess);
        }

    }

    // 判断是否为数电票(增值税专用)
    private boolean isDigitalInvoice(String invoiceType) {
        return InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE.equals(invoiceType) || InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE.equals(invoiceType);
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
    private R<Void> validateInvoice(InvoiceCheckParamDTO dto, Map<String, Object> generalInfo) throws Exception {
        // 获取 fileId
        String fileId = (String) generalInfo.get("fileId");
        if (StrUtil.isEmpty(fileId)) {
            log.error("fileId 为空");
            return R.fail(500, "查验失败,fileId 为空");
        }
        DataImageFilesInfo filesInfo = filesInfoMapper.selectOne(
            new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, fileId)
        );
        if (ObjUtil.isEmpty(filesInfo)) {
            log.error("根据 fileId 查询图片信息为空");
            return R.fail(500, "查验失败,查询图片信息为空");
        }
        // 调用查验方法
        checkInvoice.checkInvoice(filesInfo, dto);
        DataImageFilesInfo filesInfos = filesInfoMapper.selectOne(
            new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, fileId)
        );
        //查验成功
        if (filesInfos.getFileStatus().equals(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode())) {
            return R.ok();
        }
        return R.fail(500, "发票查验失败," + filesInfos.getMessage());
    }

    //修改机动车销售发票
    private R<Void> updateVehicleSaleInvoice(Map<String, Object> generalInfo) {
        DataMotorVehicleSale motorVehicleSale = BeanUtil.toBean(generalInfo, DataMotorVehicleSale.class);
        int count = motorVehicleSaleMapper.updateById(motorVehicleSale);
        if (count <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int num = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, motorVehicleSale.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (num <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改二手车销售发票
    private R<Void> updateCarSaleInvoice(Map<String, Object> generalInfo) {
        DataUsedCarSales usedCarSales = BeanUtil.toBean(generalInfo, DataUsedCarSales.class);
        int one = usedCarSalesMapper.updateById(usedCarSales);
        if (one <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int nums = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, usedCarSales.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (nums <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改船票
    private R<Void> updateSteamerTicket(Map<String, Object> generalInfo) {
        DataSteamerTicket steamerTicket = BeanUtil.toBean(generalInfo, DataSteamerTicket.class);
        int a = steamerTicketMapper.updateById(steamerTicket);
        if (a <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int nums = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, steamerTicket.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (nums <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改定额发票
    private R<Void> updateQuotaInvoice(Map<String, Object> generalInfo) {
        DataQuotaInvoice quotaInvoice = BeanUtil.toBean(generalInfo, DataQuotaInvoice.class);
        int counts = quotaInvoiceMapper.updateById(quotaInvoice);
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int num = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, quotaInvoice.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (num <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改出租车票
    private R<Void> updateTaxiTickets(Map<String, Object> generalInfo) {
        DataTaxiTickets taxiTickets = BeanUtil.toBean(generalInfo, DataTaxiTickets.class);
        int a = taxiTicketsMapper.updateById(taxiTickets);
        if (a <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int nums = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, taxiTickets.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (nums <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改火车票
    private R<Void> updateRailwayTicket(Map<String, Object> generalInfo) {
        DataRailwayTicket railwayTicket = BeanUtil.toBean(generalInfo, DataRailwayTicket.class);
        int a = railwayTicketMapper.updateById(railwayTicket);
        if (a <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int num = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, railwayTicket.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (num <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改汽车票
    private R<Void> updatePassengerCar(Map<String, Object> generalInfo) {
        DataPassengerCar passengerCar = BeanUtil.toBean(generalInfo, DataPassengerCar.class);
        int num = passengerCarMapper.updateById(passengerCar);
        if (num <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int count = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, passengerCar.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (count <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改过路费
    private R<Void> updateTollRoads(Map<String, Object> generalInfo) {
        DataTollRoads tollRoads = BeanUtil.toBean(generalInfo, DataTollRoads.class);
        int c = tollRoadsMapper.updateById(tollRoads);
        if (c <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int num = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, tollRoads.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (num <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改小票
    private R<Void> updateReceipt(Map<String, Object> generalInfo) {
        DataReceipt receipt = BeanUtil.toBean(generalInfo, DataReceipt.class);
        int a = dataReceiptMapper.updateById(receipt);
        if (a <= 0) {
            return R.fail(500, "修改发票失败");
        }
        int counts = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, receipt.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (counts <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    //修改完税证明
    private R<Void> updateDutyPaidProof(Map<String, Object> generalInfo) {
        DataDutyPaidProof dutyPaidProof = BeanUtil.toBean(generalInfo, DataDutyPaidProof.class);
        int count = paidProofMapper.updateById(dutyPaidProof);
        if (count <= 0) {
            return R.fail(500, "修改发票失败");
        }
//        List<DataDutyPaidProofDetails> ocrDetails = details.stream()
//            .map(item -> BeanUtil.toBean(item, DataDutyPaidProofDetails.class))
//            .toList();
//
//        for (DataDutyPaidProofDetails item : ocrDetails) {
//            int a=paidProofDetailsMapper.updateById(item);
//            if (a<=0){
//                return R.fail(500,"修改发票失败");
//            }
//        }
        int num = filesInfoMapper.update(new LambdaUpdateWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, dutyPaidProof.getFileId()).set(DataImageFilesInfo::getMessage, FileStatusEnumd.UPDATE_YES.getDesc()));
        if (num <= 0) {
            return R.fail(500, "修改发票失败");
        }
        return R.ok();
    }

    /**
     * 发票夹列表信息(待报销,已报销,未报销)
     */
    @Override
    public Page<InvoiceVo> getInvoicePage(InvoicePageQuery pageQuery) throws ExecutionException, InterruptedException {
        Long userId = LoginHelper.getUserId();
        pageQuery.setUserId(userId);
        List<InvoiceVo> invoiceVosList = new ArrayList<>();
        //查询图片file_status为8的图片
//        LambdaQueryWrapper<DataImageFilesInfo> eqs = new LambdaQueryWrapper<DataImageFilesInfo>()
//            .eq(DataImageFilesInfo::getCreateBy, pageQuery.getUserId())
//            .eq(DataImageFilesInfo::getFileStatus, FileStatusEnumd.OCR_FAILED.getCode());
//        List<DataImageFilesInfo> images = filesInfoMapper.selectList(eqs);
//        if (CollectionUtil.isNotEmpty(images)){
//            images.forEach(ima->{
//                InvoiceVo invoiceVo = new InvoiceVo();
//                invoiceVo.setFilesInfo(ima);
//                invoiceVosList.add(invoiceVo);
//            });
//        }
        return this.selectPageCommon(pageQuery, invoiceVosList);
    }

    /**
     * 查验成功列表
     *
     * @param pageQuery
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public Page<InvoiceVo> selectPageCheck(InvoicePageQuery pageQuery) throws ExecutionException, InterruptedException {
        Long userId = LoginHelper.getUserId();
        pageQuery.setUserId(userId);
        List<InvoiceVo> invoiceVosList = new ArrayList<>();
        return this.selectPageCommon(pageQuery, invoiceVosList);
    }

    /**
     * 公共查询
     *
     * @param pageQuery
     * @param invoiceVosList
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public Page<InvoiceVo> selectPageCommon(InvoicePageQuery pageQuery, List<InvoiceVo> invoiceVosList) throws ExecutionException, InterruptedException {
        Page<InvoiceVo> invoiceVoPage = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        //查询待报销,已报销,未报销
        LambdaQueryWrapper<DataImageFilesInfo> eq = new LambdaQueryWrapper<DataImageFilesInfo>()
            .eq(DataImageFilesInfo::getCreateBy, pageQuery.getUserId())
            .eq(DataImageFilesInfo::getFileFlowStatus, pageQuery.getStatus())
            .eq(StrUtil.isNotBlank(pageQuery.getCheckStatus()), DataImageFilesInfo::getCheckStatus, pageQuery.getCheckStatus());
        List<DataImageFilesInfo> dataImageFilesInfos = filesInfoMapper.selectList(eq);
        if (CollectionUtil.isEmpty(dataImageFilesInfos)) {
            return invoiceVoPage;
        }

        List<InvoiceVo> invoiceVos = allInvoiceTypeList(dataImageFilesInfos);
//        tasks.add(() -> transitionOcrInfo(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionCarSaleInvoice(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionSecondCarSaleInvoice(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionAirTicket(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionShipTicket(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionMedicalTicket(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionQuotaInvoice(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionTaxiTickets(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionRailwayTicket(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionPassengerCar(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionTollRoads(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionReceipt(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionTravelInvoice(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionDutyPaidProof(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionCustomsSpecialPayment(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionElectronicTransportationGoods(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionCustomsExportGoods(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionCustomsImportGoods(dataImageFilesInfos, pageQuery.getUserId()));
//        tasks.add(() -> transitionNonTaxRevenueReceipts(dataImageFilesInfos, pageQuery.getUserId()));
        invoiceVosList.addAll(invoiceVos);
        invoiceVoPage.setRecords(invoiceVosList);
        invoiceVoPage.setTotal(invoiceVosList.size());
        // 计算总计金额
        BigDecimal totalAmount = invoiceVosList.stream()
            .map(InvoiceVo::getMoneyAsBigDecimal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 将BigDecimal类型的totalAmount转换为String类型后赋值给grossAmount
        String totalAmountStr = totalAmount.toString();
        invoiceVosList.forEach(invoiceVo -> invoiceVo.setGrossAmount(totalAmountStr));
        invoiceVosList.forEach(vo -> {
            if (vo.getCreateTime() == null) {
                vo.setCreateTime(new Date()); // 设置为当前日期
            }
        });
        // 然后排序
        invoiceVosList.sort((InvoiceVo o1, InvoiceVo o2) -> {
            return o2.getCreateTime().compareTo(o1.getCreateTime());
        });
        // 进行分页处理
        int pageNum = pageQuery.getPageNum();
        int pageSize = pageQuery.getPageSize();
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int startIndex = (pageNum - 1) * pageSize;
        if (startIndex >= invoiceVosList.size()) {
            // 请求的页码超出范围，返回空的分页数据
            invoiceVoPage.setRecords(Collections.emptyList());
            invoiceVoPage.setTotal(invoiceVosList.size());
            return invoiceVoPage;
        }
        int endIndex = Math.min(startIndex + pageSize, invoiceVosList.size());
        List<InvoiceVo> pageData = invoiceVosList.subList(startIndex, endIndex);
        invoiceVoPage.setRecords(pageData);
        invoiceVoPage.setTotal(invoiceVosList.size());
        return invoiceVoPage;
    }


    //查询非税发票InvoiceVo
    private List<InvoiceVo> transitionNonTaxRevenueReceipts(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> nonTaxRevenueReceiptsList;
        nonTaxRevenueReceiptsList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataNonTax> nonTaxRevenueReceipts = dataNonTaxMapper.selectList(new LambdaQueryWrapper<DataNonTax>()
                    .eq(DataNonTax::getFileId, dataImageFilesInfo.getFileId()).eq(DataNonTax::getCreateBy, userId));
                return nonTaxRevenueReceipts.stream().map(
                    dataNonTaxRevenueReceipts -> {
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(dataNonTaxRevenueReceipts.getId());
                        invoiceVo.setFileId(dataNonTaxRevenueReceipts.getFileId());
                        invoiceVo.setPayer(dataNonTaxRevenueReceipts.getPayer());
                        invoiceVo.setPaymentCode(dataNonTaxRevenueReceipts.getPaymentCode());
                        invoiceVo.setInvoiceDate(dataNonTaxRevenueReceipts.getInvoiceDate());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.NON_TAX_REVENUE_RECEIPTS_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(dataNonTaxRevenueReceipts.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
                        return invoiceVo;
                    });
            }).toList();
        return nonTaxRevenueReceiptsList;
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(dataCustomsImportGoods.getId());
                        invoiceVo.setFileId(dataCustomsImportGoods.getFileId());
                        invoiceVo.setConsumptionCompanyName(dataCustomsImportGoods.getConsumptionCompanyName());
                        invoiceVo.setContractNumber(dataCustomsImportGoods.getContractNumber());
                        invoiceVo.setCustomsNumber(dataCustomsImportGoods.getCustomsNumber());
                        invoiceVo.setDateOfApplication(dataCustomsImportGoods.getDateOfApplication());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.CUSTOMS_IMPORTED_GOODS_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(dataCustomsExportGoods.getId());
                        invoiceVo.setFileId(dataCustomsExportGoods.getFileId());
                        invoiceVo.setConsumptionCompanyName(dataCustomsExportGoods.getConsumptionCompanyName());
                        invoiceVo.setContractNumber(dataCustomsExportGoods.getContractNumber());
                        invoiceVo.setCustomsNumber(dataCustomsExportGoods.getCustomsNumber());
                        invoiceVo.setDateOfApplication(dataCustomsExportGoods.getDateOfApplication());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(dataElectronicTransportationGoods1.getId());
                        invoiceVo.setFileId(dataElectronicTransportationGoods1.getFileId());
                        invoiceVo.setShipper(dataElectronicTransportationGoods1.getShipper());
                        invoiceVo.setElectronicReceiptNumber(dataElectronicTransportationGoods1.getElectronicReceiptNumber());
                        invoiceVo.setDate(dataElectronicTransportationGoods1.getDate());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(dataElectronicTransportationGoods1.getTotalPrice());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(dataCustomsSpecialPayments.getId());
                        invoiceVo.setFileId(dataCustomsSpecialPayments.getFileId());
                        invoiceVo.setCompanyName(dataCustomsSpecialPayments.getCompanyName());
                        invoiceVo.setCustomsNumber(dataCustomsSpecialPayments.getCustomsNumber());
                        invoiceVo.setInvoiceDate(dataCustomsSpecialPayments.getInvoiceDate());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(dataCustomsSpecialPayments.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(dataDutyPaidProofs.getId());
                        invoiceVo.setFileId(dataDutyPaidProofs.getFileId());
                        invoiceVo.setBuyerName(dataDutyPaidProofs.getBuyerName());
                        invoiceVo.setInvoiceDate(dataDutyPaidProofs.getInvoiceDate());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_DUTY_PAID_PROOF_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(dataDutyPaidProofs.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(dataDidiItinerars.getId());
                        invoiceVo.setFileId(dataDidiItinerars.getFileId());
                        invoiceVo.setPhone(dataDidiItinerars.getPhone());
                        invoiceVo.setInvoiceDate(dataDidiItinerars.getInvoiceDate());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_DIDI_ITINERARY_CODE.getCode());
                        invoiceVo.setTimeGetOn(dataDidiItinerars.getTimeGetOn());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(dataDidiItinerars.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(receipts.getId());
                        invoiceVo.setFileId(receipts.getFileId());
                        invoiceVo.setStoreName(receipts.getStoreName());
                        invoiceVo.setInvoiceDate(receipts.getInvoiceDate());
                        invoiceVo.setInvoiceNumber(receipts.getInvoiceNumber());
                        invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(receipts.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCode(receipts.getCode());
                        invoiceVo.setNumber(receipts.getNumber());
                        invoiceVo.setKind(receipts.getKind());
                        invoiceVo.setTitle(receipts.getTitle());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(tollRoad.getId());
                        invoiceVo.setFileId(tollRoad.getFileId());
                        invoiceVo.setInvoiceTime(tollRoad.getInvoiceTime());
                        invoiceVo.setInvoiceNumber(tollRoad.getInvoiceNumber());
                        invoiceVo.setInvoiceCode(tollRoad.getInvoiceCode());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_TOLL_ROADS_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(tollRoad.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(passengerCar.getId());
                        invoiceVo.setFileId(passengerCar.getFileId());
                        invoiceVo.setName(passengerCar.getName());
                        invoiceVo.setInvoiceDate(passengerCar.getInvoiceDate());
                        invoiceVo.setInvoiceNumber(passengerCar.getInvoiceNumber());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_PASSENGER_TICKET_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(passengerCar.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                        InvoiceVo invoiceVo = new InvoiceVo();
                        invoiceVo.setId(railwayTicket.getId());
                        invoiceVo.setFileId(railwayTicket.getFileId());
                        invoiceVo.setName(railwayTicket.getName());
                        invoiceVo.setInvoiceDate(railwayTicket.getInvoiceDate());
                        invoiceVo.setTrainNumber(railwayTicket.getTrainNumber());
                        invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_RAILWAY_TICKET_CODE.getCode());
                        invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                        invoiceVo.setInvoiceTotal(railwayTicket.getInvoiceTotal());
                        invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                        invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                        invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                    InvoiceVo invoiceVo = new InvoiceVo();
                    invoiceVo.setId(taxiTicket.getId());
                    invoiceVo.setFileId(taxiTicket.getFileId());
                    invoiceVo.setCity(taxiTicket.getCity());
                    invoiceVo.setInvoiceDate(taxiTicket.getInvoiceDate());
                    invoiceVo.setInvoiceNumber(taxiTicket.getInvoiceNumber());
                    invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_TAXI_TICKETS_CODE.getCode());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setInvoiceTotal(taxiTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    invoiceVo.setProvinces(taxiTicket.getProvince() + taxiTicket.getCity());
                    invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                    InvoiceVo invoiceVo = new InvoiceVo();
                    invoiceVo.setId(dataQuotaInvoice.getId());
                    invoiceVo.setFileId(dataQuotaInvoice.getFileId());
                    invoiceVo.setInvoiceNumber(dataQuotaInvoice.getInvoiceNumber());
                    invoiceVo.setInvoiceCode(dataQuotaInvoice.getInvoiceCode());
                    invoiceVo.setCity(dataQuotaInvoice.getCity());
                    invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_QUOTA_INVOICE_CODE.getCode());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setInvoiceTotal(dataQuotaInvoice.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    invoiceVo.setProvinces(dataQuotaInvoice.getProvince() + dataQuotaInvoice.getCity());
                    invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
                    return invoiceVo;
                });
            }
        ).toList();
        return invoiceList;
    }

    //查询医疗票组成InvoiceVo
    private List<InvoiceVo> transitionMedicalTicket(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataMedicalTreatment> dataMedicalTickets = dataMedicalTreatmentMapper.selectList(new LambdaQueryWrapper<DataMedicalTreatment>()
                    .eq(DataMedicalTreatment::getFileId, dataImageFilesInfo.getFileId()).eq(DataMedicalTreatment::getCreateBy, userId));
                return dataMedicalTickets.stream().map(dataMedicalTicket -> {
                    InvoiceVo invoiceVo = new InvoiceVo();
                    invoiceVo.setId(dataMedicalTicket.getId());
                    invoiceVo.setFileId(dataMedicalTicket.getFileId());
                    invoiceVo.setInvoiceNumber(dataMedicalTicket.getInvoiceNumber());
                    invoiceVo.setPayer(dataMedicalTicket.getPayer());
                    invoiceVo.setVisitDate(dataMedicalTicket.getVisitDate());
                    invoiceVo.setInvoiceType(InvoiceGlorityEnumd.MEDICAL_TICKET_DETAILS_CODE.getCode());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setInvoiceTotal(dataMedicalTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                    InvoiceVo invoiceVo = new InvoiceVo();
                    invoiceVo.setId(dataShipTicket.getId());
                    invoiceVo.setFileId(dataShipTicket.getFileId());
                    invoiceVo.setName(dataShipTicket.getName());
                    invoiceVo.setInvoiceNumber(dataShipTicket.getInvoiceNumber());
                    invoiceVo.setInvoiceDate(dataShipTicket.getInvoiceDate());
                    invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_STEAMER_TICKET_CODE.getCode());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setInvoiceTotal(dataShipTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                    InvoiceVo invoiceVo = new InvoiceVo();
                    invoiceVo.setId(dataAirTicket.getId());
                    invoiceVo.setFileId(dataAirTicket.getFileId());
                    invoiceVo.setUserName(dataAirTicket.getUserName());
                    invoiceVo.setInvoiceDate(dataAirTicket.getInvoiceDate());
                    invoiceVo.setInvoiceNumber(dataAirTicket.getInvoiceNumber());
                    invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_FLIGHT_ITINERARY_CODE.getCode());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setInvoiceTotal(dataAirTicket.getInvoiceTotal());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                    InvoiceVo invoiceVo = new InvoiceVo();
                    invoiceVo.setId(dataSecondCarSaleInvoice.getId());
                    invoiceVo.setFileId(dataSecondCarSaleInvoice.getFileId());
                    invoiceVo.setBuyerName(dataSecondCarSaleInvoice.getBuyerName());
                    invoiceVo.setSellerName(dataSecondCarSaleInvoice.getSellerName());
                    invoiceVo.setInvoiceDate(dataSecondCarSaleInvoice.getInvoiceDate());
                    invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_USED_CAR_SALES_CODE.getCode());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setInvoiceTotal(dataSecondCarSaleInvoice.getInvoiceTotal());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
                    return invoiceVo;
                });

            }).toList();
        return invoiceList;
    }

    //查询机动车销售发票信息并组装成InvoiceVo
    public List<InvoiceVo> transitionCarSaleInvoice(List<DataImageFilesInfo> dataImageFilesInfos, Long userId) {
        List<InvoiceVo> invoiceList;
        invoiceList = dataImageFilesInfos.stream().flatMap(
            dataImageFilesInfo -> {
                List<DataMotorVehicleSale> dataCarSaleInvoices = motorVehicleSaleMapper.selectList(new LambdaQueryWrapper<DataMotorVehicleSale>()
                    .eq(DataMotorVehicleSale::getFileId, dataImageFilesInfo.getFileId()).eq(DataMotorVehicleSale::getCreateBy, userId));
                return dataCarSaleInvoices.stream().map(dataCarSaleInvoice -> {
                    InvoiceVo invoiceVo = new InvoiceVo();
                    invoiceVo.setId(dataCarSaleInvoice.getId());
                    invoiceVo.setFileId(dataCarSaleInvoice.getFileId());
                    invoiceVo.setBuyerName(dataCarSaleInvoice.getBuyerName());
                    invoiceVo.setInvoiceNumber(dataCarSaleInvoice.getInvoiceNumber());
                    invoiceVo.setInvoiceDate(dataCarSaleInvoice.getInvoiceDate());
                    invoiceVo.setInvoiceType(InvoiceGlorityEnumd.GLORITY_MOTOR_VEHICLE_SALE_CODE.getCode());
                    invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                    invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                    invoiceVo.setInvoiceTotal(dataCarSaleInvoice.getInvoiceTotal());
                    invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                    invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
                InvoiceVo invoiceVo = new InvoiceVo();
                invoiceVo.setId(dataOcrInfo.getId());
                invoiceVo.setFileId(dataOcrInfo.getFileId());
                invoiceVo.setBuyerName(dataOcrInfo.getBuyerName());
                invoiceVo.setSellerName(dataOcrInfo.getSellerName());
                invoiceVo.setInvoiceDate(dataOcrInfo.getInvoiceDate());
                invoiceVo.setInvoiceType(dataImageFilesInfo.getInvoice());
                invoiceVo.setMessage(dataImageFilesInfo.getMessage());
                invoiceVo.setCheckStatus(dataImageFilesInfo.getCheckStatus());
                invoiceVo.setInvoiceTotal(dataOcrInfo.getTotalLowercase());
                invoiceVo.setStatus(dataImageFilesInfo.getFileFlowStatus());
                invoiceVo.setCreateTime(dataImageFilesInfo.getCreateTime());
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
        if (StrUtil.isEmpty(fileId)) {
            return R.fail(500, "文件id不能为空");
        }
        //根据fileId查询发票信息
        DataImageFilesInfo res = filesInfoMapper.selectOne(new LambdaQueryWrapper<DataImageFilesInfo>().eq(DataImageFilesInfo::getFileId, fileId));
        if (ObjectUtil.isEmpty(res)) {
            return R.fail();
        }
        if (!res.getFileId().isEmpty()) {
            String invoiceType = res.getInvoice();
            Object info = null;
            Object detailInfo = null;
            switch (invoiceType) {
                //增值税、机打
                case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_TAX_SPECIAL_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRON_TAX_SPECIAL_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.GLORITY_TAX_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_TAX_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRONIC_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRONIC_QUKUAILIAN_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_ROLL_TICKET_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.DIGITAL_INVOICE_VAT_SPECIAL_CODE.getCode());
                    DataOcrInfo dataOcrInfo = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    dataOcrInfo.setInvoiceCode("");
                    info = dataOcrInfo;
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.DIGITAL_INVOICE_LIST:
                    res.setInvoice(InvoiceGlorityEnumd.DIGITAL_INVOICE_LIST.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_AIRCRAFT_INVOICE_CODE.getCode());
                    info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.REIMBURSABLE_OTHER_CODE.getCode());
                    info = dataReceiptMapper.selectOne(new LambdaQueryWrapper<DataReceipt>().eq(DataReceipt::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE.getCode());
                    DataOcrInfo dataOcrInfo2 = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                    dataOcrInfo2.setInvoiceCode("");
                    info = dataOcrInfo2;
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //机动车
                case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_MOTOR_VEHICLE_SALE_CODE.getCode());
                    info = motorVehicleSaleMapper.selectOne(new LambdaQueryWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //航空运输电子客票行程单
                case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_FLIGHT_ITINERARY_CODE.getCode());
                    info = flightItineraryMapper.selectOne(new LambdaQueryWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getFileId, fileId));
                    detailInfo = flightsItineraryDetailMapper.selectList(new LambdaQueryWrapper<DataFlightsItineraryDetail>().eq(DataFlightsItineraryDetail::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //二手车
                case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_USED_CAR_SALES_CODE.getCode());
                    info = usedCarSalesMapper.selectOne(new LambdaQueryWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //船票
                case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_STEAMER_TICKET_CODE.getCode());
                    info = steamerTicketMapper.selectOne(new LambdaQueryWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //医疗票明细票
                case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
                case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.MEDICAL_TICKET_DETAILS_CODE.getCode());
                    info = dataMedicalTreatmentMapper.selectOne(new LambdaQueryWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //非税收入类发票
                case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.NON_TAX_REVENUE_RECEIPTS_CODE.getCode());
                    info = dataNonTaxMapper.selectOne(new LambdaQueryWrapper<DataNonTax>().eq(DataNonTax::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //定额发票
                case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_QUOTA_INVOICE_CODE.getCode());
                    info = quotaInvoiceMapper.selectOne(new LambdaQueryWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //出租车发票
                case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_TAXI_TICKETS_CODE.getCode());
                    info = taxiTicketsMapper.selectOne(new LambdaQueryWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //火车发票
                case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_RAILWAY_TICKET_CODE.getCode());
                    info = railwayTicketMapper.selectOne(new LambdaQueryWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //客运车发票
                case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_PASSENGER_TICKET_CODE.getCode());
                    info = passengerCarMapper.selectOne(new LambdaQueryWrapper<DataPassengerCar>().eq(DataPassengerCar::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //过路费发票
                case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_TOLL_ROADS_CODE.getCode());
                    info = tollRoadsMapper.selectOne(new LambdaQueryWrapper<DataTollRoads>().eq(DataTollRoads::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //小票
                case InvoiceConstants.GLORITY_RECEIPT_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_RECEIPT_CODE.getCode());
                    info = dataReceiptMapper.selectOne(new LambdaQueryWrapper<DataReceipt>().eq(DataReceipt::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, null));
                //出行发票/滴滴
                case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_DIDI_ITINERARY_CODE.getCode());
                    info = didiItineraryMapper.selectOne(new LambdaQueryWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getFileId, fileId));
                    detailInfo = didiItineraryDetailsMapper.selectList(new LambdaQueryWrapper<DataDidiItineraryDetails>().eq(DataDidiItineraryDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //完税证明发票
                case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.GLORITY_DUTY_PAID_PROOF_CODE.getCode());
                    info = paidProofMapper.selectOne(new LambdaQueryWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getFileId, fileId));
                    detailInfo = paidProofDetailsMapper.selectList(new LambdaQueryWrapper<DataDutyPaidProofDetails>().eq(DataDutyPaidProofDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //海关进口货物报关单发票
                case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.CUSTOMS_IMPORTED_GOODS_CODE.getCode());
                    info = customsImportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getFileId, fileId));
                    detailInfo = customsExportGoodsDetailMapper.selectList(new LambdaQueryWrapper<DataCustomsExportGoodsDetail>().eq(DataCustomsExportGoodsDetail::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //海关出口货物报关单发票
                case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.CUSTOMS_EXPORT_GOODS_CODE.getCode());
                    info = customsExportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getFileId, fileId));
                    detailInfo = customsExportGoodsDetailMapper.selectList(new LambdaQueryWrapper<DataCustomsExportGoodsDetail>().eq(DataCustomsExportGoodsDetail::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //海关专用缴款书发票
                case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE.getCode());
                    info = customsSpecialPaymentMapper.selectOne(new LambdaQueryWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                //货物运输电子收款凭证发票
                case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                    res.setInvoice(InvoiceGlorityEnumd.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE.getCode());
                    info = paymentMapper.selectOne(new LambdaQueryWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getFileId, fileId));
                    detailInfo = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
                    return R.ok(new DataResponseDTO(res, info, detailInfo));
                default: {
                    return R.fail();
                }
            }
        }
        return R.fail();
    }

    //发票新增
    @Override
    public R<Void> addInvoice(InvoiceRequest request) throws Exception {
        //获取发票类型
        String invoiceType = request.getInvoiceType();
        if (StrUtil.isEmpty(invoiceType)) {
            return R.fail("发票类型不能为空");
        }
        Map<String, Object> generalInfo = request.getGeneralInfo();
        log.info("新增传入的参数:" + generalInfo);
        //判断fileId是否为空
        Object fileIdObj = generalInfo.get("fileId");
        if (fileIdObj != null && StrUtil.isEmpty((CharSequence) fileIdObj)) {
            return R.fail("文件id不能为空");
        }
        switch (invoiceType) {
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                // 处理增值税发票
                return addOcrInvoice(generalInfo, invoiceType);
            //机打发票/增值税发票清单/可报销其他发票
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
                return addOrdinaryInvoice(generalInfo, invoiceType);
            //机动车销售发票
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                return addVehicleSaleInvoice(generalInfo, invoiceType);
            //二手车发票
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                return addCarSaleInvoice(generalInfo, invoiceType);
            //航空电子客运单
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                return addFilghtItinerary(generalInfo, invoiceType);
            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                return addSteamerTicket(generalInfo, invoiceType);
            //医疗票明细票/医疗票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                return addMedicalTicket(generalInfo, invoiceType);
            //非税收入单据
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                return addNonTaxRevenueReceipts(generalInfo, invoiceType);
            //定额发票
            case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                return addQuotaInvoice(generalInfo, invoiceType);
            //出租车发票
            case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                return addTaxiTickets(generalInfo, invoiceType);
            //火车发票
            case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                return addRailwayTicket(generalInfo, invoiceType);
            //客运车发票
            case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                return addPassengerCar(generalInfo, invoiceType);
            //过路费发票
            case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                return addTollRoads(generalInfo, invoiceType);
            //小票/可报销其他发票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                return addReceipt(generalInfo, invoiceType);
            //出行发票/滴滴
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                return addDidiItinerary(generalInfo, invoiceType);
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                return addDutyPaidProof(generalInfo, invoiceType);
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                return addCustomsImportGoods(generalInfo, invoiceType);
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                return addCustomsExportGoods(generalInfo, invoiceType);
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                return addCustomsSpecialPayment(generalInfo, invoiceType);
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                return addElectronicPaymentGoodsTransportation(generalInfo, invoiceType);
        }
        return R.fail(500, "发票类型错误");
    }

    //火车票新增
    private R<Void> addRailwayTicket(Map<String, Object> generalInfo, String invoiceType) {
        DataRailwayTicket res = BeanUtil.toBean(generalInfo, DataRailwayTicket.class);
        int one = railwayTicketMapper.insert(res);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(res.getFileId(), invoiceType);
    }

    //货物运输电子收款凭证发票
    private R<Void> addElectronicPaymentGoodsTransportation(Map<String, Object> generalInfo, String invoiceType) {
        DataElectronicTransportationGoods res = BeanUtil.toBean(generalInfo, DataElectronicTransportationGoods.class);
        int i = paymentMapper.insert(res);
        if (i <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(res.getFileId(), invoiceType);
    }
    //海关专用缴款书发票

    private R<Void> addCustomsSpecialPayment(Map<String, Object> generalInfo, String invoiceType) {
        DataCustomsSpecialPayment customsSpecialPayment = BeanUtil.toBean(generalInfo, DataCustomsSpecialPayment.class);
        int i = customsSpecialPaymentMapper.insert(customsSpecialPayment);
        if (i <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(customsSpecialPayment.getFileId(), invoiceType);
    }

    //海关出口货物报关单发票
    private R<Void> addCustomsExportGoods(Map<String, Object> generalInfo, String invoiceType) {
        DataCustomsExportGoods customsExportGoods = BeanUtil.toBean(generalInfo, DataCustomsExportGoods.class);
        int i = customsExportGoodsMapper.insert(customsExportGoods);
        if (i <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(customsExportGoods.getFileId(), invoiceType);
    }

    //海关进口货物报关单发票
    private R<Void> addCustomsImportGoods(Map<String, Object> generalInfo, String invoiceType) {
        DataCustomsImxportGoods customsImportGoods = BeanUtil.toBean(generalInfo, DataCustomsImxportGoods.class);
        int one = customsImportGoodsMapper.insert(customsImportGoods);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(customsImportGoods.getFileId(), invoiceType);
    }

    //完税证明发票新增
    private R<Void> addDutyPaidProof(Map<String, Object> generalInfo, String invoiceType) {
        DataDutyPaidProof dutyPaidProof = BeanUtil.toBean(generalInfo, DataDutyPaidProof.class);
        int one = paidProofMapper.insert(dutyPaidProof);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(dutyPaidProof.getFileId(), invoiceType);
    }

    //出行发票/滴滴
    private R<Void> addDidiItinerary(Map<String, Object> generalInfo, String invoiceType) {
        DataDidiItinerary didiItinerary = BeanUtil.toBean(generalInfo, DataDidiItinerary.class);
        int one = didiItineraryMapper.insert(didiItinerary);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(didiItinerary.getFileId(), invoiceType);
    }

    //小票新增
    private R<Void> addReceipt(Map<String, Object> generalInfo, String invoiceType) {
        DataReceipt receipt = BeanUtil.toBean(generalInfo, DataReceipt.class);
        int one = dataReceiptMapper.insert(receipt);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(receipt.getFileId(), invoiceType);
    }

    //过路费发票新增
    private R<Void> addTollRoads(Map<String, Object> generalInfo, String invoiceType) {
        DataTollRoads tollRoads = BeanUtil.toBean(generalInfo, DataTollRoads.class);
        int one = tollRoadsMapper.insert(tollRoads);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(tollRoads.getFileId(), invoiceType);
    }

    //客运发票新增
    private R<Void> addPassengerCar(Map<String, Object> generalInfo, String invoiceType) {
        DataPassengerCar passengerCar = BeanUtil.toBean(generalInfo, DataPassengerCar.class);
        int one = passengerCarMapper.insert(passengerCar);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(passengerCar.getFileId(), invoiceType);
    }

    //出行发票新增
    private R<Void> addTaxiTickets(Map<String, Object> generalInfo, String invoiceType) {
        DataTaxiTickets taxiTickets = BeanUtil.toBean(generalInfo, DataTaxiTickets.class);
        int one = taxiTicketsMapper.insert(taxiTickets);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(taxiTickets.getFileId(), invoiceType);
    }

    //定额发票新增
    private R<Void> addQuotaInvoice(Map<String, Object> generalInfo, String invoiceType) {
        DataQuotaInvoice quotaInvoice = BeanUtil.toBean(generalInfo, DataQuotaInvoice.class);
        int one = quotaInvoiceMapper.insert(quotaInvoice);
        if (one <= 0) {
            return R.fail(500, "新增发票失败");
        }
        return updateImages(quotaInvoice.getFileId(), invoiceType);
    }

    //非税收入单据新增
    private R<Void> addNonTaxRevenueReceipts(Map<String, Object> generalInfo, String invoiceType) {
        DataNonTax dataNonTax = BeanUtil.toBean(generalInfo, DataNonTax.class);
        int res = dataNonTaxMapper.insert(dataNonTax);
        if (res <= 0) {
            return R.fail("新增发票失败");
        }
        return updateImages(dataNonTax.getFileId(), invoiceType);
    }

    //添加医疗票
    private R<Void> addMedicalTicket(Map<String, Object> generalInfo, String invoiceType) {
        DataMedicalTreatment dataMedicalTreatment = BeanUtil.toBean(generalInfo, DataMedicalTreatment.class);
        int res = dataMedicalTreatmentMapper.insert(dataMedicalTreatment);
        if (res <= 0) {
            return R.fail("新增发票失败");
        }
        return updateImages(dataMedicalTreatment.getFileId(), invoiceType);
    }

    //添加船票
    private R<Void> addSteamerTicket(Map<String, Object> generalInfo, String invoiceType) {
        DataSteamerTicket dataSteamerTicket = BeanUtil.toBean(generalInfo, DataSteamerTicket.class);
        int res = steamerTicketMapper.insert(dataSteamerTicket);
        if (res <= 0) {
            return R.fail("新增发票失败");
        }
        return updateImages(dataSteamerTicket.getFileId(), invoiceType);
    }

    //添加航空电子客票
    private R<Void> addFilghtItinerary(Map<String, Object> generalInfo, String invoiceType) {
        DataFlightItinerary dataFlightItinerary = BeanUtil.toBean(generalInfo, DataFlightItinerary.class);
        int res = flightItineraryMapper.insert(dataFlightItinerary);
        if (res <= 0) {
            return R.fail("新增发票失败");
        }
        return updateImages(dataFlightItinerary.getFileId(), invoiceType);
    }

    //添加二手车发票
    private R<Void> addCarSaleInvoice(Map<String, Object> generalInfo, String invoiceType) {
        DataUsedCarSales dataUsedCarSales = BeanUtil.toBean(generalInfo, DataUsedCarSales.class);
        int res = usedCarSalesMapper.insert(dataUsedCarSales);
        if (res <= 0) {
            return R.fail("新增发票失败");
        }
        //根据file_id查询图片信息
        DataImageFilesInfo imageFiles = filesInfoMapper
            .selectOne(new LambdaQueryWrapper<DataImageFilesInfo>()
                .eq(DataImageFilesInfo::getFileId, dataUsedCarSales.getFileId()));
        if (ObjectUtil.isEmpty(imageFiles)) {
            return R.fail("文件信息为空");
        }
        //修改图片信息
        imageFiles.setInvoice(invoiceType);
        imageFiles.setFileStatus("");
        imageFiles.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
        int image = filesInfoMapper.updateById(imageFiles);
        if (image <= 0) {
            return R.fail("修改图片信息失败");
        }
        return R.ok();
    }

    //添加机动车销售发票
    private R<Void> addVehicleSaleInvoice(Map<String, Object> generalInfo, String invoiceType) {
        DataMotorVehicleSale dataMotorVehicleSale = BeanUtil.toBean(generalInfo, DataMotorVehicleSale.class);
        int res = motorVehicleSaleMapper.insert(dataMotorVehicleSale);
        if (res <= 0) {
            return R.fail("新增发票失败");
        }
        //根据file_id查询图片信息
        DataImageFilesInfo imageFiles = filesInfoMapper
            .selectOne(new LambdaQueryWrapper<DataImageFilesInfo>()
                .eq(DataImageFilesInfo::getFileId, dataMotorVehicleSale.getFileId()));
        if (ObjectUtil.isEmpty(imageFiles)) {
            return R.fail("文件信息为空");
        }
        //修改图片信息
        imageFiles.setInvoice(invoiceType);
        imageFiles.setFileStatus("");
        imageFiles.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
        int image = filesInfoMapper.updateById(imageFiles);
        if (image <= 0) {
            return R.fail("修改图片信息失败");
        }
        return R.ok();
    }

    //添加数电票普通发票/机打发票/增值税发票清单/可报销其他发票
    private R<Void> addOrdinaryInvoice(Map<String, Object> generalInfo, String invoiceType) {
        DataOcrInfo dataOcrInfo = BeanUtil.toBean(generalInfo, DataOcrInfo.class);
        int info = ocrInfoMapper.insert(dataOcrInfo);
        if (info <= 0) {
            return R.fail("新增发票失败");
        }
        return updateImages(dataOcrInfo.getFileId(), invoiceType);
    }

    //增值税发票
    private R<Void> addOcrInvoice(Map<String, Object> generalInfo, String invoiceType) throws Exception {
        DataOcrInfo dataOcrInfo = BeanUtil.toBean(generalInfo, DataOcrInfo.class);
        int info = ocrInfoMapper.insert(dataOcrInfo);
        if (info <= 0) {
            return R.fail("新增发票失败");
        }
        //根据file_id查询图片信息
        DataImageFilesInfo imageFiles = filesInfoMapper
            .selectOne(new LambdaQueryWrapper<DataImageFilesInfo>()
                .eq(DataImageFilesInfo::getFileId, dataOcrInfo.getFileId()));
        if (ObjectUtil.isEmpty(imageFiles)) {
            return R.fail("文件信息为空");
        }
        //修改图片信息
        imageFiles.setInvoice(invoiceType);
        imageFiles.setFileStatus("");
        imageFiles.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
        int image = filesInfoMapper.updateById(imageFiles);
        if (image <= 0) {
            return R.fail("修改图片信息失败");
        }
        return R.ok();
    }

    private R<Void> updateImages(String fileId, String invoiceType) {
        //根据file_id查询图片信息
        DataImageFilesInfo imageFiles = filesInfoMapper
            .selectOne(new LambdaQueryWrapper<DataImageFilesInfo>()
                .eq(DataImageFilesInfo::getFileId, fileId));
        if (ObjectUtil.isEmpty(imageFiles)) {
            return R.fail("文件信息为空");
        }
        //修改图片信息
        imageFiles.setInvoice(invoiceType);
        imageFiles.setFileStatus("");
        int image = filesInfoMapper.updateById(imageFiles);
        if (image <= 0) {
            return R.fail("修改图片信息失败");
        }
        return R.ok();
    }

    //发票回写查询
    @Override
    public List<InvoiceWriteBackVo> invoiceWriteSelect(List<HashMap<String, Object>> list) {
        List<InvoiceWriteBackVo> voList = new ArrayList<>();
        for (HashMap<String, Object> invoiceType : list) {
            String type = invoiceType.get("type").toString();
            String id = invoiceType.get("id").toString();
            InvoiceWriteBackVo invoiceWriteBackVo = selectTypeInvoice(type, id);
            if (ObjectUtil.isNotEmpty(invoiceWriteBackVo)) {
                voList.add(invoiceWriteBackVo);
            }
        }
        return voList;
    }

    private InvoiceWriteBackVo selectTypeInvoice(String invoiceType, String invoiceId) {
        switch (invoiceType) {
            //增值税、机打
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
                DataOcrInfo ocrInfo = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getId, invoiceId));
                String url1 = getInvoiceUrl(ocrInfo.getFileId());
                return new InvoiceWriteBackVo().setAmount(ocrInfo.getTotalLowercase()).setDetails(ocrInfo.getRemark()).setInvoiceId(invoiceId).setUrL(url1);
            //机动车
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                DataMotorVehicleSale dataMotorVehicleSale = motorVehicleSaleMapper.selectOne(new LambdaQueryWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getId, invoiceId));
                String url2 = getInvoiceUrl(dataMotorVehicleSale.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataMotorVehicleSale.getInvoiceTotal()).setDetails(dataMotorVehicleSale.getRemark()).setInvoiceId(invoiceId).setUrL(url2);

            //航空运输电子客票行程单
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                DataFlightItinerary dataFlightItinerary = flightItineraryMapper.selectOne(new LambdaQueryWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getId, invoiceId));
                String url3 = getInvoiceUrl(dataFlightItinerary.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataFlightItinerary.getInvoiceTotal()).setDetails(dataFlightItinerary.getRemark()).setInvoiceId(invoiceId).setUrL(url3);
            //二手车
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                DataUsedCarSales usedCarSales = usedCarSalesMapper.selectOne(new LambdaQueryWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getId, invoiceId));
                String url4 = getInvoiceUrl(usedCarSales.getFileId());
                return new InvoiceWriteBackVo().setAmount(usedCarSales.getInvoiceTotal()).setDetails(usedCarSales.getRemark()).setInvoiceId(invoiceId).setUrL(url4);

            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                DataSteamerTicket dataSteamerTicket = steamerTicketMapper.selectOne(new LambdaQueryWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getId, invoiceId));
                String url5 = getInvoiceUrl(dataSteamerTicket.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataSteamerTicket.getInvoiceTotal()).setDetails(dataSteamerTicket.getRemark()).setInvoiceId(invoiceId).setUrL(url5);

            //医疗票明细票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                DataMedicalTreatment medicalTreatment = dataMedicalTreatmentMapper.selectOne(new LambdaQueryWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getId, invoiceId));
                String url6 = getInvoiceUrl(medicalTreatment.getFileId());
                return new InvoiceWriteBackVo().setAmount(medicalTreatment.getInvoiceTotal()).setDetails(medicalTreatment.getRemark()).setInvoiceId(invoiceId).setUrL(url6);

            //非税收入类发票
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                DataNonTax dataNonTax = dataNonTaxMapper.selectOne(new LambdaQueryWrapper<DataNonTax>().eq(DataNonTax::getId, invoiceId));
                String url7 = getInvoiceUrl(dataNonTax.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataNonTax.getInvoiceTotal()).setDetails(dataNonTax.getRemark()).setInvoiceId(invoiceId).setUrL(url7);

            //定额发票
            case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                DataQuotaInvoice dataQuotaInvoice = quotaInvoiceMapper.selectOne(new LambdaQueryWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getId, invoiceId));
                String url8 = getInvoiceUrl(dataQuotaInvoice.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataQuotaInvoice.getInvoiceTotal()).setDetails(dataQuotaInvoice.getRemark()).setInvoiceId(invoiceId).setUrL(url8);
            //出租车发票
            case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                DataTaxiTickets dataTaxiTickets = taxiTicketsMapper.selectOne(new LambdaQueryWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getId, invoiceId));
                String url9 = getInvoiceUrl(dataTaxiTickets.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataTaxiTickets.getInvoiceTotal()).setDetails(dataTaxiTickets.getRemark()).setInvoiceId(invoiceId).setUrL(url9);
            //火车发票
            case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                DataRailwayTicket dataRailwayTicket = railwayTicketMapper.selectOne(new LambdaQueryWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getId, invoiceId));
                String url10 = getInvoiceUrl(dataRailwayTicket.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataRailwayTicket.getInvoiceTotal()).setDetails(dataRailwayTicket.getRemark()).setInvoiceId(invoiceId).setUrL(url10);
            //客运车发票
            case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                DataPassengerCar dataPassengerCar = passengerCarMapper.selectOne(new LambdaQueryWrapper<DataPassengerCar>().eq(DataPassengerCar::getId, invoiceId));
                String url11 = getInvoiceUrl(dataPassengerCar.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataPassengerCar.getInvoiceTotal()).setDetails(dataPassengerCar.getRemark()).setInvoiceId(invoiceId).setUrL(url11);

            //过路费发票
            case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                DataTollRoads dataTollRoads = tollRoadsMapper.selectOne(new LambdaQueryWrapper<DataTollRoads>().eq(DataTollRoads::getId, invoiceId));
                String url12 = getInvoiceUrl(dataTollRoads.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataTollRoads.getInvoiceTotal()).setDetails(dataTollRoads.getRemark()).setInvoiceId(invoiceId).setUrL(url12);
            //小票/可报销其他发票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                DataReceipt dataReceipt = dataReceiptMapper.selectOne(new LambdaQueryWrapper<DataReceipt>().eq(DataReceipt::getId, invoiceId));
                String url13 = getInvoiceUrl(dataReceipt.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataReceipt.getInvoiceTotal()).setDetails(dataReceipt.getRemark()).setInvoiceId(invoiceId).setUrL(url13);
            //出行发票/滴滴
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                DataDidiItinerary didiItinerary = didiItineraryMapper.selectOne(new LambdaQueryWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getId, invoiceId));
                String ur14 = getInvoiceUrl(didiItinerary.getFileId());
                return new InvoiceWriteBackVo().setAmount(didiItinerary.getInvoiceTotal()).setDetails(didiItinerary.getRemark()).setInvoiceId(invoiceId).setUrL(ur14);
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                DataDutyPaidProof dutyPaidProof = paidProofMapper.selectOne(new LambdaQueryWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getId, invoiceId));
                String ur15 = getInvoiceUrl(dutyPaidProof.getFileId());
                return new InvoiceWriteBackVo().setAmount(dutyPaidProof.getInvoiceTotal()).setDetails(dutyPaidProof.getRemark()).setInvoiceId(invoiceId).setUrL(ur15);
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                DataCustomsImxportGoods dataCustomsImxportGoods = customsImportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getId, invoiceId));
                String ur16 = getInvoiceUrl(dataCustomsImxportGoods.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataCustomsImxportGoods.getFreight()).setDetails(dataCustomsImxportGoods.getRemark()).setInvoiceId(invoiceId).setUrL(ur16);
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                DataCustomsExportGoods dataCustomsExportGoods = customsExportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getId, invoiceId));
                String ur17 = getInvoiceUrl(dataCustomsExportGoods.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataCustomsExportGoods.getFreight()).setDetails(dataCustomsExportGoods.getRemark()).setInvoiceId(invoiceId).setUrL(ur17);
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                DataCustomsSpecialPayment dataCustomsSpecialPayment = customsSpecialPaymentMapper.selectOne(new LambdaQueryWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getId, invoiceId));
                String ur18 = getInvoiceUrl(dataCustomsSpecialPayment.getFileId());
                return new InvoiceWriteBackVo().setAmount(dataCustomsSpecialPayment.getInvoiceTotal()).setDetails(dataCustomsSpecialPayment.getRemark()).setInvoiceId(invoiceId).setUrL(ur18);
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                DataElectronicTransportationGoods transportationGoods = paymentMapper.selectOne(new LambdaQueryWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getId, invoiceId));
                String ur19 = getInvoiceUrl(transportationGoods.getFileId());
                return new InvoiceWriteBackVo().setAmount(transportationGoods.getTotalPrice()).setDetails(transportationGoods.getRemark()).setInvoiceId(invoiceId).setUrL(ur19);
            default: {
                return new InvoiceWriteBackVo();
            }
        }
    }

    private String getInvoiceUrl(String fileId) {
        DataImageFilesInfo imageFilesInfo = filesInfoMapper.selectOne(new LambdaQueryWrapper<DataImageFilesInfo>()
            .eq(DataImageFilesInfo::getFileId, fileId));
        if (ObjectUtil.isNotEmpty(imageFilesInfo)) {
            return imageFilesInfo.getIurl();
        }
        return "";
    }

    //列表查询发票
    private List<InvoiceVo> allInvoiceTypeList(List<DataImageFilesInfo> dataImageFilesInfos) {
        // 1. 过滤无效数据（fileId或invoice为空）
        List<DataImageFilesInfo> validInfos = dataImageFilesInfos.stream()
            .filter(info -> StrUtil.isNotBlank(info.getFileId()) && StrUtil.isNotBlank(info.getInvoice()))
            .collect(Collectors.toList());

        if (validInfos.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 按“发票类型 + fileId”分组（key: 发票类型, value: 该类型下的fileId集合）
        Map<String, Set<String>> typeFileIdMap = validInfos.stream()
            .collect(Collectors.groupingBy(
                DataImageFilesInfo::getInvoice, // 按发票类型分组
                Collectors.mapping(
                    DataImageFilesInfo::getFileId, // 提取fileId
                    Collectors.toSet() // 去重，避免重复查询
                )
            ));

        // 3. 存储所有关联后的结果
        List<InvoiceVo> allInvoiceVos = new ArrayList<>();

        // 4. 遍历每组，查询对应表并组装
        for (Map.Entry<String, Set<String>> entry : typeFileIdMap.entrySet()) {
            String invoiceType = entry.getKey();
            Set<String> fileIds = entry.getValue();

            // 根据发票类型，批量查询对应表
            List<?> invoiceInfos = queryInvoiceInfosByType(invoiceType, fileIds);

            // 转换为 Map（key: fileId, value: 发票信息）
            Map<String, Object> infoMap = invoiceInfos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                    obj -> getFileIdFromInvoiceInfo(obj), // 从发票信息中取fileId
                    Function.identity(),
                    (v1, v2) -> v1 // 重复fileId保留第一个
                ));

            // 组装当前类型的InvoiceVo
            List<InvoiceVo> typeVos = validInfos.stream()
                .filter(info -> info.getInvoice().equals(invoiceType)) // 只处理当前类型
                .map(info -> {
                    InvoiceVo vo = new InvoiceVo();
                    Object invoiceInfo = infoMap.get(info.getFileId());
                    if (invoiceInfo != null) {
                        setInvoiceVoFields(vo, invoiceInfo, invoiceType); // 设置业务字段
                    }
                    // 设置公共字段（来自DataImageFilesInfo）
                    vo.setInvoiceType(info.getInvoice());
                    vo.setMessage(info.getMessage());
                    vo.setCheckStatus(info.getCheckStatus());
                    vo.setStatus(info.getFileFlowStatus());
                    vo.setFilesInfo(info);
                    return vo;
                })
                .collect(Collectors.toList());

            allInvoiceVos.addAll(typeVos);
        }

        return allInvoiceVos;
    }


    // 从发票信息对象中提取fileId（需确保所有表都有fileId字段）
        private String getFileIdFromInvoiceInfo(Object invoiceInfo) {
            try {
                Method method = invoiceInfo.getClass().getMethod("getFileId");
                return (String) method.invoke(invoiceInfo);
            } catch (Exception e) {
                log.error("提取fileId失败", e);
                return null;
            }
     }
    private List<?> queryInvoiceInfosByType(String invoiceType, Set<String> fileIds) {
        switch (invoiceType) {
            //增值税、机打
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
                return ocrInfoMapper.selectList(new LambdaQueryWrapper<DataOcrInfo>().in(DataOcrInfo::getFileId, fileIds));
            //机动车
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                return motorVehicleSaleMapper.selectList(new LambdaQueryWrapper<DataMotorVehicleSale>().in(DataMotorVehicleSale::getFileId, fileIds));
            //航空运输电子客票行程单
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                return flightItineraryMapper.selectList(new LambdaQueryWrapper<DataFlightItinerary>().in(DataFlightItinerary::getFileId, fileIds));
            //二手车
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                return usedCarSalesMapper.selectList(new LambdaQueryWrapper<DataUsedCarSales>().in(DataUsedCarSales::getFileId, fileIds));
            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                return steamerTicketMapper.selectList(new LambdaQueryWrapper<DataSteamerTicket>().in(DataSteamerTicket::getFileId, fileIds));
            //医疗票明细票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                return dataMedicalTreatmentMapper.selectList(new LambdaQueryWrapper<DataMedicalTreatment>().in(DataMedicalTreatment::getFileId, fileIds));
            //非税收入类发票
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                return dataNonTaxMapper.selectList(new LambdaQueryWrapper<DataNonTax>().in(DataNonTax::getFileId, fileIds));
            //定额发票
            case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                return quotaInvoiceMapper.selectList(new LambdaQueryWrapper<DataQuotaInvoice>().in(DataQuotaInvoice::getFileId, fileIds));
            //出租车发票
            case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                return taxiTicketsMapper.selectList(new LambdaQueryWrapper<DataTaxiTickets>().in(DataTaxiTickets::getFileId, fileIds));
            //火车发票
            case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                return railwayTicketMapper.selectList(new LambdaQueryWrapper<DataRailwayTicket>().in(DataRailwayTicket::getFileId, fileIds));
            //客运车发票
            case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                return passengerCarMapper.selectList(new LambdaQueryWrapper<DataPassengerCar>().in(DataPassengerCar::getFileId, fileIds));
            //过路费发票
            case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                return tollRoadsMapper.selectList(new LambdaQueryWrapper<DataTollRoads>().in(DataTollRoads::getFileId, fileIds));
            //小票/可报销其他发票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                return dataReceiptMapper.selectList(new LambdaQueryWrapper<DataReceipt>().in(DataReceipt::getFileId, fileIds));
            //出行发票/滴滴
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                return didiItineraryMapper.selectList(new LambdaQueryWrapper<DataDidiItinerary>().in(DataDidiItinerary::getFileId, fileIds));
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                return paidProofMapper.selectList(new LambdaQueryWrapper<DataDutyPaidProof>().in(DataDutyPaidProof::getFileId, fileIds));
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                return customsImportGoodsMapper.selectList(new LambdaQueryWrapper<DataCustomsImxportGoods>().in(DataCustomsImxportGoods::getFileId, fileIds));
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                return customsExportGoodsMapper.selectList(new LambdaQueryWrapper<DataCustomsExportGoods>().in(DataCustomsExportGoods::getFileId, fileIds));
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                return customsSpecialPaymentMapper.selectList(new LambdaQueryWrapper<DataCustomsSpecialPayment>().in(DataCustomsSpecialPayment::getFileId, fileIds));
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                return paymentMapper.selectList(new LambdaQueryWrapper<DataElectronicTransportationGoods>().in(DataElectronicTransportationGoods::getFileId, fileIds));
            default: {
                return Collections.emptyList();
            }
        }
    }

    //设置列表属性
    private void setInvoiceVoFields(InvoiceVo vo, Object invoiceInfo, String invoiceType) {
        switch (invoiceType) {
            //增值税、机打
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
                DataOcrInfo dataOcrInfo = (DataOcrInfo) invoiceInfo;
                vo.setId(dataOcrInfo.getId());
                vo.setFileId(dataOcrInfo.getFileId());
                vo.setBuyerName(dataOcrInfo.getBuyerName());
                vo.setSellerName(dataOcrInfo.getSellerName());
                vo.setInvoiceDate(dataOcrInfo.getInvoiceDate());
                vo.setInvoiceTotal(dataOcrInfo.getInvoiceTotal());
                break;
            //机动车
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
            DataMotorVehicleSale dataCarSaleInvoice = (DataMotorVehicleSale) invoiceInfo;
                vo.setId(dataCarSaleInvoice.getId());
                vo.setFileId(dataCarSaleInvoice.getFileId());
                vo.setBuyerName(dataCarSaleInvoice.getBuyerName());
                vo.setInvoiceNumber(dataCarSaleInvoice.getInvoiceNumber());
                vo.setInvoiceDate(dataCarSaleInvoice.getInvoiceDate());
                vo.setInvoiceTotal(dataCarSaleInvoice.getInvoiceTotal());
                break;
            //航空运输电子客票行程单
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                DataFlightItinerary dataAirTicket = (DataFlightItinerary) invoiceInfo;
                vo.setId(dataAirTicket.getId());
                vo.setFileId(dataAirTicket.getFileId());
                vo.setUserName(dataAirTicket.getUserName());
                vo.setInvoiceDate(dataAirTicket.getInvoiceDate());
                vo.setInvoiceNumber(dataAirTicket.getInvoiceNumber());
                vo.setInvoiceTotal(dataAirTicket.getInvoiceTotal());
                break;
            //二手车
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                DataUsedCarSales dataSecondCarSaleInvoice = (DataUsedCarSales) invoiceInfo;
                vo.setId(dataSecondCarSaleInvoice.getId());
                vo.setFileId(dataSecondCarSaleInvoice.getFileId());
                vo.setBuyerName(dataSecondCarSaleInvoice.getBuyerName());
                vo.setSellerName(dataSecondCarSaleInvoice.getSellerName());
                vo.setInvoiceDate(dataSecondCarSaleInvoice.getInvoiceDate());
                vo.setInvoiceTotal(dataSecondCarSaleInvoice.getInvoiceTotal());
                break;
            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                DataSteamerTicket dataShipTicket = (DataSteamerTicket) invoiceInfo;
                vo.setId(dataShipTicket.getId());
                vo.setFileId(dataShipTicket.getFileId());
                vo.setName(dataShipTicket.getName());
                vo.setInvoiceNumber(dataShipTicket.getInvoiceNumber());
                vo.setInvoiceDate(dataShipTicket.getInvoiceDate());
                vo.setInvoiceTotal(dataShipTicket.getInvoiceTotal());
                break;
            //医疗票明细票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                DataMedicalTreatment dataMedicalTicket = (DataMedicalTreatment) invoiceInfo;
                vo.setId(dataMedicalTicket.getId());
                vo.setFileId(dataMedicalTicket.getFileId());
                vo.setInvoiceNumber(dataMedicalTicket.getInvoiceNumber());
                vo.setPayer(dataMedicalTicket.getPayer());
                vo.setVisitDate(dataMedicalTicket.getVisitDate());
                vo.setInvoiceTotal(dataMedicalTicket.getInvoiceTotal());
                break;
            //非税收入类发票
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                DataNonTax dataNonTaxRevenueReceipts = (DataNonTax) invoiceInfo;
                vo.setId(dataNonTaxRevenueReceipts.getId());
                vo.setFileId(dataNonTaxRevenueReceipts.getFileId());
                vo.setPayer(dataNonTaxRevenueReceipts.getPayer());
                vo.setPaymentCode(dataNonTaxRevenueReceipts.getPaymentCode());
                vo.setInvoiceDate(dataNonTaxRevenueReceipts.getInvoiceDate());
                vo.setInvoiceTotal(dataNonTaxRevenueReceipts.getInvoiceTotal());
                break;
            //定额发票
            case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) invoiceInfo;
                vo.setId(dataQuotaInvoice.getId());
                vo.setFileId(dataQuotaInvoice.getFileId());
                vo.setInvoiceNumber(dataQuotaInvoice.getInvoiceNumber());
                vo.setInvoiceCode(dataQuotaInvoice.getInvoiceCode());
                vo.setCity(dataQuotaInvoice.getCity());
                vo.setInvoiceTotal(dataQuotaInvoice.getInvoiceTotal());
                vo.setProvinces(dataQuotaInvoice.getProvince() + dataQuotaInvoice.getCity());
                break;
            //出租车发票
            case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                DataTaxiTickets taxiTicket = (DataTaxiTickets) invoiceInfo;
                vo.setId(taxiTicket.getId());
                vo.setFileId(taxiTicket.getFileId());
                vo.setCity(taxiTicket.getCity());
                vo.setInvoiceDate(taxiTicket.getInvoiceDate());
                vo.setInvoiceNumber(taxiTicket.getInvoiceNumber());
                vo.setInvoiceTotal(taxiTicket.getInvoiceTotal());
                vo.setProvinces(taxiTicket.getProvince() + taxiTicket.getCity());
                break;
            //火车发票
            case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                DataRailwayTicket railwayTicket = (DataRailwayTicket) invoiceInfo;
                vo.setId(railwayTicket.getId());
                vo.setFileId(railwayTicket.getFileId());
                vo.setName(railwayTicket.getName());
                vo.setInvoiceDate(railwayTicket.getInvoiceDate());
                vo.setTrainNumber(railwayTicket.getTrainNumber());
                vo.setInvoiceTotal(railwayTicket.getInvoiceTotal());
                break;
            //客运车发票
            case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                DataPassengerCar passengerCar = (DataPassengerCar) invoiceInfo;
                vo.setId(passengerCar.getId());
                vo.setFileId(passengerCar.getFileId());
                vo.setName(passengerCar.getName());
                vo.setInvoiceDate(passengerCar.getInvoiceDate());
                vo.setInvoiceNumber(passengerCar.getInvoiceNumber());
                vo.setInvoiceTotal(passengerCar.getInvoiceTotal());
                break;
            //过路费发票
            case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                DataTollRoads tollRoad = (DataTollRoads) invoiceInfo;
                vo.setId(tollRoad.getId());
                vo.setFileId(tollRoad.getFileId());
                vo.setInvoiceTime(tollRoad.getInvoiceTime());
                vo.setInvoiceNumber(tollRoad.getInvoiceNumber());
                vo.setInvoiceCode(tollRoad.getInvoiceCode());
                vo.setInvoiceTotal(tollRoad.getInvoiceTotal());
                break;
            //小票/可报销其他发票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                DataReceipt receipts = (DataReceipt) invoiceInfo;
                vo.setId(receipts.getId());
                vo.setFileId(receipts.getFileId());
                vo.setStoreName(receipts.getStoreName());
                vo.setInvoiceDate(receipts.getInvoiceDate());
                vo.setInvoiceNumber(receipts.getInvoiceNumber());
                vo.setInvoiceTotal(receipts.getInvoiceTotal());
                vo.setCode(receipts.getCode());
                vo.setNumber(receipts.getNumber());
                vo.setKind(receipts.getKind());
                vo.setTitle(receipts.getTitle());
                break;
            //出行发票/滴滴
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                DataDidiItinerary dataDidiItinerars = (DataDidiItinerary) invoiceInfo;
                vo.setId(dataDidiItinerars.getId());
                vo.setFileId(dataDidiItinerars.getFileId());
                vo.setPhone(dataDidiItinerars.getPhone());
                vo.setInvoiceDate(dataDidiItinerars.getInvoiceDate());
                vo.setTimeGetOn(dataDidiItinerars.getTimeGetOn());
                vo.setInvoiceTotal(dataDidiItinerars.getInvoiceTotal());
                break;
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                DataDutyPaidProof dataDutyPaidProofs = (DataDutyPaidProof) invoiceInfo;
                vo.setId(dataDutyPaidProofs.getId());
                vo.setFileId(dataDutyPaidProofs.getFileId());
                vo.setBuyerName(dataDutyPaidProofs.getBuyerName());
                vo.setInvoiceDate(dataDutyPaidProofs.getInvoiceDate());
                vo.setInvoiceTotal(dataDutyPaidProofs.getInvoiceTotal());
                break;
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                DataCustomsImxportGoods dataCustomsImportGoods = (DataCustomsImxportGoods) invoiceInfo;
                vo.setId(dataCustomsImportGoods.getId());
                vo.setFileId(dataCustomsImportGoods.getFileId());
                vo.setConsumptionCompanyName(dataCustomsImportGoods.getConsumptionCompanyName());
                vo.setContractNumber(dataCustomsImportGoods.getContractNumber());
                vo.setCustomsNumber(dataCustomsImportGoods.getCustomsNumber());
                vo.setDateOfApplication(dataCustomsImportGoods.getDateOfApplication());
                break;
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                DataCustomsExportGoods dataCustomsExportGoods = (DataCustomsExportGoods) invoiceInfo;
                vo.setId(dataCustomsExportGoods.getId());
                vo.setFileId(dataCustomsExportGoods.getFileId());
                vo.setConsumptionCompanyName(dataCustomsExportGoods.getConsumptionCompanyName());
                vo.setContractNumber(dataCustomsExportGoods.getContractNumber());
                vo.setCustomsNumber(dataCustomsExportGoods.getCustomsNumber());
                vo.setDateOfApplication(dataCustomsExportGoods.getDateOfApplication());
                break;
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                DataCustomsSpecialPayment dataCustomsSpecialPayments = (DataCustomsSpecialPayment) invoiceInfo;
                vo.setId(dataCustomsSpecialPayments.getId());
                vo.setFileId(dataCustomsSpecialPayments.getFileId());
                vo.setCompanyName(dataCustomsSpecialPayments.getCompanyName());
                vo.setCustomsNumber(dataCustomsSpecialPayments.getCustomsNumber());
                vo.setInvoiceDate(dataCustomsSpecialPayments.getInvoiceDate());
                vo.setInvoiceTotal(dataCustomsSpecialPayments.getInvoiceTotal());
                break;
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                DataElectronicTransportationGoods dataElectronicTransportationGoods1 = (DataElectronicTransportationGoods) invoiceInfo;
                vo.setId(dataElectronicTransportationGoods1.getId());
                vo.setFileId(dataElectronicTransportationGoods1.getFileId());
                vo.setShipper(dataElectronicTransportationGoods1.getShipper());
                vo.setElectronicReceiptNumber(dataElectronicTransportationGoods1.getElectronicReceiptNumber());
                vo.setDate(dataElectronicTransportationGoods1.getDate());
                vo.setInvoiceType(InvoiceGlorityEnumd.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE.getCode());
                vo.setInvoiceTotal(dataElectronicTransportationGoods1.getTotalPrice());
                break;
            default: {
                log.info("列表赋值类型不存在");
            }
        }
    }

}
