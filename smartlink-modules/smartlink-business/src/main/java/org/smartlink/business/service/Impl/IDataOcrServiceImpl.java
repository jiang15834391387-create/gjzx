package org.smartlink.business.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.smartlink.business.service.IDataOcrService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.domain.bo.DataTollRoadsBo;
import org.smartlink.common.entity.domain.business.mapper.DataDidiItineraryDetailsMapper;
import org.smartlink.common.entity.domain.business.mapper.DataImageFilesInfoMapper;
import org.smartlink.common.entity.domain.business.mapper.DataOcrDetailsMapper;
import org.smartlink.common.entity.domain.business.service.*;
import org.smartlink.common.entity.domain.business.service.Impl.DataUsedCarSalesServiceImpl;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.glority.conversion.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class IDataOcrServiceImpl implements IDataOcrService {

    private final IDataOcrInfoService iDataOcrInfoService;
    private final IDataOcrDetailsService iDataOcrDetailsService;

    private final IDataMotorVehicleSaleService iDataMotorVehicleSaleService;
    private final IDataFlightItineraryService iDataFlightItineraryService;
    private final IDataFlightsItineraryDetailService iDataFlightsItineraryDetailService;
    private final IDataUsedCarSalesService iDataUsedCarSalesService;
    private final DataUsedCarSalesServiceImpl dataUsedCarSalesService;
    private final IDataSteamerTicketService iDataSteamerTicketService;
    private final IDataMedicalTreatmentService iDataMedicalTreatmentService;
    private final IDataMedicalTreatmentDetailService iDataMedicalTreatmentDetailService;
    private final IDataQuotaInvoiceService iDataQuotaInvoiceService;
    private final IDataTaxiTicketsService iDataTaxiTicketsService;
    private final IDataRailwayTicketService iDataRailwayTicketService;
    private final IDataPassengerCarService iDataPassengerCarService;
    private final IDataTollRoadsService iDataTollRoadsService;
    private final IDataReceiptService iDataReceiptService;
    private final IDataDidiItineraryService iDataDidiItineraryService;
    private final IDataDidiItineraryDetailsService iDataDidiItineraryDetailsService;
    private final IDataDutyPaidProofService iDataDutyPaidProofService;
    private final IDataDutyPaidProofDetailsService iDataDutyPaidProofDetailsService;
    private final IDataNonTaxService iDataNonTaxService;
    private final IDataCustomsImxportGoodsService iDataCustomsImxportGoodsService;

    private final IDataCustomsImportGoodsDetailService iDataCustomsImportGoodsDetailService;
    private final IDataCustomsExportGoodsService iDataCustomsExportGoodsService;
    private final IDataCustomsExportGoodsDetailService iDataCustomsExportGoodsDetailService;
    private final IDataCustomsSpecialPaymentService iDataCustomsSpecialPaymentService;
    private final IDataElectronicTransportationGoodsService iDataElectronicTransportationGoodsService;


    /**
     * ocr信息插入或保存  （插入会根据FileID修改文件表的type为传入type，修改根据ID修改）
     *
     * @param key        发票类型
     * @param obj 发票实体
     * @return 0 失败 1 成功
     */
    @Override
    public R<T> ocrInsert(String key, Object obj) throws Exception {
        if (key.isEmpty()){
            return R.warn("发票类型识别为空！");
        }
        switch (key) {
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
                DataOcrInfo dataOcrInfo = new DataOcrInfo();
                BeanUtils.copyProperties(obj,dataOcrInfo);
                Boolean info = iDataOcrInfoService.insert(dataOcrInfo);
                log.info("增值税相关信息入库:{}", info);
                List<DataOcrDetails> list = dataOcrInfo.getDetails();
                Boolean detail = iDataOcrDetailsService.insertBatch(list);
                log.info("增值税相关明细信息入库:{}", detail);
                break;
            //机动车
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                DataMotorVehicleSale dataMotorVehicleSale = new DataMotorVehicleSale();
                BeanUtils.copyProperties(obj,dataMotorVehicleSale);
                Boolean dataMotorVehicleSaleInfo = iDataMotorVehicleSaleService.insert(dataMotorVehicleSale);
                log.info("机动车信息入库:{}", dataMotorVehicleSaleInfo);
                break;
            //机票
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                DataFlightItinerary dataFlightItinerary = new DataFlightItinerary();
                BeanUtils.copyProperties(obj,dataFlightItinerary);
                Boolean dataFlightItineraryInfo = iDataFlightItineraryService.insert(dataFlightItinerary);
                log.info("机票信息入库:{}", dataFlightItineraryInfo);
                List<DataFlightsItineraryDetail> flightItinerarylist = dataFlightItinerary.getDetails();
                Boolean flightItinerarylistdetail = iDataFlightsItineraryDetailService.insertBatch(flightItinerarylist);
                log.info("机票信息入库:{}", flightItinerarylistdetail);
                break;
            //二手车
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                DataUsedCarSales dataUsedCarSales = new DataUsedCarSales();
                BeanUtils.copyProperties(obj,dataUsedCarSales);
                Boolean dataUsedCarSalesInfo = iDataUsedCarSalesService.insert(dataUsedCarSales);
                log.info("二手车信息入库:{}", dataUsedCarSalesInfo);
                break;
            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                DataSteamerTicket dataSteamerTicket = new DataSteamerTicket();
                BeanUtils.copyProperties(obj,dataSteamerTicket);
                Boolean dataSteamerTicketInfo = iDataSteamerTicketService.insert(dataSteamerTicket);
                log.info("船票信息入库:{}", dataSteamerTicketInfo);
                break;
            //医疗票明细票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            //医疗票
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                DataMedicalTreatment dataMedicalTreatment = new DataMedicalTreatment();
                BeanUtils.copyProperties(obj,dataMedicalTreatment);
                Boolean dataMedicalTreatmentInfo = iDataMedicalTreatmentService.insert(dataMedicalTreatment);
                log.info("医疗票信息入库:{}", dataMedicalTreatmentInfo);
                List<DataOcrDetails> dataMedicalTreatmentlist = dataMedicalTreatment.getDetails();
                Boolean detailDataMedicalTreatmentlist = iDataOcrDetailsService.insertBatch(dataMedicalTreatmentlist);
                log.info("医疗票相关明细信息入库:{}", detailDataMedicalTreatmentlist);
                break;
            //定额发票
            case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                DataQuotaInvoice dataQuotaInvoice = new DataQuotaInvoice();
                BeanUtils.copyProperties(obj,dataQuotaInvoice);
                Boolean dataQuotaInvoiceInfo = iDataQuotaInvoiceService.insert(dataQuotaInvoice);
                log.info("定额发票信息入库:{}", dataQuotaInvoiceInfo);
                break;
            //出租车发票
            case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                DataTaxiTickets dataTaxiTickets = new DataTaxiTickets();
                BeanUtils.copyProperties(obj,dataTaxiTickets);
                Boolean dataTaxiTicketsInfo = iDataTaxiTicketsService.insert(dataTaxiTickets);
                log.info("出租车发票信息入库:{}", dataTaxiTicketsInfo);
                break;
            //火车发票
            case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                DataRailwayTicket dataRailwayTicket = new DataRailwayTicket();
                BeanUtils.copyProperties(obj,dataRailwayTicket);
                Boolean dataRailwayTicketInfo = iDataRailwayTicketService.insert(dataRailwayTicket);
                log.info("火车发票信息入库:{}", dataRailwayTicketInfo);
                break;
            //客运车发票
            case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                DataPassengerCar dataPassengerCar = new DataPassengerCar();
                BeanUtils.copyProperties(obj,dataPassengerCar);
                Boolean dataPassengerCarInfo = iDataPassengerCarService.insert(dataPassengerCar);
                log.info("客运车发票信息入库:{}", dataPassengerCarInfo);
                break;
            //过路费发票
            case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                DataTollRoads dataTollRoads = new DataTollRoads();
                BeanUtils.copyProperties(obj,dataTollRoads);
                Boolean dataTollRoadsInfo = iDataTollRoadsService.insert(dataTollRoads);
                log.info("过路费发票信息入库:{}", dataTollRoadsInfo);
                break;
            //小票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
                DataReceipt dataReceipt = new DataReceipt();
                BeanUtils.copyProperties(obj,dataReceipt);
                Boolean dataReceiptInfo = iDataReceiptService.insert(dataReceipt);
                log.info("小票信息入库:{}", dataReceiptInfo);
                break;
            //出行发票
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                DataDidiItinerary dataDidiItinerary = new DataDidiItinerary();
                BeanUtils.copyProperties(obj,dataDidiItinerary);
                Boolean dataDidiItineraryInfo = iDataDidiItineraryService.insert(dataDidiItinerary);
                log.info("出行发票信息入库:{}", dataDidiItineraryInfo);
                List<DataDidiItineraryDetails> DataDidiItineraryDetailslist = dataDidiItinerary.getDetails();
                Boolean dataDidiItineraryDetailslistDetail = iDataDidiItineraryDetailsService.insertBatch(DataDidiItineraryDetailslist);
                log.info("出行发票明细信息入库:{}", dataDidiItineraryDetailslistDetail);
                break;
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                DataDutyPaidProof dataDutyPaidProof = new DataDutyPaidProof();
                BeanUtils.copyProperties(obj,dataDutyPaidProof);
                Boolean dataDutyPaidProofInfo = iDataDutyPaidProofService.insert(dataDutyPaidProof);
                log.info("完税证明发票信息入库:{}", dataDutyPaidProofInfo);
                List<DataDutyPaidProofDetails> dataDutyPaidProofDetailslist = dataDutyPaidProof.getDetails();
                Boolean dataDutyPaidProofDetailslistDetail = iDataDutyPaidProofDetailsService.insertBatch(dataDutyPaidProofDetailslist);
                log.info("完税证明发票明细信息入库:{}", dataDutyPaidProofDetailslistDetail);
                break;
            //非税收入类发票
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                DataNonTax dataNonTax = new DataNonTax();
                BeanUtils.copyProperties(obj,dataNonTax);
                Boolean dataNonTaxInfo = iDataNonTaxService.insert(dataNonTax);
                log.info("非税收入类发票信息入库:{}", dataNonTaxInfo);
                List<DataOcrDetails> dataNonTaxlist = dataNonTax.getDetails();
                Boolean dataNonTaxDetailslistDetails = iDataOcrDetailsService.insertBatch(dataNonTaxlist);
                log.info("非税收入类发票明细信息入库:{}", dataNonTaxDetailslistDetails);
                break;
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                DataCustomsImxportGoods dataCustomsImxportGoods = new DataCustomsImxportGoods();
                BeanUtils.copyProperties(obj,dataCustomsImxportGoods);
                Boolean dataCustomsImxportGoodsInfo = iDataCustomsImxportGoodsService.insert(dataCustomsImxportGoods);
                log.info("海关进口货物报关单发票信息入库:{}", dataCustomsImxportGoodsInfo);
                List<DataCustomsImportGoodsDetail> dataCustomsImportGoodsDetaillist = dataCustomsImxportGoods.getDetails();
                Boolean dataCustomsImportGoodsDetaillistDetail = iDataCustomsImportGoodsDetailService.insertBatch(dataCustomsImportGoodsDetaillist);
                log.info("海关进口货物报关单发票明细信息入库:{}", dataCustomsImportGoodsDetaillistDetail);
                break;
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                DataCustomsExportGoods dataCustomsExportGoods = new DataCustomsExportGoods();
                BeanUtils.copyProperties(obj,dataCustomsExportGoods);
                Boolean dataCustomsExportGoodsInfo = iDataCustomsExportGoodsService.insert(dataCustomsExportGoods);
                log.info("海关出口货物报关单发票信息入库:{}", dataCustomsExportGoodsInfo);
                List<DataCustomsExportGoodsDetail> dataCustomsExportGoodslist = dataCustomsExportGoods.getDetails();
                Boolean dataCustomsExportGoodslistDetail = iDataCustomsExportGoodsDetailService.insertBatch(dataCustomsExportGoodslist);
                log.info("海关出口货物报关单发票明细信息入库:{}", dataCustomsExportGoodslistDetail);
                break;
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                DataCustomsSpecialPayment dataCustomsSpecialPayment = new DataCustomsSpecialPayment();
                BeanUtils.copyProperties(obj,dataCustomsSpecialPayment);
                Boolean dataCustomsSpecialPaymentInfo = iDataCustomsSpecialPaymentService.insert(dataCustomsSpecialPayment);
                log.info("海关专用缴款书发票信息入库:{}", dataCustomsSpecialPaymentInfo);
                List<DataOcrDetails> dataCustomsSpecialPaymentlist = dataCustomsSpecialPayment.getDetails();
                Boolean dataCustomsSpecialPaymentlistDetail = iDataOcrDetailsService.insertBatch(dataCustomsSpecialPaymentlist);
                log.info("海关专用缴款书发票明细信息入库:{}", dataCustomsSpecialPaymentlistDetail);
                break;
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                DataElectronicTransportationGoods dataElectronicTransportationGoods = new DataElectronicTransportationGoods();
                BeanUtils.copyProperties(obj,dataElectronicTransportationGoods);
                Boolean dataElectronicTransportationGoodsInfo = iDataElectronicTransportationGoodsService.insert(dataElectronicTransportationGoods);
                log.info("货物运输电子收款凭证发票信息入库:{}", dataElectronicTransportationGoodsInfo);
                List<DataOcrDetails> dataElectronicTransportationGoodslist = dataElectronicTransportationGoods.getDetails();
                Boolean dataElectronicTransportationGoodslistDetail = iDataOcrDetailsService.insertBatch(dataElectronicTransportationGoodslist);
                log.info("货物运输电子收款凭证发票明细信息入库:{}", dataElectronicTransportationGoodslistDetail);
                break;
            default:
                // 当没有匹配到任何case分支时执行的默认逻辑
                return R.ok("没有匹配到发票处理流程！");
        }

        return R.ok();

    }

}
