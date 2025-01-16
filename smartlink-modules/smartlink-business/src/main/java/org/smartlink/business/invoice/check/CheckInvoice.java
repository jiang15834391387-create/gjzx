package org.smartlink.business.invoice.check;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.util.StringUtils;
import org.smartlink.business.enumd.CheckInvoiceStatusEnumd;
import org.smartlink.business.invoice.factory.CheckFactory;
import org.smartlink.business.invoice.service.IDataOcrInfoServices;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.exception.CheckException;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.*;
import org.smartlink.common.entity.domain.business.service.*;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
/**
 * @Description: 查验
 * @Author: Mr.Meng
 *
 */
@Slf4j
@Component
public class CheckInvoice {
    private final IDataOcrInfoServices dataOcrInfoService;
    private final IDataImageFilesInfoService imageFilesInfoService;
    private final DataOcrDetailsMapper detailsMapper;
    private final DataOcrInfoMapper ocrInfoMapper;
    private final DataUsedCarSalesMapper usedCarSalesMapper;
    private final DataMotorVehicleSaleMapper motorVehicleSaleMapper;
    private final DataRailwayTicketMapper railwayTicketMapper;
    private final DataFlightItineraryMapper flightItineraryMapper;
    private final DataFlightsItineraryDetailMapper flightsItineraryDetailMapper;
    private final IDataRailwayTicketService dataRailwayTicketService;
    private final IDataFlightItineraryService dataFlightItineraryService;
    private final IDataMedicalTreatmentService dataMedicalTreatmentDetailService;
    private final IDataMotorVehicleSaleService dataMotorVehicleSaleService;
    private final IDataUsedCarSalesService dataUsedCarSalesService;
    public CheckInvoice(IDataOcrInfoServices dataOcrInfoService, IDataImageFilesInfoService imageFilesInfoService, DataOcrDetailsMapper detailsMapper, DataOcrInfoMapper ocrInfoMapper, DataUsedCarSalesMapper usedCarSalesMapper, DataMotorVehicleSaleMapper motorVehicleSaleMapper, DataRailwayTicketMapper railwayTicketMapper, DataFlightItineraryMapper flightItineraryMapper, DataFlightsItineraryDetailMapper flightsItineraryDetailMapper, IDataRailwayTicketService dataRailwayTicketService, IDataFlightItineraryService dataFlightItineraryService, IDataMedicalTreatmentService dataMedicalTreatmentDetailService, IDataMotorVehicleSaleService dataMotorVehicleSaleService, IDataUsedCarSalesService dataUsedCarSalesService) {
        this.dataOcrInfoService = dataOcrInfoService;
        this.imageFilesInfoService = imageFilesInfoService;
        this.detailsMapper = detailsMapper;
        this.ocrInfoMapper = ocrInfoMapper;
        this.usedCarSalesMapper = usedCarSalesMapper;
        this.motorVehicleSaleMapper = motorVehicleSaleMapper;
        this.railwayTicketMapper = railwayTicketMapper;
        this.flightItineraryMapper = flightItineraryMapper;
        this.flightsItineraryDetailMapper = flightsItineraryDetailMapper;
        this.dataRailwayTicketService = dataRailwayTicketService;
        this.dataFlightItineraryService = dataFlightItineraryService;
        this.dataMedicalTreatmentDetailService = dataMedicalTreatmentDetailService;
        this.dataMotorVehicleSaleService = dataMotorVehicleSaleService;
        this.dataUsedCarSalesService = dataUsedCarSalesService;
    }


