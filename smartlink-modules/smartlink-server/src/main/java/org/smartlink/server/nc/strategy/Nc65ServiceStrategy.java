package org.smartlink.server.nc.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.*;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.SysUser;
import org.smartlink.server.nc.domain.dto.NcDeleteServiceDTO;
import org.smartlink.server.nc.domain.dto.NcImageServiceDTO;
import org.smartlink.server.nc.domain.dto.TaskSubmitDTO;
import org.smartlink.server.nc.domain.dto.UpdateTaskDTO;
import org.smartlink.server.nc.domain.invoice.*;
import org.smartlink.server.nc.domain.invoice.bo.DataFlightsBo;
import org.smartlink.server.nc.domain.invoice.vo.DataFlightsVo;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.ocr.properties.OcrProperties;
import org.smartlink.server.nc.ocr.service.yesfp.YesfpDeleteService;
import org.smartlink.server.nc.ocr.service.yesfp.YesfpOcrSaveService;
import org.smartlink.server.nc.ocr.service.yesfp.config.YesfpProperties;
import org.smartlink.server.nc.ocr.service.yesfp.reponse.Invoices;
import org.smartlink.server.nc.ocr.service.yesfp.reponse.YesfpDeleteResult;
import org.smartlink.server.nc.ocr.service.yesfp.request.*;
import org.smartlink.server.nc.properties.Nc65ParamProperties;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.smartlink.server.nc.utils.file.FileUtils;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: NC65端业务实现类
 * @author: L
 * @create:
 **/
@Component
@Slf4j
public class Nc65ServiceStrategy extends AbstractNCStrategy{

    private final NcService ncService;

    private Nc65ParamProperties yesfpProperties = new Nc65ParamProperties();
    private final IDataCurrentTaskService dataCurrentTaskService;
    private final IDataOcrService dataOcrService;

    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataFlightsService dataFlightsService;
    private final ISysUserService sysUserService;

    public Nc65ServiceStrategy(IDataCurrentTaskService dataCurrentTaskService,NcService ncService,IDataOcrService iDataOcrService,IDataImageFilesInfoService dataImageFilesInfoService,IDataFlightsService dataFlightsService,ISysUserService sysUserService) {
        this.dataCurrentTaskService=dataCurrentTaskService;
        this.ncService = ncService;
        this.dataOcrService=iDataOcrService;
        this.dataImageFilesInfoService = dataImageFilesInfoService;
        this.dataFlightsService = dataFlightsService;
        this.sysUserService = sysUserService;
    }
    @Override
    public void deleteNcInvoiceDataBusinessService(NcDeleteServiceDTO ncDeleteServiceDTO) throws Exception {
        boolean saveStandOff = Boolean.parseBoolean(yesfpProperties.getSaveStandOff());
        // 保存台账开关是否开启
        if(saveStandOff){
            String type = RedisUtils.getCacheObject(OcrConstant.CACHE_CONFIG_KEY);
            Object json = RedisUtils.getCacheObject(OcrConstant.SYS_OCR_KEY + type);
            OcrProperties properties = JsonUtils.parseObject(json.toString(), OcrProperties.class);
            String detailInfo = properties.getDetailInfo();
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(detailInfo);
            // JSON转对象
            YesfpProperties yesfpProperties = JSONUtil.toBean(jsonObject, YesfpProperties.class);
            List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectDataImageFilesInfoListByFileIdList(ncDeleteServiceDTO.getFileIdList());
            if(CollectionUtil.isNotEmpty(dataImageFilesInfoList)){
                List<BaseEntity> baseEntityList = new ArrayList<>();
                for (DataImageFilesInfo imageFilesInfo : dataImageFilesInfoList) {
                    // 对多票据文件判断处理
                    if(StrUtil.equals(imageFilesInfo.getFileType(), InvoiceConstants.INVOICE_MUCH_NCC)){
                        List<String> fileTypeList = StrUtil.split(imageFilesInfo.getIncludeTypeArr(), Constants.CONNECT_COMMA_SYMBOL);
                        if(CollectionUtil.isNotEmpty(fileTypeList) && fileTypeList.size()>0){
                            for (String fileType : fileTypeList) {
                                List<BaseEntity> entityList = dataOcrService.queryInvoiceInfoByTypeAndFileId(fileType, imageFilesInfo.getFileId());
                                if(CollectionUtil.isNotEmpty(entityList) && entityList.size()>0){
                                    baseEntityList.addAll(entityList);
                                }
                            }
                        }
                    }else{
                        BaseEntity baseEntity = dataOcrService.ocrQueryByFileId(imageFilesInfo.getFileId());
                        if(ObjectUtil.isNotEmpty(baseEntity)){
                            baseEntityList.add(baseEntity);
                        }
                    }
                }
                if(CollectionUtil.isNotEmpty(baseEntityList) && baseEntityList.size()>0){
                    List<YesfpDeleteBills> yesfpDeleteBillsList = YesfpDeleteService.getDeleteBills(baseEntityList);
                    if(CollectionUtil.isNotEmpty(yesfpDeleteBillsList) && yesfpDeleteBillsList.size()>0){
                        YesfpDeleteData yesfpDeleteData = new YesfpDeleteData();
                        yesfpDeleteData.setNsrsbh(yesfpProperties.getNsrsbh());
                        yesfpDeleteData.setOrgcode(yesfpProperties.getOrgCode());
                        yesfpDeleteData.setBills(yesfpDeleteBillsList);
                        String request = JSONObject.toJSONString(yesfpDeleteData);
                        log.info("税务云删除接口请求参数：" + request);
                        YesfpDeleteResult yesfpDeleteResult = YesfpDeleteService.deleteStandData(yesfpDeleteData,yesfpProperties);
                        log.info("删除税务云返回结果：" + JSONObject.toJSONString(yesfpDeleteResult));
                    }
                }
            }
        }
    }

