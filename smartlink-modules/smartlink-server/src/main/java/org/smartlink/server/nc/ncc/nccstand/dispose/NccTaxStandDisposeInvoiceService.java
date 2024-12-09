package org.smartlink.server.nc.ncc.nccstand.dispose;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.*;
import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.dto.UploadInvoiceForNccRequest;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageTree;
import org.smartlink.server.nc.domain.invoice.*;
import org.smartlink.server.nc.domain.invoice.bo.DataFlightsBo;
import org.smartlink.server.nc.domain.invoice.bo.DataOcrDetailsBo;
import org.smartlink.server.nc.domain.invoice.vo.DataFlightsVo;
import org.smartlink.server.nc.domain.invoice.vo.DataOcrDetailsVo;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.ncc.nccstand.NccTaxStandService;
import org.smartlink.server.nc.ncc.nccstand.request.allinvoice.StandAllInvoiceRequest;
import org.smartlink.server.nc.ncc.nccstand.request.taxinvoice.StandTaxInvoiceRequest;
import org.smartlink.server.nc.ncc.nccstand.request.taxinvoice.StandTaxInvoiceRequestData;
import org.smartlink.server.nc.ncc.nccstand.response.allinvoice.StandAllInvoiceResponse;
import org.smartlink.server.nc.ncc.nccstand.response.allinvoice.StandAllInvoiceResponseData;
import org.smartlink.server.nc.ncc.nccstand.response.taxinvoice.StandTaxInvoiceResponse;
import org.smartlink.server.nc.ncc.nccstand.response.taxinvoice.StandTaxInvoiceResponseData;
import org.smartlink.server.nc.properties.NccParamProperties;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.token.response.Token;
import org.smartlink.server.nc.utils.BatchIdUtils;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.smartlink.server.nc.utils.file.FileUtils;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: NCC保存发票台账逻辑
 * @author: L
 * @create:
 **/
@AllArgsConstructor
@Component
@Slf4j
public class NccTaxStandDisposeInvoiceService {

    private IDataOcrService dataOcrService;
    private IDataImageFilesInfoService dataImageFilesInfoService;
    private IDataOcrDetailsService dataOcrDetailsService;
    private IDataFlightsService dataFlightsService;
    private IDataImageTreeService imageTreeService;
    private IDataCurrentTaskService currentTaskService;


    public DataImageFilesInfo invoiceInfoStandBusiness(UploadInvoiceForNccRequest uploadInvoiceForNCCRequest, String invoiceType, BaseEntity baseInfo, Token token) throws Exception{
        NccParamProperties paramProperties = uploadInvoiceForNCCRequest.getParamProperties();
        String interFaceType = paramProperties.getInterFaceType();
        DataImageFilesInfo dataImageFilesInfo = uploadInvoiceForNCCRequest.getDataImageFilesInfo();

        if(StrUtil.equals(NcConstant.ALL_INVOICE_INTERFACE,interFaceType)){
            // 调用NCC业务系统全票种发票保存逻辑
            StandAllInvoiceRequest standAllInvoiceRequest = new StandAllInvoiceRequest();
            standAllInvoiceRequest.setUserid(uploadInvoiceForNCCRequest.getUserId());
            standAllInvoiceRequest.setOrgCode(uploadInvoiceForNCCRequest.getOrgCode());
            standAllInvoiceRequest.setDatasource(paramProperties.getDataSource());
            standAllInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
            standAllInvoiceRequest.setBilltype(uploadInvoiceForNCCRequest.getBillType());
            standAllInvoiceRequest.setTransitype(uploadInvoiceForNCCRequest.getPkBillType());
            standAllInvoiceRequest.setBillid(uploadInvoiceForNCCRequest.getBillId());
            JSONObject requestData = this.getRequestData(invoiceType, uploadInvoiceForNCCRequest.getUploadBusinessType(), baseInfo,dataImageFilesInfo);
            standAllInvoiceRequest.setData(ListUtil.of(requestData));
            uploadInvoiceForNCCRequest.setDataImageFilesInfo(dataImageFilesInfo);
            dataImageFilesInfo = this.allInvoiceInfoSaveToStand(invoiceType,baseInfo,uploadInvoiceForNCCRequest,standAllInvoiceRequest,token);
        }else{
            // 调用NCC业务系统增值税发票保存逻辑
            StandTaxInvoiceRequest standTaxInvoiceRequest = new StandTaxInvoiceRequest();
            standTaxInvoiceRequest.setBilltype(uploadInvoiceForNCCRequest.getBillType());
            standTaxInvoiceRequest.setDatasource(paramProperties.getDataSource());
            standTaxInvoiceRequest.setFactorycode(paramProperties.getFactoryCode());
            standTaxInvoiceRequest.setOrgCode(uploadInvoiceForNCCRequest.getOrgCode());
            //standTaxInvoiceRequest.setPk_org(uploadInvoiceForNCCRequest.getPk_org());
            standTaxInvoiceRequest.setTransitype(uploadInvoiceForNCCRequest.getPkBillType());
            standTaxInvoiceRequest.setUserid(uploadInvoiceForNCCRequest.getUserId());
            List<StandTaxInvoiceRequestData> dataList = new ArrayList<>();
            StandTaxInvoiceRequestData data = new StandTaxInvoiceRequestData();
            if(baseInfo instanceof DataMotorVehicleSale){
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseInfo;
                data.setFpDm(dataMotorVehicleSale.getInvoiceCode());
                data.setFpHm(dataMotorVehicleSale.getInvoiceNumber());
                data.setSaveToken(dataMotorVehicleSale.getSaveToken());
            }else{
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseInfo;
                data.setFpDm(dataOcrInfo.getInvoiceCode());
                data.setFpHm(dataOcrInfo.getInvoiceNumber());
                data.setSaveToken(dataOcrInfo.getSaveToken());
            }
            data.setBillid(uploadInvoiceForNCCRequest.getBillId());
            dataList.add(data);
            standTaxInvoiceRequest.setData(dataList);
            uploadInvoiceForNCCRequest.setDataImageFilesInfo(dataImageFilesInfo);
            try {
                dataImageFilesInfo = this.taxInvoiceInfoSaveToStand(uploadInvoiceForNCCRequest,standTaxInvoiceRequest,token);
            } catch (Exception e) {
                log.error("增值税推送业务系统台账逻辑出错："+ ExceptionUtil.getExceptionMessage(e));
                dataImageFilesInfo.setMessage("增值税推送业务系统台账逻辑出错："+e.getLocalizedMessage());
                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                baseInfo.setCheckResult("出现异常："+e.getLocalizedMessage());
                baseInfo.updateById();
            }
        }
        return dataImageFilesInfo;
    }