    //查验方法
    public void check(boolean checkOff,IdentificationData identificationDatum,  DataImageFilesInfo filesInfo) throws IOException, InvocationTargetException, IllegalAccessException {
        // 判断小程序入口上传的发票 如果没有开启小程序查验开关，不允许走查验逻辑
//        if(wechatUpload && !Boolean.parseBoolean(fileUploadDTO.getIsCheck())){
//            return;
//        }
        // 判断PC端入口上传的发票 如果没有开启PC端查验开关，不允许走查验逻辑
        if(!checkOff){
            return;
        }
        //获取发票类型
        String invoiceType = filesInfo.getInvoice();
        if (invoiceType.equals(InvoiceConstants.GLORITY_TAX_SPECIAL_CODE)
            || invoiceType.equals(InvoiceConstants.GLORITY_TAX_CODE)
            || invoiceType.equals(InvoiceConstants.GLORITY_ELECTRONIC_CODE)
            || invoiceType.equals(InvoiceConstants.GLORITY_ROLL_TICKET_CODE)
            || invoiceType.equals(InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE)
            || invoiceType.equals(InvoiceConstants.GLORITY_USED_CAR_SALES_CODE)
            || invoiceType.equals(InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE)
            || invoiceType.equals(InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE)
            || invoiceType.equals(InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE)
            || invoiceType.equals(InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE)
            || invoiceType.equals(InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE)
        ){
            InvoiceCheckParamDTO invoiceCheckParamDTO=null;
            switch (invoiceType){
                case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                    log.info("查验二手车发票");
                    final DataUsedCarSales dataUsedCarSales =dataUsedCarSalesService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(dataUsedCarSales.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(dataUsedCarSales.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(dataUsedCarSales.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    invoiceCheckParamDTO.setPretax_amount(dataUsedCarSales.getTotalUppercase());
                    break;
                case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                    log.info("查验机动车发票");
                    final DataMotorVehicleSale dataMotorVehicleSale =dataMotorVehicleSaleService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(dataMotorVehicleSale.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(dataMotorVehicleSale.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(dataMotorVehicleSale.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    invoiceCheckParamDTO.setPretax_amount(dataMotorVehicleSale.getPreTaxAmount());
                    break;
                case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                    log.info("查验火车发票发票");
                    final DataRailwayTicket railwayTicket =dataRailwayTicketService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setNumber(railwayTicket.getInvoiceNumber());
                    invoiceCheckParamDTO.setTotal(railwayTicket.getInvoiceTotal());
                    invoiceCheckParamDTO.setDate_of_issue(railwayTicket.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    break;
                case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                    log.info("查验航空运输电子客票发票");
                    final DataFlightItinerary dataFlightItinerary = dataFlightItineraryService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setReceipt_numbe(dataFlightItinerary.getInvoiceNumber());
                    invoiceCheckParamDTO.setTotal(dataFlightItinerary.getInvoiceTotal());
                    invoiceCheckParamDTO.setDate(dataFlightItinerary.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    break;
                case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                    log.info("查验机打发票发票");
                    final DataOcrInfo ocrInfoss = this.dataOcrInfoService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(ocrInfoss.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(ocrInfoss.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(ocrInfoss.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    invoiceCheckParamDTO.setTotal(ocrInfoss.getTotalUppercase());
                    invoiceCheckParamDTO.setSeller_tax_id(ocrInfoss.getSellerNo());
                    invoiceCheckParamDTO.setArea(ocrInfoss.getProvince());
                    break;
                case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                    log.info("查验非税收入票据发票");
                    final DataMedicalTreatment medicalTreatment = this.dataMedicalTreatmentDetailService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(medicalTreatment.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(medicalTreatment.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(medicalTreatment.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    invoiceCheckParamDTO.setPretax_amount(medicalTreatment.getInvoiceTotal());
                    String checkCodess = medicalTreatment.getCheckCode();
                    if (StringUtils.hasText(checkCodess) && checkCodess.length() > 5) {
                        checkCodess = checkCodess.substring(checkCodess.length() - 6);
                    }
                    invoiceCheckParamDTO.setCheck_code(checkCodess);
                    break;
                case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
                    log.info("查验增值税专票发票");
                    final DataOcrInfo ocrInfo = this.dataOcrInfoService.getByFileId(filesInfo.getFileId());
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(ocrInfo.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(ocrInfo.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(ocrInfo.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    invoiceCheckParamDTO.setElectron_mark(Integer.valueOf(ocrInfo.getElectronicMark()));
                    invoiceCheckParamDTO.setPretax_amount(ocrInfo.getPretaxAmount());
                    break;
                case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                case InvoiceConstants.GLORITY_TAX_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
                case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                    log.info("查验增值税发票");
                    final DataOcrInfo ocrInfos = this.dataOcrInfoService.getByFileId(filesInfo.getFileId());
                    if (StrUtil.isNotBlank(ocrInfos.getBlockChain())){
                        if (ocrInfos.getBlockChain().equals("1")){
                            invoiceCheckParamDTO.setElectron_mark(Integer.valueOf(ocrInfos.getBlockChain()));
                        }
                        if (StrUtil.isNotBlank(ocrInfos.getSellerNo())){
                            invoiceCheckParamDTO.setSeller_tax_id(ocrInfos.getSellerNo());
                        }
                        if (StrUtil.isNotBlank(ocrInfos.getProvince())){
                            invoiceCheckParamDTO.setArea(ocrInfos.getProvince());

                        }
                    }
                    if (invoiceType.equals(InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE)||invoiceType.equals(InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE)){
                        invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                        invoiceCheckParamDTO.setNumber(ocrInfos.getInvoiceNumber());
                        invoiceCheckParamDTO.setTotal(ocrInfos.getTotalUppercase());
                        invoiceCheckParamDTO.setDate(ocrInfos.getInvoiceDate());
                        invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                        break;
                    }
                    invoiceCheckParamDTO = new InvoiceCheckParamDTO();
                    invoiceCheckParamDTO.setCode(ocrInfos.getInvoiceCode());
                    invoiceCheckParamDTO.setNumber(ocrInfos.getInvoiceNumber());
                    invoiceCheckParamDTO.setDate(ocrInfos.getInvoiceDate());
                    invoiceCheckParamDTO.setType(filesInfo.getInvoice());
                    String checkCodes = ocrInfos.getCheckCode();
                    if (StringUtils.hasText(checkCodes) && checkCodes.length() > 5) {
                        checkCodes = checkCodes.substring(checkCodes.length() - 6);
                    }
                    invoiceCheckParamDTO.setCheck_code(checkCodes);

                    break;
                default:
                    throw new CheckException("发票类型有误!");
            }
            this.checkInvoice(filesInfo,invoiceCheckParamDTO);

    }


 }
    //传递查验参数调用查验工厂
    public BaseEntity checkInvoice(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO invoiceCheckParamDTO) throws InvocationTargetException, IllegalAccessException, IOException {
        BaseEntity baseEntity= CheckFactory.instance().checkInvoke(filesInfo, invoiceCheckParamDTO);
        if (filesInfo.getFileStatus().equals(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode())&&filesInfo.getCheckStatus().equals(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode())) {
           //如果是增值税（专用/普通/电子专用）或增值税电子普通发票 或区块链电子发票  或机打发票 或增值税普通发票(卷票)或数电票(增值税专用发票/普通发票)
            if (filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_TAX_SPECIAL_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_TAX_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_ELECTRONIC_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_ROLL_TICKET_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE)
                || filesInfo.getInvoice().equals(InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE)) {
                log.info("fileinfo", filesInfo.getInvoice());
                ocrConversionAlter(baseEntity,filesInfo);
                return baseEntity;
            }
            if (filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_USED_CAR_SALES_CODE)){
                //转换后二手车销售统一发票
                usedCsrConversionAlter(baseEntity,filesInfo);
                return baseEntity;
            }
            if (filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE)){
                //转换后机动车销售统一发票
                motorVehicleSaleConversionAlter(baseEntity,filesInfo);
                return baseEntity;
            }
            if (filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE)){
                //转换后火车票
                railwayTicketConversionAlter(baseEntity,filesInfo);
                return baseEntity;
            }
            if (filesInfo.getInvoice().equals(InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE)){
                //转换后航空运输电子客运行程单基本信息
                flightItineraryConversionAlter(baseEntity,filesInfo);
                return baseEntity;
            }

        }
        log.info("查验发票查验失败结果：{}", baseEntity);
        return baseEntity;
    }

    public void flightItineraryConversionAlter(BaseEntity baseEntity, DataImageFilesInfo filesInfo) throws InvocationTargetException, IllegalAccessException {
        if (ObjectUtil.isEmpty(baseEntity)){
            log.info("航空运输电子客运行程单基本信息查验转换后结果为空");
            return;
        }
        log.info("修改查验转换后的航空运输电子客运行程单基本信息信息：{}", baseEntity);
        //创建航空运输电子客运行程单基本信息
        DataFlightItinerary dataFlightItinerary = new DataFlightItinerary();
        BeanUtils.copyProperties(baseEntity, dataFlightItinerary);
        dataFlightItinerary=BeanUtil.toBean(baseEntity, DataFlightItinerary.class);
        List<DataFlightsItineraryDetail> list = new ArrayList<>();
        List<DataFlightsItineraryDetail> detailList = dataFlightItinerary.getFlightItineraryDetails();
        if (CollectionUtil.isNotEmpty(detailList)){
            for (DataFlightsItineraryDetail dataFlightsItineraryDetail : detailList) {
                DataFlightsItineraryDetail flightsItineraryDetail = new DataFlightsItineraryDetail();
                flightsItineraryDetail=BeanUtil.toBean(dataFlightsItineraryDetail, DataFlightsItineraryDetail.class);
                flightsItineraryDetail.setFileId(filesInfo.getFileId());
                list.add(flightsItineraryDetail);
            }
        }
        //根据fileId进行修改航空电子单基本信息
        flightItineraryMapper.update(dataFlightItinerary, new LambdaUpdateWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getFileId, filesInfo.getFileId()));
        for (DataFlightsItineraryDetail flightsItineraryDetail : list) {
            flightsItineraryDetailMapper.update(flightsItineraryDetail, new LambdaUpdateWrapper<DataFlightsItineraryDetail>().eq(DataFlightsItineraryDetail::getFileId, filesInfo.getFileId()));
        }

    }

    // 转换后火车票
    private void railwayTicketConversionAlter(BaseEntity baseEntity, DataImageFilesInfo filesInfo) throws InvocationTargetException, IllegalAccessException {
        if (ObjectUtil.isEmpty(baseEntity)){
            log.info("火车票查验转换后结果为空");
            return;
        }
        log.info("修改查验转换后的火车票信息：{}", baseEntity);
        DataRailwayTicket dataRailwayTicket = new DataRailwayTicket();
        dataRailwayTicket=BeanUtil.toBean(baseEntity, DataRailwayTicket.class);


        //根据fileId进行修改
        railwayTicketMapper.update(dataRailwayTicket,new LambdaUpdateWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getFileId, filesInfo.getFileId()));

    }

    // 转换后机动车销售统一发票
    private void motorVehicleSaleConversionAlter(BaseEntity baseEntity, DataImageFilesInfo filesInfo) throws InvocationTargetException, IllegalAccessException {
        if (ObjectUtil.isEmpty(baseEntity)){
            log.info("机动车销售统一发票查验转换后结果为空");
            return;
        }
        log.info("修改查验转换后的机动车信息：{}", baseEntity);
        DataMotorVehicleSale dataMotorVehicleSale = new DataMotorVehicleSale();
        dataMotorVehicleSale=BeanUtil.toBean(baseEntity, DataMotorVehicleSale.class);
        //根据fileId进行修改
        motorVehicleSaleMapper.update(dataMotorVehicleSale,new LambdaUpdateWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getFileId, filesInfo.getFileId()));

    }

    //转换后机动二手车销售统一发票
    private void usedCsrConversionAlter(BaseEntity baseEntity, DataImageFilesInfo filesInfo) throws InvocationTargetException, IllegalAccessException {
        if (ObjectUtil.isEmpty(baseEntity)){
            log.info("二手车销售统一发票查验转换后结果为空");
            return;
        }
        log.info("修改查验转换后的二手车信息：{}", baseEntity);
        DataUsedCarSales dataUsedCarSales = new DataUsedCarSales();
        dataUsedCarSales=BeanUtil.toBean(baseEntity, DataUsedCarSales.class);
        //根据fileId进行修改
        usedCarSalesMapper.update(dataUsedCarSales,new LambdaUpdateWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, filesInfo.getFileId()));
    }

    //转换后ocr信息修改
    public void ocrConversionAlter(BaseEntity baseEntity,DataImageFilesInfo filesInfo) throws InvocationTargetException, IllegalAccessException {
        if (ObjectUtil.isEmpty(baseEntity)){
            log.info("ocr查验转换后结果为空");
            return;
        }
        log.info("修改查验转换后的OCR信息：{}", baseEntity);
        DataOcrInfo dataOcrInfo = new DataOcrInfo();
        dataOcrInfo = BeanUtil.toBean(baseEntity, DataOcrInfo.class);
        log.info("修改查验转换后的OCR信息：{}", dataOcrInfo);
        List<DataOcrDetails> arrayList = new ArrayList<>();
        if (dataOcrInfo.getDetails() != null) {
            List<DataOcrDetails> details = dataOcrInfo.getDetails();
            for (DataOcrDetails ocrDetails : details) {
                DataOcrDetails ocrDetail = new DataOcrDetails();
                ocrDetail = BeanUtil.toBean(ocrDetails, DataOcrDetails.class);
                ocrDetail.setFileId(filesInfo.getFileId());
                arrayList.add(ocrDetail);
            }
        }
        //根据file_id修改ocr基本信息
        ocrInfoMapper.update(dataOcrInfo, new LambdaUpdateWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, filesInfo.getFileId()));
        for (DataOcrDetails detail : arrayList) {
            detailsMapper.update(detail, new LambdaUpdateWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, filesInfo.getFileId()));
        }
    }
}