    @Override
    public DataCurrentTask submitTaskStateToBusinessService(TaskSubmitDTO taskSubmitDTO) throws Exception {
        return ncService.submitTaskStateForNc(taskSubmitDTO.getDataCurrentTask(),taskSubmitDTO.getUserId(),taskSubmitDTO.getSupplementaryScan());
    }

    @Override
    public DataCurrentTask rejectTaskStateBusinessService(UpdateTaskDTO updateTaskDTO) throws Exception {
        return ncService.rejectTaskStateForNc(updateTaskDTO.getDataCurrentTask(),updateTaskDTO.getUserId(),updateTaskDTO.getUpdateState());
    }

    @Override
    public DataImageFilesInfo doBusinessService(NcImageServiceDTO ncImageServiceDTO) {
        DataImageFilesInfo dataImageFilesInfo = ncImageServiceDTO.getDataImageFilesInfo();
        String fileType = dataImageFilesInfo.getFileType();
        String fileSuffix = FileUtils.getFileSuffix(dataImageFilesInfo.getFileName());
        Boolean saveStandOff = Boolean.valueOf(yesfpProperties.getSaveStandOff());
        // 保存台账开关
        if(saveStandOff){
            String type = RedisUtils.getCacheObject(OcrConstant.CACHE_CONFIG_KEY);
            Object json = RedisUtils.getCacheObject(OcrConstant.SYS_OCR_KEY + type);
            OcrProperties properties = JsonUtils.parseObject(json.toString(), OcrProperties.class);
            String detailInfo = properties.getDetailInfo();
            // JSON转对象
            YesfpProperties yesfpProperties = JSON.parseObject(detailInfo,YesfpProperties.class);
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(ncImageServiceDTO.getBusinessSerialNo());
            String nsrsbh = yesfpProperties.getNsrsbh();
            String orgCode = yesfpProperties.getOrgCode();
            if(!ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, fileSuffix) && (StrUtil.equals(fileType,InvoiceConstants.ELECTRONIC_INVOICE) || StrUtil.equals(fileType,InvoiceConstants.ELECTRONIC_OFD_INVOICE))){
                // 电票源文件直接return，因为电票识别接口就已经入税务云台账了
                return dataImageFilesInfo;
            }else if (fileType.equalsIgnoreCase(InvoiceConstants.TAX_SPECIAL_INVOICE)
                || fileType.equalsIgnoreCase(InvoiceConstants.TAX_INVOICE)
                || fileType.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_INVOICE)
                || fileType.equalsIgnoreCase(InvoiceConstants.ROLL_TICKET)
                || fileType.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_OFD_INVOICE)
                || fileType.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY)
                || fileType.equalsIgnoreCase(InvoiceConstants.MOTOR_VEHICLE_SALE)
                || fileType.equalsIgnoreCase(InvoiceConstants.USED_CAR_SALES)
            ) {
                // 增值税发票保存税务云台账
                List<BaseEntity> baseEntityList;
                try {
                    baseEntityList = dataOcrService.queryInvoiceInfoByTypeAndFileId(dataImageFilesInfo.getFileType(),dataImageFilesInfo.getFileId());
                } catch (Exception e) {
                    log.error("查询OCR数据方法出现异常："+ ExceptionUtil.getExceptionMessage(e));
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                    dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                    return dataImageFilesInfo;
                }
                if(CollectionUtil.isNotEmpty(baseEntityList)){
                    BaseEntity baseEntity = baseEntityList.get(0);
                    JSONObject yesfpJsonData = this.getYesfpJsonData(dataImageFilesInfo, baseEntity);
                    if(ObjectUtil.isNotEmpty(yesfpJsonData)){
                        List<SysUser> sysUserList = sysUserService.selectListByNcUserId(dataCurrentTask.getUserId());
                        String nickName = CollUtil.isNotEmpty(sysUserList)?sysUserList.get(0).getNickName():"admin";
                        YesfpTaxInvoiceRequest yesfpTaxInvoiceRequest = new YesfpTaxInvoiceRequest();
                        yesfpTaxInvoiceRequest.setOrgcode(orgCode);
                        yesfpTaxInvoiceRequest.setNsrsbh(nsrsbh);
                        yesfpTaxInvoiceRequest.setSubmitter(nickName);
                        Invoices invoices = JSONObject.toJavaObject(yesfpJsonData, Invoices.class);
                        yesfpTaxInvoiceRequest.setInvoices(ListUtil.of(invoices));
                        try {
                            YesfpOcrSaveService.taxInvoiceToStand(yesfpTaxInvoiceRequest,yesfpProperties);
                        } catch (Exception e) {
                            log.error("税务云增值税发票保存台账出现异常："+ExceptionUtil.getExceptionMessage(e));
                            dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                            dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                            dataImageFilesInfo.updateById();
                            return dataImageFilesInfo;
                        }
                        // 更新推送台账成功的状态值
                        dataImageFilesInfo = this.updateYesfpBusinessInfoFlag(dataImageFilesInfo,baseEntity);
                        dataImageFilesInfo.updateById();
                    }
                }
            }else{
                // 非增值税发票保存税务云台账
                List<BaseEntity> baseEntityList;
                try {
                    baseEntityList = dataOcrService.queryInvoiceInfoByTypeAndFileId(dataImageFilesInfo.getFileType(),dataImageFilesInfo.getFileId());
                } catch (Exception e) {
                    log.error("查询OCR数据方法出现异常："+ ExceptionUtil.getExceptionMessage(e));
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                    dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                    return dataImageFilesInfo;
                }
                if(CollectionUtil.isNotEmpty(baseEntityList)){
                    BaseEntity baseEntity = baseEntityList.get(0);
                    JSONObject yesfpJsonData = this.getYesfpJsonData(dataImageFilesInfo, baseEntity);
                    if(ObjectUtil.isNotEmpty(yesfpJsonData)){
                        YesfpNonTaxInvoiceRequest yesfpNonTaxInvoiceRequest = new YesfpNonTaxInvoiceRequest();
                        yesfpNonTaxInvoiceRequest.setOrgcode(orgCode);
                        yesfpNonTaxInvoiceRequest.setNsrsbh(nsrsbh);
                        yesfpNonTaxInvoiceRequest.setSrcBillCode(ObjectUtil.isNotEmpty(dataCurrentTask)?dataCurrentTask.getBillNum():"");
                        yesfpNonTaxInvoiceRequest.setSrcBillType("影像");
                        log.info("---文件id---："+dataImageFilesInfo.getFileId());
                        log.info("非增值税数据组装获取billType："+yesfpJsonData.getString("billType"));
                        log.info("非增值税数据组装获取imageId："+yesfpJsonData.getString("imageId"));
                        Bills bills = Bills.builder().billType(yesfpJsonData.getString("billType")).imageId(yesfpJsonData.getString("imageId")).data(yesfpJsonData).build();
                        yesfpNonTaxInvoiceRequest.setBills(ListUtil.of(bills));
                        try {
                            YesfpOcrSaveService.nonTaxInvoiceToStand(yesfpNonTaxInvoiceRequest,yesfpProperties);
                        } catch (Exception e) {
                            log.error("税务云非增值税发票保存台账出现异常："+ ExceptionUtil.getExceptionMessage(e));
                            dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                            dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                            dataImageFilesInfo.updateById();
                            return dataImageFilesInfo;
                        }
                        // 更新推送台账成功的状态值
                        dataImageFilesInfo = this.updateYesfpBusinessInfoFlag(dataImageFilesInfo,baseEntity);
                        dataImageFilesInfo.updateById();
                    }
                }
            }
        }
        return dataImageFilesInfo;
    }

    private DataImageFilesInfo updateYesfpBusinessInfoFlag(DataImageFilesInfo dataImageFilesInfo,BaseEntity baseEntity){
        switch (dataImageFilesInfo.getFileType()){
            case InvoiceConstants.TAX_SPECIAL_INVOICE:
            case InvoiceConstants.TAX_INVOICE:
            case InvoiceConstants.ROLL_TICKET:
            case InvoiceConstants.ELECTRONIC_INVOICE:
            case InvoiceConstants.ELECTRONIC_OFD_INVOICE:
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                dataOcrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataOcrInfo.updateById();
                log.info("发票OCRINFO查验成功后入库id："+dataOcrInfo.getId());
                // 设置图片数据为验真成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                dataImageFilesInfo.setMessage("查验成功");
                break;
            case InvoiceConstants.MOTOR_VEHICLE_SALE:
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                dataMotorVehicleSale.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataMotorVehicleSale.updateById();
                // 设置图片数据为验真成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                dataImageFilesInfo.setMessage("查验成功");
                break;
            //  定额发票
            case InvoiceConstants.QUOTA_INVOICE:
                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
                dataQuotaInvoice.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataQuotaInvoice.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
            //  机打发票
            case InvoiceConstants.AIRCRAFT_INVOICE:
                DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
                dataAircraftInvoice.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataAircraftInvoice.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
            //  出租车发票
            case InvoiceConstants.TAXI_TICKETS:
                DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
                dataTaxiTickets.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataTaxiTickets.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
            //  火车票
            case InvoiceConstants.RAILWAY_TICKET:
                DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
                dataRailwayTicket.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataRailwayTicket.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
            //  客运汽车票
            case InvoiceConstants.PASSENGER_TICKET:
                DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
                dataPassengerTicket.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataPassengerTicket.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
            //  航空运输电子客票行程单
            case InvoiceConstants.FLIGHT_ITINERARY:
                DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
                dataFlightItinerary.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataFlightItinerary.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
            //  过路费
            case InvoiceConstants.TOLL_ROADS:
                DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
                dataTollRoads.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                dataTollRoads.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
            //  其他发票
            case InvoiceConstants.INVOICE_OTHERS:
                DataOcrInfo ocrInfo = (DataOcrInfo) baseEntity;
                ocrInfo.setPushBusinessInfoFlag(NcConstant.PUSH_BUSINESS_INFO_SUCCESS);
                ocrInfo.updateById();
                // 设置图片数据为上传成功
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                break;
        }
        return dataImageFilesInfo;
    }
    /**
     * 组装保存台账所需JSON结构
     * @param dataImageFilesInfo
     * @param baseEntity
     * @return
     */
    private JSONObject getYesfpJsonData(DataImageFilesInfo dataImageFilesInfo,BaseEntity baseEntity){
        String fileType = dataImageFilesInfo.getFileType();
        switch (fileType){
            //  增值税票种组装台账保存JSON参数
            case InvoiceConstants.TAX_SPECIAL_INVOICE:
            case InvoiceConstants.TAX_INVOICE:
            case InvoiceConstants.ROLL_TICKET:
            case InvoiceConstants.ELECTRONIC_INVOICE:
            case InvoiceConstants.ELECTRONIC_OFD_INVOICE:
                DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
                JSONObject ocrInfo = new JSONObject();
                if(StrUtil.equals(dataOcrInfo.getCheckInvoice(),CheckConstant.SUCCESS_CHECK)){
                    DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(dataImageFilesInfo.getBusinessSerialNo());
                    ocrInfo.put("fpDm",StrUtil.isNotEmpty(dataOcrInfo.getInvoiceCode())?dataOcrInfo.getInvoiceCode():"");
                    ocrInfo.put("fpHm",dataOcrInfo.getInvoiceNumber());
                    ocrInfo.put("kprq", DateUtil.format(dataOcrInfo.getInvoiceDate(),"yyyyMMdd"));
                    ocrInfo.put("hjje",dataOcrInfo.getSumAmount());
                    ocrInfo.put("jym",dataOcrInfo.getCheckCode());
                    ocrInfo.put("saveToken",dataOcrInfo.getSaveToken());
                    ocrInfo.put("srcBillType","");
                    ocrInfo.put("srcBillCode",ObjectUtil.isNotEmpty(dataCurrentTask)?dataCurrentTask.getBillNum():"");
                    ocrInfo.put("imageId",dataImageFilesInfo.getNcImageId());
                    return ocrInfo;
                }else {
                    return null;
                }
                // 机动车发票
            case InvoiceConstants.MOTOR_VEHICLE_SALE:
                DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) baseEntity;
                if(StrUtil.equals(dataMotorVehicleSale.getCheckInvoice(),CheckConstant.SUCCESS_CHECK)){
                    DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(dataImageFilesInfo.getBusinessSerialNo());
                    JSONObject motorVehicleSale = new JSONObject();
                    motorVehicleSale.put("fpDm",dataMotorVehicleSale.getInvoiceCode());
                    motorVehicleSale.put("fpHm",dataMotorVehicleSale.getInvoiceNumber());
                    motorVehicleSale.put("kprq",DateUtil.format(dataMotorVehicleSale.getInvoiceDate(),"yyyyMMdd"));
                    motorVehicleSale.put("hjje",dataMotorVehicleSale.getPreTaxAmount());
                    motorVehicleSale.put("jym","");
                    motorVehicleSale.put("saveToken",dataMotorVehicleSale.getSaveToken());
                    motorVehicleSale.put("srcBillType","");
                    motorVehicleSale.put("srcBillCode",ObjectUtil.isNotEmpty(dataCurrentTask)?dataCurrentTask.getBillNum():"");
                    motorVehicleSale.put("imageId",dataImageFilesInfo.getNcImageId());
                    return motorVehicleSale;
                }else{
                    return null;
                }
                //  定额发票
            case InvoiceConstants.QUOTA_INVOICE:
                DataQuotaInvoice dataQuotaInvoice = (DataQuotaInvoice) baseEntity;
                JSONObject quotaInvoice = new JSONObject();
                quotaInvoice.put("billType", YesfpInvoiceTypeConstants.QUOTA);
                quotaInvoice.put("imageId", dataImageFilesInfo.getNcImageId());
                quotaInvoice.put("id", "");
                quotaInvoice.put("totalAmount", dataQuotaInvoice.getInvoiceTotal());
                quotaInvoice.put("kind", "");
                quotaInvoice.put("invoiceCode", dataQuotaInvoice.getInvoiceCode());
                quotaInvoice.put("invoiceNum", dataQuotaInvoice.getInvoiceNumber());
                return quotaInvoice;
            //  机打发票
            case InvoiceConstants.AIRCRAFT_INVOICE:
                JSONObject aircraft = new JSONObject();
                DataAircraftInvoice dataAircraftInvoice = (DataAircraftInvoice) baseEntity;
                aircraft.put("billType", YesfpInvoiceTypeConstants.MACHINE);
                aircraft.put("imageId", dataImageFilesInfo.getNcImageId());
                aircraft.put("date", DateUtil.format(dataAircraftInvoice.getInvoiceDate(),"yyyyMMdd"));
                aircraft.put("kind", "");
                aircraft.put("filePath", "");
                aircraft.put("sellerName", dataAircraftInvoice.getSellerName());
                aircraft.put("buyerName", dataAircraftInvoice.getBuyerName());
                aircraft.put("invoiceCode", dataAircraftInvoice.getInvoiceCode());
                aircraft.put("invoiceNum", dataAircraftInvoice.getInvoiceNumber());
                aircraft.put("buyerTaxId", dataAircraftInvoice.getBuyerTaxid());
                aircraft.put("checkCode", dataAircraftInvoice.getCheckCode());
                aircraft.put("totalAmount", dataAircraftInvoice.getInvoiceTotal());
                aircraft.put("purchaserStatus", "");
                aircraft.put("sellerTaxId", dataAircraftInvoice.getSellerTaxid());
                aircraft.put("itemList", "");
                aircraft.put("id", "");
                aircraft.put("time", "");
                aircraft.put("category", "");
                return aircraft;
            //  出租车发票
            case InvoiceConstants.TAXI_TICKETS:
                JSONObject taxiTickets = new JSONObject();
                DataTaxiTickets dataTaxiTickets = (DataTaxiTickets) baseEntity;
                taxiTickets.put("billType", YesfpInvoiceTypeConstants.TAXI);
                taxiTickets.put("id", "");
                taxiTickets.put("date", DateUtil.format(dataTaxiTickets.getInvoiceDate(),"yyyyMMdd"));
                taxiTickets.put("kind", "");
                taxiTickets.put("invoiceCode", dataTaxiTickets.getInvoiceCode());
                taxiTickets.put("invoiceNum", dataTaxiTickets.getInvoiceNumber());
                taxiTickets.put("totalAmount", dataTaxiTickets.getInvoiceTotal());
                taxiTickets.put("startTime", dataTaxiTickets.getTimeGetOn());
                taxiTickets.put("endTime", dataTaxiTickets.getTimeGetOff());
                taxiTickets.put("place", dataTaxiTickets.getPlace());
                taxiTickets.put("mileage", dataTaxiTickets.getMileage());
                taxiTickets.put("imageId", dataImageFilesInfo.getNcImageId());
                return taxiTickets;
            //  火车票
            case InvoiceConstants.RAILWAY_TICKET:
                JSONObject railwayTicket = new JSONObject();
                DataRailwayTicket dataRailwayTicket = (DataRailwayTicket) baseEntity;
                railwayTicket.put("billType", YesfpInvoiceTypeConstants.TRAIN);
                railwayTicket.put("imageId", dataImageFilesInfo.getNcImageId());
                railwayTicket.put("date", DateUtil.format(dataRailwayTicket.getInvoiceDate(),"yyyyMMdd"));
                railwayTicket.put("thirdVerifyStatus",0.0);
                railwayTicket.put("level",dataRailwayTicket.getSeat());
                railwayTicket.put("kind","");
                railwayTicket.put("origin",dataRailwayTicket.getStationGetOn());
                railwayTicket.put("yyVerifyStatus",0.0);
                railwayTicket.put("destination",dataRailwayTicket.getStationGetOff());
                railwayTicket.put("idNumber",dataRailwayTicket.getIdNumber());
                railwayTicket.put("invoiceNum",dataRailwayTicket.getInvoiceNumber());
                railwayTicket.put("number",dataRailwayTicket.getInvoiceNumber());
                railwayTicket.put("totalAmount",dataRailwayTicket.getInvoiceTotal());
                railwayTicket.put("trainNum",dataRailwayTicket.getTrainNumber());
                railwayTicket.put("name",dataRailwayTicket.getName());
                railwayTicket.put("ticketNum",dataRailwayTicket.getTrainNumber());
                railwayTicket.put("time",dataRailwayTicket.getInvoiceTime());
                return railwayTicket;
            //  客运汽车票
            case InvoiceConstants.PASSENGER_TICKET:
                JSONObject passengerTicket = new JSONObject();
                DataPassengerTicket dataPassengerTicket = (DataPassengerTicket) baseEntity;
                passengerTicket.put("billType", YesfpInvoiceTypeConstants.PASSENGER);
                passengerTicket.put("imageId", dataImageFilesInfo.getNcImageId());
                //jsonObject.put("id", passengerTicket.getId());
                passengerTicket.put("id", "");
                passengerTicket.put("date", DateUtil.format(dataPassengerTicket.getInvoiceDate(),"yyyyMMdd"));
                passengerTicket.put("kind", "");
                passengerTicket.put("invoiceCode",dataPassengerTicket.getInvoiceCode());
                passengerTicket.put("invoiceNum", dataPassengerTicket.getInvoiceNumber());
                passengerTicket.put("exit", dataPassengerTicket.getStationGetOff());
                passengerTicket.put("totalAmount", dataPassengerTicket.getInvoiceTotal());
                passengerTicket.put("name", dataPassengerTicket.getName());
                passengerTicket.put("time", dataPassengerTicket.getInvoiceTime());
                passengerTicket.put("entrance", dataPassengerTicket.getStationGetOn());
                return passengerTicket;
            //  航空运输电子客票行程单
            case InvoiceConstants.FLIGHT_ITINERARY:
                JSONObject flightItinerary = new JSONObject();
                DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
                DataFlightsBo dataFlightsBo = new DataFlightsBo();
                dataFlightsBo.setFileId(dataFlightItinerary.getFileId());
                List<DataFlightsVo> dataFlightsVoList = dataFlightsService.queryList(dataFlightsBo);
                flightItinerary.put("billType", YesfpInvoiceTypeConstants.AIR);
                flightItinerary.put("imageId", dataImageFilesInfo.getNcImageId());
                //jsonObject.put("id", flightItinerary.getId());
                flightItinerary.put("id", "");
                flightItinerary.put("date", DateUtil.format(dataFlightItinerary.getInvoiceDate(),"yyyyMMdd"));
                flightItinerary.put("fare", dataFlightItinerary.getFare());
                flightItinerary.put("agentCode", dataFlightItinerary.getAgentCode());
                flightItinerary.put("issueBy", dataFlightItinerary.getIssueBy());
                flightItinerary.put("kind", "");
                flightItinerary.put("caacDevelopFund", dataFlightItinerary.getCaacDevelopmentFund());
                flightItinerary.put("checkCode", dataFlightItinerary.getCheckCode());
                flightItinerary.put("totalAmount", dataFlightItinerary.getInvoiceTotal());
                flightItinerary.put("ticketNum", dataFlightItinerary.getInvoiceNumber());
                flightItinerary.put("uniqueCode", "");
                flightItinerary.put("airportType", dataFlightItinerary.getInternationalFlag());
                flightItinerary.put("airType", dataFlightItinerary.getInternationalFlag());
                flightItinerary.put("receiptNumber", dataFlightItinerary.getInvoiceNumber());
                List<JSONObject> jsons = new ArrayList<>();
                if(CollectionUtil.isNotEmpty(dataFlightsVoList)){
                    flightItinerary.put("userName", dataFlightsVoList.get(0).getUserName());
                    flightItinerary.put("userId", dataFlightsVoList.get(0).getUserId());
                    dataFlightsVoList.forEach((flight) -> {
                        JSONObject json = new JSONObject(); //保存台账时，明细中Id字段需要置空
                        json.put("id", "");
                        json.put("fileId", "");
                        json.put("airId", "");
                        json.put("from", flight.getStationGetOff());
                        json.put("to", flight.getStationGetOff());
                        json.put("flightNumber", flight.getFlightNumber());
                        json.put("date", DateUtil.format(flight.getInvoiceDate(),"yyyyMMdd"));
                        json.put("time", flight.getInvoiceTime());
                        json.put("seat", flight.getSeat());
                        json.put("carrier", flight.getCarrier());
                        jsons.add(json);
                    });
                }
                flightItinerary.put("itemList", jsons);
                flightItinerary.put("fuelSurcharge", dataFlightItinerary.getFuelSurcharge());
                return flightItinerary;
            //  过路费
            case InvoiceConstants.TOLL_ROADS:
                JSONObject tollRoads = new JSONObject();
                DataTollRoads dataTollRoads = (DataTollRoads) baseEntity;
                tollRoads.put("billType", YesfpInvoiceTypeConstants.TOLLS);
                //jsonObject.put("id", tollRoads.getId());
                tollRoads.put("id", "");
                tollRoads.put("date", DateUtil.format(dataTollRoads.getInvoiceDate(),"yyyyMMdd"));
                tollRoads.put("exit", dataTollRoads.getTollExit());
                tollRoads.put("totalAmount", dataTollRoads.getInvoiceTotal());
                tollRoads.put("kind", "");
                tollRoads.put("time", dataTollRoads.getInvoiceTime());
                tollRoads.put("entrance", dataTollRoads.getEntrance());
                tollRoads.put("invoiceCode", dataTollRoads.getInvoiceCode());
                tollRoads.put("invoiceNum", dataTollRoads.getInvoiceNumber());
                tollRoads.put("imageId", dataImageFilesInfo.getNcImageId());
                return tollRoads;
            //  其他发票
            case InvoiceConstants.INVOICE_OTHERS:
                break;
        }
        return null;
    }
}