    /**
     * 增值税发票信息入台账
     * @param uploadInvoiceForNCCRequest NCC所需参数类
     * @param standTaxInvoiceRequest 保存台账参数类
     * @param token NCC token
     * @return 返回
     * @throws ClassNotFoundException 异常
     * @throws IllegalAccessException 异常
     * @throws InstantiationException 异常
     */
    public DataImageFilesInfo taxInvoiceInfoSaveToStand(UploadInvoiceForNccRequest uploadInvoiceForNCCRequest, StandTaxInvoiceRequest standTaxInvoiceRequest, Token token) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        DataImageFilesInfo dataImageFilesInfo = uploadInvoiceForNCCRequest.getDataImageFilesInfo();
        NccParamProperties paramProperties = uploadInvoiceForNCCRequest.getParamProperties();
        log.info("NCC增值税推送发票信息入台账接口请求报文："+ JSONObject.toJSONString(standTaxInvoiceRequest));
        StandTaxInvoiceResponse standTaxInvoiceResponse;
        try {
            standTaxInvoiceResponse = NccTaxStandService.sendTaxInvoiceInfo(standTaxInvoiceRequest, token, paramProperties);
        } catch (Exception e) {
            log.error("NCC增值税推送发票信息入台账接口出现异常："+ExceptionUtil.getExceptionMessage(e));
            dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
            dataImageFilesInfo.setMessage(e.getLocalizedMessage());
            return dataImageFilesInfo;
        }
        log.info("NCC增值税推送发票信息入台账接口返回报文："+ JSONObject.toJSONString(standTaxInvoiceResponse));
        List<StandTaxInvoiceResponseData> responseDataList = standTaxInvoiceResponse.getData();
        // 发票入台账成功后需要将影像同时绑定到NCC保存接口返回的billId下边
        if(ObjectUtil.isNotEmpty(responseDataList)){
            StandTaxInvoiceResponseData responseData = responseDataList.get(0);
            BaseEntity baseEntity = dataOcrService.ocrQueryByFileId(dataImageFilesInfo.getFileId());
            if(baseEntity instanceof DataMotorVehicleSale){
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                dataMotorVehicleSale.setSaveToken(responseData.getSaveToken());
                dataMotorVehicleSale.setCheckResult("查验成功");
                dataMotorVehicleSale.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataMotorVehicleSale.updateById();
                baseEntity = dataMotorVehicleSale;
            }else{
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                dataOcrInfo.setSaveToken(responseData.getSaveToken());
                dataOcrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataOcrInfo.setCheckResult("查验成功");
                dataOcrInfo.updateById();
                baseEntity = dataOcrInfo;
            }
            String billId = responseData.getBillId();
            this.saveDataForNewBusinessSerialNo(dataImageFilesInfo.getFileType(),uploadInvoiceForNCCRequest.getBillId(),billId,dataImageFilesInfo,baseEntity);
            // 设置图片数据为验真成功
            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
            dataImageFilesInfo.setMessage("查验成功");
            dataImageFilesInfo.insertOrUpdate();
        }
        return dataImageFilesInfo;
    }


    public DataImageFilesInfo allInvoiceInfoSaveToStand(String invoiceType,BaseEntity baseEntity,UploadInvoiceForNccRequest uploadInvoiceForNCCRequest, StandAllInvoiceRequest standAllInvoiceRequest, Token token) throws Exception {
        DataImageFilesInfo dataImageFilesInfo = uploadInvoiceForNCCRequest.getDataImageFilesInfo();
        NccParamProperties paramProperties = uploadInvoiceForNCCRequest.getParamProperties();
        log.info("NCC全票种推送发票信息入台账接口请求报文："+ JSONObject.toJSONString(standAllInvoiceRequest));
        StandAllInvoiceResponse standAllInvoiceResponse;
        standAllInvoiceResponse = NccTaxStandService.sendAllInvoiceInfo(standAllInvoiceRequest, token, paramProperties);
        log.info("NCC全票种推送发票信息入台账接口返回报文："+ JSONObject.toJSONString(standAllInvoiceResponse));
        List<StandAllInvoiceResponseData> responseDataList = standAllInvoiceResponse.getData();
        // 发票入台账成功后需要将影像同时绑定到NCC保存接口返回的billId下边
        if(ObjectUtil.isNotEmpty(responseDataList)){
            StandAllInvoiceResponseData responseData = responseDataList.get(0);
            String saveToken = responseData.getSaveToken();
            switch (invoiceType){
                case InvoiceConstants.TAX_SPECIAL_INVOICE:
                case InvoiceConstants.TAX_INVOICE:
                case InvoiceConstants.ROLL_TICKET:
                case InvoiceConstants.ELECTRONIC_INVOICE:
                case InvoiceConstants.ELECTRONIC_OFD_INVOICE:
                    DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                    dataOcrInfo.setSaveToken(saveToken);
                    dataOcrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataOcrInfo.setQrCode("成功入库");
                    dataOcrInfo.setCheckResult("查验成功");
                    dataOcrInfo.updateById();
                    log.info("发票OCRINFO查验成功后入库id："+dataOcrInfo.getId());
                    baseEntity = dataOcrInfo;
                    // 设置图片数据为验真成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                    dataImageFilesInfo.setMessage("查验成功");
                    break;
                case InvoiceConstants.MOTOR_VEHICLE_SALE:
                    DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                    dataMotorVehicleSale.setSaveToken(saveToken);
                    dataMotorVehicleSale.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataMotorVehicleSale.setCheckResult("查验成功");
                    dataMotorVehicleSale.updateById();
                    baseEntity = dataMotorVehicleSale;
                    // 设置图片数据为验真成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                    dataImageFilesInfo.setMessage("查验成功");
                    break;
                //  定额发票
                case InvoiceConstants.QUOTA_INVOICE:
                    DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
                    dataQuotaInvoice.setSaveToken(saveToken);
                    dataQuotaInvoice.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataQuotaInvoice.setCheckResult("上传成功");
                    dataQuotaInvoice.updateById();
                    baseEntity = dataQuotaInvoice;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
                //  机打发票
                case InvoiceConstants.AIRCRAFT_INVOICE:
                    DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
                    dataAircraftInvoice.setSaveToken(saveToken);
                    dataAircraftInvoice.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataAircraftInvoice.setCheckResult("上传成功");
                    dataAircraftInvoice.updateById();
                    baseEntity = dataAircraftInvoice;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
                //  出租车发票
                case InvoiceConstants.TAXI_TICKETS:
                    DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
                    dataTaxiTickets.setSaveToken(saveToken);
                    dataTaxiTickets.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataTaxiTickets.setCheckResult("上传成功");
                    dataTaxiTickets.updateById();
                    baseEntity = dataTaxiTickets;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
                //  火车票
                case InvoiceConstants.RAILWAY_TICKET:
                    DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
                    dataRailwayTicket.setSaveToken(saveToken);
                    dataRailwayTicket.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataRailwayTicket.setCheckResult("上传成功");
                    dataRailwayTicket.updateById();
                    baseEntity = dataRailwayTicket;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
                //  客运汽车票
                case InvoiceConstants.PASSENGER_TICKET:
                    DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
                    dataPassengerTicket.setSaveToken(saveToken);
                    dataPassengerTicket.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataPassengerTicket.setCheckResult("上传成功");
                    dataPassengerTicket.updateById();
                    baseEntity = dataPassengerTicket;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
                //  航空运输电子客票行程单
                case InvoiceConstants.FLIGHT_ITINERARY:
                    DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
                    dataFlightItinerary.setSaveToken(saveToken);
                    dataFlightItinerary.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataFlightItinerary.setCheckResult("上传成功");
                    dataFlightItinerary.updateById();
                    baseEntity = dataFlightItinerary;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
                //  过路费
                case InvoiceConstants.TOLL_ROADS:
                    DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
                    dataTollRoads.setSaveToken(saveToken);
                    dataTollRoads.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    dataTollRoads.setCheckResult("上传成功");
                    dataTollRoads.updateById();
                    baseEntity = dataTollRoads;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
                //  其他发票
                case InvoiceConstants.INVOICE_OTHERS:
                    DataOcrInfo ocrInfo = (DataOcrInfo) baseEntity;
                    ocrInfo.setSaveToken(saveToken);
                    ocrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                    ocrInfo.setCheckResult("上传成功");
                    ocrInfo.updateById();
                    baseEntity = ocrInfo;
                    // 设置图片数据为上传成功
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    dataImageFilesInfo.setMessage("上传成功");
                    break;
            }
            String billId = responseData.getBillId();
            this.saveDataForNewBusinessSerialNo(invoiceType,uploadInvoiceForNCCRequest.getBillId(),billId,dataImageFilesInfo,baseEntity);
            dataImageFilesInfo.insertOrUpdate();
        }
        return dataImageFilesInfo;
    }

    /**
     * 发票联查保存图片方法
     * @param businessSerialNo 发票联查流水号
     * @param srcBusinessSerialNo 原单据流水号
     * @param dataImageFilesInfo 图片对象
     */
    private void saveDataForNewBusinessSerialNo(String invoiceType,String srcBusinessSerialNo,String businessSerialNo,DataImageFilesInfo dataImageFilesInfo,BaseEntity baseEntity) {
        DataImageFilesInfo invoiceImageFilesInfo = new DataImageFilesInfo();
        BeanUtil.copyProperties(dataImageFilesInfo,invoiceImageFilesInfo);
        invoiceImageFilesInfo.setMessage("复制数据，仅供发票联查");
        invoiceImageFilesInfo.setFileId(IdUtil.simpleUUID());
        switch (invoiceType){
            case InvoiceConstants.TAX_SPECIAL_INVOICE:
            case InvoiceConstants.TAX_INVOICE:
            case InvoiceConstants.ROLL_TICKET:
            case InvoiceConstants.ELECTRONIC_INVOICE:
            case InvoiceConstants.ELECTRONIC_OFD_INVOICE:
            case InvoiceConstants.INVOICE_OTHERS:
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                DataOcrInfo invoiceOcrInfo = new DataOcrInfo();
                BeanUtil.copyProperties(dataOcrInfo,invoiceOcrInfo);
                invoiceOcrInfo.setId(IdUtil.simpleUUID());
                invoiceOcrInfo.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceOcrInfo.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                break;
            case InvoiceConstants.MOTOR_VEHICLE_SALE:
                DataMotorVehicleSale invoiceMotorVehicleSale = new DataMotorVehicleSale();
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                BeanUtil.copyProperties(dataMotorVehicleSale,invoiceMotorVehicleSale);
                invoiceMotorVehicleSale.setId(IdUtil.simpleUUID());
                invoiceMotorVehicleSale.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceMotorVehicleSale.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                break;
            //  定额发票
            case InvoiceConstants.QUOTA_INVOICE:
                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
                DataQuotaInvoice invoiceQuotaInvoice = new DataQuotaInvoice();
                BeanUtil.copyProperties(dataQuotaInvoice,invoiceQuotaInvoice);
                invoiceQuotaInvoice.setId(IdUtil.simpleUUID());
                invoiceQuotaInvoice.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceQuotaInvoice.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                break;
            //  机打发票
            case InvoiceConstants.AIRCRAFT_INVOICE:
                DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
                DataAircraftInvoice invoiceAircraftInvoice = new DataAircraftInvoice();
                BeanUtil.copyProperties(dataAircraftInvoice,invoiceAircraftInvoice);
                invoiceAircraftInvoice.setId(IdUtil.simpleUUID());
                invoiceAircraftInvoice.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceAircraftInvoice.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                break;
            //  出租车发票
            case InvoiceConstants.TAXI_TICKETS:
                DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
                DataTaxiTickets invoiceTaxiTickets = new DataTaxiTickets();
                BeanUtil.copyProperties(dataTaxiTickets,invoiceTaxiTickets);
                invoiceTaxiTickets.setId(IdUtil.simpleUUID());
                invoiceTaxiTickets.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceTaxiTickets.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                break;
            //  火车票
            case InvoiceConstants.RAILWAY_TICKET:
                DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
                DataRailwayTicket invoiceRailwayTicket = new DataRailwayTicket();
                BeanUtil.copyProperties(dataRailwayTicket,invoiceRailwayTicket);
                invoiceRailwayTicket.setId(IdUtil.simpleUUID());
                invoiceRailwayTicket.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceRailwayTicket.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                break;
            //  客运汽车票
            case InvoiceConstants.PASSENGER_TICKET:
                DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
                DataPassengerTicket invoicePassengerTicket = new DataPassengerTicket();
                BeanUtil.copyProperties(dataPassengerTicket,invoicePassengerTicket);
                invoicePassengerTicket.setId(IdUtil.simpleUUID());
                invoicePassengerTicket.setFileId(invoiceImageFilesInfo.getFileId());
                invoicePassengerTicket.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                break;
            //  航空运输电子客票行程单
            case InvoiceConstants.FLIGHT_ITINERARY:
                DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
                DataFlightItinerary invoiceFlightItinerary = new DataFlightItinerary();
                BeanUtil.copyProperties(dataFlightItinerary,invoiceFlightItinerary);
                invoiceFlightItinerary.setId(IdUtil.simpleUUID());
                invoiceFlightItinerary.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceFlightItinerary.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                break;
            //  过路费
            case InvoiceConstants.TOLL_ROADS:
                DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
                DataTollRoads invoiceTollRoads = new DataTollRoads();
                BeanUtil.copyProperties(dataTollRoads,invoiceTollRoads);
                invoiceTollRoads.setId(IdUtil.simpleUUID());
                invoiceTollRoads.setFileId(invoiceImageFilesInfo.getFileId());
                invoiceTollRoads.insert();
                invoiceImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                break;
        }
        // 生成中间表
        DataCmInfo cmInfo = new DataCmInfo();
        cmInfo.setBatchId(BatchIdUtils.getNewBatchId());
        cmInfo.setBusinessSerialNo(businessSerialNo);
        cmInfo.setId(IdUtil.simpleUUID());
        cmInfo.insert();
        invoiceImageFilesInfo.setBatchId(cmInfo.getBatchId());
        invoiceImageFilesInfo.insert();
        // 生成任务表
        DataCurrentTask dataCurrentTask = currentTaskService.selectDataCurrentTaskByBusinessSerialNo(srcBusinessSerialNo);
        if(ObjectUtil.isNotEmpty(dataCurrentTask)){
            DataCurrentTask invoiceCurrentTask = new DataCurrentTask();
            BeanUtil.copyProperties(dataCurrentTask,invoiceCurrentTask);
            invoiceCurrentTask.setBusinessSerialNo(businessSerialNo);
            invoiceCurrentTask.setBillNum("复制数据，仅供查看");
            invoiceCurrentTask.setRemark("复制数据，仅供查看");
            invoiceCurrentTask.insert();
        }
        saveOrUpdateImageTree(invoiceImageFilesInfo);
    }

    /**
     * 生成收票树节点
     */
    private DataImageTree saveOrUpdateImageTree(DataImageFilesInfo dataImageFilesInfo) {
        DataImageTree dataImageTree = new DataImageTree();
        dataImageTree.setProductId(dataImageFilesInfo.getFileId());
        dataImageTree.setParentId(InvoiceConstants.INVOICE_PREVIEW);
        //附件类型加判断
        dataImageTree.setProductName(dataImageFilesInfo.getFileName());
        dataImageTree.setProductLevel(2L);
        dataImageTree.setProductType("0");
        dataImageTree.setStatus("0");
        dataImageTree.setOrderNum(0L);
        dataImageTree.setImageId(dataImageFilesInfo.getFileId());
        dataImageTree.setBatchId(dataImageFilesInfo.getBatchId());
        imageTreeService.saveOrUpdate(dataImageTree);
        return dataImageTree;
    }

    /**
     * 获取请求体Data
     * @param invoiceType
     * @param uploadBusinessType
     * @param baseEntity
     * @param dataImageFilesInfo
     * @return
     */
    public JSONObject getRequestData(String invoiceType,String uploadBusinessType,BaseEntity baseEntity,DataImageFilesInfo dataImageFilesInfo){
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        String url;
        if(ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, FileUtils.getFileSuffix(dataImageFilesInfo.getFileName()))){
            url = FileUtils.getFileUrlResource(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getIurl(), dataImageFilesInfo.getFileId());
        }else{
            // 文件格式的取url源文件字段
            url = FileUtils.getFileUrlResource(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_IP) + ":" + RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CONFIG_PORT) + dataImageFilesInfo.getUrl(), dataImageFilesInfo.getFileId());
        }
        switch (invoiceType){
            //  增值税票种组装台账保存JSON参数
            case InvoiceConstants.TAX_SPECIAL_INVOICE:
            case InvoiceConstants.TAX_INVOICE:
            case InvoiceConstants.ROLL_TICKET:
            case InvoiceConstants.ELECTRONIC_INVOICE:
            case InvoiceConstants.ELECTRONIC_OFD_INVOICE:
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                jsonObject.put("saveToken",dataOcrInfo.getSaveToken());
                jsonObject.put("imageId",dataOcrInfo.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType", NcOcrInvoiceTypeConstant.INVOICE);
                data.put("fpDm",dataOcrInfo.getInvoiceCode());
                data.put("fpHm",dataOcrInfo.getInvoiceNumber());
                data.put("kprq",DateUtil.format(dataOcrInfo.getInvoiceDate(),"yyyyMMdd"));
                data.put("hjje",dataOcrInfo.getSumAmount());
                data.put("jshj",dataOcrInfo.getTotalLowercase());
                if(StrUtil.equals(InvoiceConstants.TAX_SPECIAL_INVOICE,invoiceType)){
                    data.put("fplx",NcCheckInvoiceTypeConstant.NCC_VAT_SPECIAL_INVOICE);
                }else if(StrUtil.equals(InvoiceConstants.ROLL_TICKET,invoiceType)){
                    data.put("fplx",NcCheckInvoiceTypeConstant.NCC_VAT_INVOICE_ROLL);
                    data.put("jym",dataOcrInfo.getCheckCode());
                }else{
                    data.put("fplx",NcCheckInvoiceTypeConstant.NCC_VAT_INVOICE);
                    data.put("jym",dataOcrInfo.getCheckCode());
                }
                jsonObject.put("data",data);
                break;
            case InvoiceConstants.MOTOR_VEHICLE_SALE:
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                jsonObject.put("saveToken",dataMotorVehicleSale.getSaveToken());
                jsonObject.put("imageId",dataMotorVehicleSale.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.INVOICE);
                data.put("fpDm",dataMotorVehicleSale.getInvoiceCode());
                data.put("fpHm",dataMotorVehicleSale.getInvoiceNumber());
                data.put("kprq",DateUtil.format(dataMotorVehicleSale.getInvoiceDate(),"yyyyMMdd"));
                data.put("hjje",dataMotorVehicleSale.getPreTaxAmount());
                data.put("jshj",dataMotorVehicleSale.getInvoiceTotal());
                data.put("fplx",NcCheckInvoiceTypeConstant.NCC_MOTOR_VEHICLE_SALES);
                jsonObject.put("data",data);
                break;
            //  定额发票
            case InvoiceConstants.QUOTA_INVOICE:
                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
                jsonObject.put("saveToken",dataQuotaInvoice.getSaveToken());
                jsonObject.put("imageId",dataQuotaInvoice.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.QUOTA);
                data.put("totalAmount",dataQuotaInvoice.getInvoiceTotal());
                data.put("thirdVerifyStatus",0.0);
                data.put("kind","");
                data.put("yyVerifyStatus",0.0);
                data.put("invoiceCode",dataQuotaInvoice.getInvoiceCode());
                data.put("invoiceNum",dataQuotaInvoice.getInvoiceNumber());
                jsonObject.put("data",data);
                break;
            //  机打发票
            case InvoiceConstants.AIRCRAFT_INVOICE:
                DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
                List<JSONObject> itemList = new ArrayList<>();
                DataOcrDetailsBo dataOcrDetailsBo = new DataOcrDetailsBo();
                dataOcrDetailsBo.setFileId(dataAircraftInvoice.getFileId());
                List<DataOcrDetailsVo> dataOcrDetailsVoList = dataOcrDetailsService.queryList(dataOcrDetailsBo);
                if(CollUtil.isNotEmpty(dataOcrDetailsVoList)){
                    dataOcrDetailsVoList.forEach(obj->{
                        JSONObject item = new JSONObject();
                        item.put("taxRate",obj.getTax());
                        item.put("taxAmount",0.0);
                        item.put("amount",obj.getDetailAmount());
                        item.put("specs","");
                        item.put("item",obj.getName());
                        item.put("unit","");
                        item.put("num",obj.getDetailsCount());
                        item.put("price",obj.getPrice());
                        itemList.add(item);
                    });
                }
                jsonObject.put("saveToken",dataAircraftInvoice.getSaveToken());
                jsonObject.put("imageId",dataAircraftInvoice.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.MACHINE);
                data.put("date",DateUtil.format(dataAircraftInvoice.getInvoiceDate(),"yyyyMMdd"));
                data.put("thirdVerifyStatus",0.0);
                data.put("city",dataAircraftInvoice.getCity());
                data.put("sellerName",dataAircraftInvoice.getSellerName());
                data.put("title","");
                data.put("buyerTaxId",dataAircraftInvoice.getBuyerTaxid());
                data.put("province",dataAircraftInvoice.getProvince());
                data.put("sellerTaxId",dataAircraftInvoice.getSellerTaxid());
                data.put("producerStamp","");
                data.put("companySeal",dataAircraftInvoice.getCompanySeal());
                data.put("buyerName",dataAircraftInvoice.getBuyerName());
                data.put("invoiceCode",dataAircraftInvoice.getInvoiceCode());
                data.put("invoiceNum",dataAircraftInvoice.getInvoiceNumber());
                data.put("checkCode",dataAircraftInvoice.getCheckCode());
                data.put("totalAmount",dataAircraftInvoice.getInvoiceTotal());
                data.put("time","");
                data.put("itemList",itemList);
                data.put("kind","");
                data.put("yyVerifyStatus",0.0);
                jsonObject.put("data",data);
                break;
            //  出租车发票
            case InvoiceConstants.TAXI_TICKETS:
                DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
                jsonObject.put("saveToken",dataTaxiTickets.getSaveToken());
                jsonObject.put("imageId",dataTaxiTickets.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.TAXI);
                data.put("date",DateUtil.format(dataTaxiTickets.getInvoiceDate(),"yyyyMMdd"));
                data.put("thirdVerifyStatus",0.0);
                data.put("city",dataTaxiTickets.getCity());
                data.put("amount",dataTaxiTickets.getInvoiceTotal());
                data.put("carNum",dataTaxiTickets.getLicensePlate());
                data.put("province",dataTaxiTickets.getProvince());
                data.put("totalAmount",dataTaxiTickets.getInvoiceTotal());
                data.put("startTime",dataTaxiTickets.getTimeGetOn());
                data.put("endTime",dataTaxiTickets.getTimeGetOff());
                data.put("place",dataTaxiTickets.getPlace());
                data.put("invoiceCode",dataTaxiTickets.getInvoiceCode());
                data.put("invoiceNum",dataTaxiTickets.getInvoiceNumber());
                data.put("fuelSurcharge",0.0);
                data.put("mileage",dataTaxiTickets.getMileage());
                data.put("kind","");
                data.put("yyVerifyStatus",0.0);
                jsonObject.put("data",data);
                break;
            //  火车票
            case InvoiceConstants.RAILWAY_TICKET:
                DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
                jsonObject.put("saveToken",dataRailwayTicket.getSaveToken());
                jsonObject.put("imageId",dataRailwayTicket.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.TRAIN);
                data.put("date",DateUtil.format(dataRailwayTicket.getInvoiceDate(),"yyyyMMdd"));
                data.put("thirdVerifyStatus",0.0);
                data.put("level",dataRailwayTicket.getSeat());
                data.put("kind","");
                data.put("origin",dataRailwayTicket.getStationGetOn());
                data.put("yyVerifyStatus",0.0);
                data.put("destination",dataRailwayTicket.getStationGetOff());
                data.put("idNumber",dataRailwayTicket.getIdNumber());
                data.put("invoiceNum",dataRailwayTicket.getInvoiceNumber());
                data.put("number",dataRailwayTicket.getInvoiceNumber());
                data.put("totalAmount",dataRailwayTicket.getInvoiceTotal());
                data.put("trainNum",dataRailwayTicket.getTrainNumber());
                data.put("name",dataRailwayTicket.getName());
                data.put("ticketNum",dataRailwayTicket.getTrainNumber());
                data.put("time",dataRailwayTicket.getInvoiceTime());
                jsonObject.put("data",data);
                break;
            //  客运汽车票
            case InvoiceConstants.PASSENGER_TICKET:
                DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
                jsonObject.put("saveToken",dataPassengerTicket.getSaveToken());
                jsonObject.put("imageId",dataPassengerTicket.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.PASSENGER);
                data.put("date",DateUtil.format(dataPassengerTicket.getInvoiceDate(),"yyyyMMdd"));
                data.put("thirdVerifyStatus",0.0);
                data.put("kind","");
                data.put("yyVerifyStatus",0.0);
                data.put("invoiceNum",dataPassengerTicket.getInvoiceNumber());
                data.put("invoiceCode",dataPassengerTicket.getInvoiceCode());
                data.put("totalAmount",dataPassengerTicket.getInvoiceTotal());
                data.put("exit",dataPassengerTicket.getStationGetOn());
                data.put("name",dataPassengerTicket.getName());
                data.put("entrance",dataPassengerTicket.getStationGetOff());
                data.put("time",dataPassengerTicket.getInvoiceTime());
                jsonObject.put("data",data);
                break;
            //  航空运输电子客票行程单
            case InvoiceConstants.FLIGHT_ITINERARY:
                DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
                DataFlightsBo dataFlightsBo = new DataFlightsBo();
                dataFlightsBo.setFileId(dataFlightItinerary.getFileId());
                List<DataFlightsVo> dataFlightsVoList = dataFlightsService.queryList(dataFlightsBo);
                jsonObject.put("saveToken",dataFlightItinerary.getSaveToken());
                jsonObject.put("imageId", dataFlightItinerary.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.AIR);
                data.put("date",DateUtil.format(dataFlightItinerary.getInvoiceDate(),"yyyyMMdd"));
                data.put("insurance",dataFlightItinerary.getInsurance());
                data.put("fare",Convert.toBigDecimal(dataFlightItinerary.getFare()));
                data.put("thirdVerifyStatus",0.0);
                data.put("agentCode",dataFlightItinerary.getAgentCode());
                data.put("caacDevelopFund",dataFlightItinerary.getCaacDevelopmentFund());
                data.put("ticketNum",dataFlightItinerary.getInvoiceNumber());
                data.put("issueBy",dataFlightItinerary.getIssueBy());
                data.put("kind","");
                data.put("yyVerifyStatus",0.0);
                data.put("userName",dataFlightsVoList.get(0).getUserName());
                data.put("invoiceNum",dataFlightItinerary.getInvoiceNumber());
                data.put("userId",dataFlightsVoList.get(0).getUserId());
                data.put("checkCode",dataFlightItinerary.getCheckCode());
                data.put("airportType",dataFlightItinerary.getInternationalFlag());
                data.put("totalAmount",dataFlightItinerary.getInvoiceTotal());
                data.put("fuelSurcharge",dataFlightItinerary.getFuelSurcharge());
                List<JSONObject> details = new ArrayList();
                for (int i = 0; i < dataFlightsVoList.size(); i++) {
                    DataFlightsVo flights = dataFlightsVoList.get(i);
                    JSONObject detail = new JSONObject();
                    detail.put("date",DateUtil.format(flights.getInvoiceDate(),"yyyyMMdd"));
                    detail.put("seat",flights.getSeat());
                    detail.put("carrier",flights.getCarrier());
                    detail.put("from",flights.getStationGetOn());
                    detail.put("time",flights.getInvoiceTime());
                    detail.put("to",flights.getStationGetOff());
                    detail.put("flightNumber",flights.getFlightNumber());
                    details.add(detail);
                }
                data.put("itemList",details);
                jsonObject.put("data",data);
                break;
            //  过路费
            case InvoiceConstants.TOLL_ROADS:
                DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
                jsonObject.put("saveToken",dataTollRoads.getSaveToken());
                jsonObject.put("imageId", dataTollRoads.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                jsonObject.put("billType",NcOcrInvoiceTypeConstant.TOLLS);
                data.put("thirdVerifyStatus",0.0);
                data.put("kind","");
                data.put("yyVerifyStatus",0.0);
                data.put("title","");
                data.put("invoiceNum",dataTollRoads.getInvoiceNumber());
                data.put("totalAmount",dataTollRoads.getInvoiceTotal());
                data.put("invoiceCode",dataTollRoads.getInvoiceCode());
                data.put("exit",dataTollRoads.getTollExit());
                data.put("entrance",dataTollRoads.getEntrance());
                data.put("time",dataTollRoads.getInvoiceTime());
                data.put("date",DateUtil.format(dataTollRoads.getInvoiceDate(),"yyyyMMdd"));
                jsonObject.put("data",data);
                break;
            //  其他发票
            case InvoiceConstants.INVOICE_OTHERS:
                DataOcrInfo ocrInfo = (DataOcrInfo) baseEntity;
                jsonObject.put("saveToken",ocrInfo.getSaveToken());
                jsonObject.put("billType", NcOcrInvoiceTypeConstant.OTHER_INVOICE);
                jsonObject.put("imageId",ocrInfo.getNcImageId());
                // 对应文件路径地址
                jsonObject.put("imageFileDownLoadURL",url);
                // 对应文件名称
                jsonObject.put("imageFileName",dataImageFilesInfo.getFileName());
                data.put("thirdVerifyStatus",0.0);
                data.put("kind","");
                data.put("yyVerifyStatus",0.0);
                data.put("invoiceCode",ocrInfo.getInvoiceCode());
                data.put("invoiceNum",ocrInfo.getInvoiceNumber());
                data.put("totalAmount", Convert.toBigDecimal(ocrInfo.getSumAmount()));
                jsonObject.put("data",data);
                break;
        }
        return jsonObject;
    }

}
