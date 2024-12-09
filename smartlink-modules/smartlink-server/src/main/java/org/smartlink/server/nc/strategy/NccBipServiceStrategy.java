package org.smartlink.server.nc.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.constant.HttpStatus;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.*;
import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.dto.*;
import org.smartlink.server.nc.domain.dto.biprequest.BipSubBIPOcrRequest;
import org.smartlink.server.nc.domain.dto.bipresponse.BipResponse;
import org.smartlink.server.nc.domain.invoice.DataMotorVehicleSale;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.smartlink.server.nc.domain.invoice.DataUsedCarSales;
import org.smartlink.server.nc.domain.invoice.dto.InvoiceCheckParamDTO;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.enumd.CheckConstants;
import org.smartlink.server.nc.enumd.CheckSupplierEnum;
import org.smartlink.server.nc.enumd.NccBipInvoiceAllTypeEnum;
import org.smartlink.server.nc.enumd.NccBipInvoiceTypeEnum;
import org.smartlink.server.nc.enums.BIPTaskStateEnum;
import org.smartlink.server.nc.mapper.DataCmInfoMapper;
import org.smartlink.server.nc.mapper.DataCurrentTaskMapper;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.IDataCheckStatisticsService;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.factory.NCCBIPFactory;
import org.smartlink.server.nc.ocr.service.nccbip.conversion.NccBipBasicOcrInfo;
import org.smartlink.server.nc.ocr.service.nccbip.conversion.NccBipChangeInvoiceDetails;
import org.smartlink.server.nc.ocr.service.nccbip.response.NccBipCheckResponse;
import org.smartlink.server.nc.ocr.service.nccbip.response.NccBipOcrResponse;
import org.smartlink.server.nc.ocr.service.nccbip.resquest.NccBipCheckRequest;
import org.smartlink.server.nc.ocr.service.nccbip.resquest.NccBipOcrRequest;
import org.smartlink.server.nc.ocr.service.nccbip.resquest.NccBipSaveLedgerRequest;
import org.smartlink.server.nc.oss.factory.OssFactory;
import org.smartlink.server.nc.properties.BipParamProperties;
import org.smartlink.server.nc.service.nc.IDataCmInfoService;
import org.smartlink.server.nc.service.nc.IDataCurrentTaskService;
import org.smartlink.server.nc.service.nc.IDataImageFilesInfoService;
import org.smartlink.server.nc.service.nc.IDataOcrService;
import org.smartlink.server.nc.utils.BatchIdUtils;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.smartlink.server.nc.utils.GetRequestUtils;
import org.smartlink.server.nc.utils.WebClientUtil;
import org.smartlink.server.nc.utils.biptoken.BIpTokenUtils;
import org.smartlink.server.nc.utils.biptoken.response.Token;
import org.smartlink.server.nc.utils.document.PDFUtil;
import org.smartlink.server.nc.utils.file.FileUtils;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;
import org.smartlink.server.task.util.OfdUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author L
 * @title BIP服务
 * @description BIP服务
 * @date
 */
@Slf4j
@Component
public class NccBipServiceStrategy implements INcStrategy{

    private BipParamProperties bipParamProperties = new BipParamProperties();
    private final IDataCurrentTaskService iDataCurrentTaskService;
    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataOcrService dataOcrService;
    private final IDataCmInfoService cmInfoService;
    private final NccBipBasicOcrInfo nccBipBasicOcrInfo;
    private final IDataCheckStatisticsService dataCheckStatisticsService;
    private final NccBipChangeInvoiceDetails nccBipChangeInvoiceDetails;
    private final DataCurrentTaskMapper currentTaskMapper;
    private final DataCmInfoMapper cmInfoMapper;

    public NccBipServiceStrategy(IDataCurrentTaskService iDataCurrentTaskService, IDataImageFilesInfoService dataImageFilesInfoService, IDataOcrService dataOcrService, IDataCmInfoService cmInfoService, NccBipBasicOcrInfo nccBipBasicOcrInfo, IDataCheckStatisticsService dataCheckStatisticsService, NccBipChangeInvoiceDetails nccBipChangeInvoiceDetails, DataCurrentTaskMapper currentTaskMapper, DataCmInfoMapper cmInfoMapper) {
        this.iDataCurrentTaskService = iDataCurrentTaskService;
        this.dataImageFilesInfoService = dataImageFilesInfoService;
        this.dataOcrService = dataOcrService;
        this.cmInfoService = cmInfoService;
        this.nccBipBasicOcrInfo = nccBipBasicOcrInfo;
        this.dataCheckStatisticsService = dataCheckStatisticsService;
        this.nccBipChangeInvoiceDetails = nccBipChangeInvoiceDetails;
        this.currentTaskMapper = currentTaskMapper;
        this.cmInfoMapper = cmInfoMapper;
    }

    @Override
    public void deleteNcInvoiceDataBusinessService(NcDeleteServiceDTO ncDeleteServiceDTO) throws Exception {
        DataCurrentTask dataCurrentTask = iDataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(ncDeleteServiceDTO.getBusinessSerialNo());
        WebClient webClient = WebClient.builder()
            .exchangeStrategies(builder ->
                builder.codecs(codecs -> codecs.defaultCodecs().
                    maxInMemorySize(20 * 1024 * 1024))).build();
        List<BipDeleteOcrRequest.DataDTO> data = new ArrayList<>();
        List<String> fileIdList = ncDeleteServiceDTO.getFileIdList();
        for (String s : fileIdList) {
            String fpdm = null;
            String fphm = null;
            String token = null;
            String pushBusinessInfoFlag = null;
            ArrayList<DataImageFilesInfo> dataImageFilesInfoArrayList = new ArrayList<>();
            DataImageFilesInfo imageFilesInfo = dataImageFilesInfoService.selectById(s);
            if (StrUtil.equals(InvoiceConstants.INVOICE_MUCH_NCC, imageFilesInfo.getFileType())) {
                List<String> includeTypeArr = StrUtil.split(imageFilesInfo.getIncludeTypeArr(), ",");
                for (String type : includeTypeArr) {
                    DataImageFilesInfo dataImageFilesInfo = new DataImageFilesInfo();
                    BeanUtil.copyProperties(imageFilesInfo, dataImageFilesInfo);
                    dataImageFilesInfo.setFileType(type);
                    dataImageFilesInfoArrayList.add(dataImageFilesInfo);
                }
            } else {
                dataImageFilesInfoArrayList.add(imageFilesInfo);
            }
            for (DataImageFilesInfo dataImageFilesInfo : dataImageFilesInfoArrayList) {
                List<BaseEntity> baseEntityList = dataOcrService.ocrQueryByFileIdAll(dataImageFilesInfo);
                if (CollUtil.isEmpty(baseEntityList)) {
                    continue;
                }
                for (BaseEntity baseEntity : baseEntityList) {
                    String className = InvoiceConstants.INVOICE_ClASS_TYPE.get(dataImageFilesInfo.getFileType());
                    if (className != null) {
                        Class<?> invoiceClass = Class.forName(className);
                        try {
                            Field tokenField = invoiceClass.getDeclaredField("saveToken");
                            tokenField.setAccessible(true);
                            token = (String) tokenField.get(baseEntity);
                        } catch (Exception e) {
                            log.error("当前发票类型没有saveToken:{}", e.getMessage());
                        }
                        try {
                            Field invoiceCodeField = invoiceClass.getDeclaredField("invoiceCode");
                            invoiceCodeField.setAccessible(true);
                            fpdm = (String) invoiceCodeField.get(baseEntity);
                        } catch (Exception e) {
                            log.error("当前发票类型没有invoiceCode:{}", e.getMessage());
                        }
                        try {
                            Field invoiceNumberField = invoiceClass.getDeclaredField("invoiceNumber");
                            invoiceNumberField.setAccessible(true);
                            fphm = (String) invoiceNumberField.get(baseEntity);
                        } catch (Exception e) {
                            log.error("当前发票类型没有invoiceNumber:{}", e.getMessage());
                        }
                        try {
                            Field invoiceNumberField = invoiceClass.getDeclaredField("pushBusinessInfoFlag");
                            invoiceNumberField.setAccessible(true);
                            pushBusinessInfoFlag = (String) invoiceNumberField.get(baseEntity);
                        } catch (Exception e) {
                            log.error("当前发票类型没有pushBusinessInfoFlag:{}", e.getMessage());
                        }
                    }
                    BipDeleteOcrRequest.DataDTO dataDTO = new BipDeleteOcrRequest.DataDTO();
                    String sysType = NccBipInvoiceAllTypeEnum.getSysType(dataImageFilesInfo.getFileType());
                    if (StrUtil.equals(NccBipInvoiceAllTypeEnum.OTHER.getBipInvoiceType(), sysType)) {
                        continue;
                    }
                    if (StrUtil.isBlank(pushBusinessInfoFlag) || !StrUtil.equals("1", pushBusinessInfoFlag)) {
                        continue;
                    }
                    if (StrUtil.equals(InvoiceConstants.RECEIPT, dataImageFilesInfo.getFileType())) {
                        sysType = NccBipInvoiceAllTypeEnum.OTHER.getBipInvoiceType();
                    }
                    dataDTO.setBillType(sysType);
                    dataDTO.setSaveToken(token);
                    BipDeleteOcrRequest.DataDTO.Invoice invoice = new BipDeleteOcrRequest.DataDTO.Invoice();
                    invoice.setFpdm(fpdm);
                    invoice.setFphm(fphm);
                    dataDTO.setData(invoice);
                    data.add(dataDTO);
                }
            }
        }
        if (CollUtil.isEmpty(data)) {
            return;
        }
        BipDeleteOcrRequest request = new BipDeleteOcrRequest();
        //request.setYtenantid(TenantContextHolder.getTenantId());
        request.setUuId(IdUtil.simpleUUID());
        request.setBarcode(dataCurrentTask.getBarCode());
        request.setOpTime(DateUtil.now());
        request.setFactorycode(bipParamProperties.getFactoryCode());
        request.setData(data);
        request.setBillid(dataCurrentTask.getBusinessSerialNo());
        try {
            Token token = BIpTokenUtils.getToken(JSONObject.toJSONString(bipParamProperties));
            String requestDeleteBip = JSONObject.toJSONString(request);
            log.info("=================BIP调用删除参数：" + requestDeleteBip);
            String block = webClient.post().uri(bipParamProperties.getDelInvoiceUrl() + "?access_token=" + token.getData().getAccessToken())
                .contentType(MediaType.APPLICATION_STREAM_JSON).bodyValue(requestDeleteBip).header("Content-Type", MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToMono(String.class).block();
            log.info("=================BIP调用删除结果：" + block);
        } catch (Exception e) {
            log.error("调用BIP删除异常:{}", ExceptionUtil.getExceptionMessage(e));
        }
    }

    @Override
    public DataCurrentTask submitTaskStateToBusinessService(TaskSubmitDTO taskSubmitDTO) throws Exception {
        Token token = null;
        try {
            token = BIpTokenUtils.getToken(JSONObject.toJSONString(bipParamProperties));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataCurrentTask dataCurrentTask = taskSubmitDTO.getDataCurrentTask();
        BipSubBIPOcrRequest bipSubBIPOcrRequest = new BipSubBIPOcrRequest();
        //bipSubBIPOcrRequest.setYtenantid(taskSubmitDTO.getTenantId());
        bipSubBIPOcrRequest.setUuId(IdUtil.simpleUUID());
        bipSubBIPOcrRequest.setBarcode(dataCurrentTask.getBillNum());
        bipSubBIPOcrRequest.setOpTime(DateUtil.now());
        bipSubBIPOcrRequest.setFactorycode(bipParamProperties.getFactoryCode());
        bipSubBIPOcrRequest.setBillid(dataCurrentTask.getBusinessSerialNo());
        String state = null;
        //状态变更 待扫描 待补扫 待重扫
        if (dataCurrentTask.getTaskState().equalsIgnoreCase(TaskStateConstants.TASK_STATE_BH_BS)) {
            //待补扫
            dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BS_WC);
            state = BIPTaskStateEnum.TASK_STATE_MAKEUP_DOWN.getState();
        }
        if (dataCurrentTask.getTaskState().equalsIgnoreCase(TaskStateConstants.TASK_STATE_BH_CS)) {
            //待重扫
            state = BIPTaskStateEnum.TASK_STATE_RESCAN_DOWN.getState();
            dataCurrentTask.setRescanFileSum("");
            dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_CS_WC);
        }
        if (dataCurrentTask.getTaskState().equalsIgnoreCase(TaskStateConstants.TASK_STATE_SCAN)) {
            //扫描完成
            state = BIPTaskStateEnum.TASK_STATE_COMPLETE.getState();
            dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_COMPLETE);
        }
        //状态不变更 补扫完成 扫描完成 重扫完成
        if (dataCurrentTask.getTaskState().equalsIgnoreCase(TaskStateConstants.TASK_STATE_COMPLETE)) {
            //扫描完成
            state = BIPTaskStateEnum.TASK_STATE_COMPLETE.getState();
        }
        if (dataCurrentTask.getTaskState().equalsIgnoreCase(TaskStateConstants.TASK_STATE_BS_WC)) {
            //补扫完成
            state = BIPTaskStateEnum.TASK_STATE_MAKEUP_DOWN.getState();

        }
        if (dataCurrentTask.getTaskState().equalsIgnoreCase(TaskStateConstants.TASK_STATE_CS_WC)) {
            //重扫完成
            dataCurrentTask.setRescanFileSum("");
            state = BIPTaskStateEnum.TASK_STATE_RESCAN_DOWN.getState();
        }
        List<DataCmInfo> dataCmInfoList = cmInfoService.selectDataCmInfoByBusinessSerialNo(dataCurrentTask.getBusinessSerialNo());
        List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchId(dataCmInfoList.get(0).getBatchId());
        List<DataImageFilesInfo> filterDataImageFilesInfoList = dataImageFilesInfoList.stream().filter((obj) -> !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.INVOICE_CHECK_SUCCESS) && !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.SAVED_SUCCESSFULLY) && !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.INVOICE_UPDATE)).collect(Collectors.toList());
        if (filterDataImageFilesInfoList.size() > 0) {
            throw new Exception("提交影像状态失败，该单据下包含异常状态发票");
        }
        int i = 0;
        for (DataImageFilesInfo imageFilesInfo : dataImageFilesInfoList) {
            if (StrUtil.isNotBlank(imageFilesInfo.getFileFlowStatus()) && imageFilesInfo.getFileFlowStatus().equalsIgnoreCase("4")) {
                imageFilesInfo.setFileFlowStatus("2");
                imageFilesInfo.updateById();
            }
            for (Map.Entry<String, String> entry : InvoiceConstants.INVOICE_ClASS_TYPE.entrySet()) {
                if (imageFilesInfo.getFileType().equalsIgnoreCase(entry.getKey())) {
                    i += 1;
                }
            }
        }
        dataCurrentTask.setScanFileSum(String.valueOf(dataImageFilesInfoList.size()));
        dataCurrentTask.updateById();
        BipSubBIPOcrRequest.DataDTO dataDTO = new BipSubBIPOcrRequest.DataDTO();
        dataDTO.setImagestate(state);
        dataDTO.setImagenum(String.valueOf(dataImageFilesInfoList.size()));
        dataDTO.setInvoicenum(String.valueOf(i));
        dataDTO.setOpuserid(taskSubmitDTO.getUserId());
        dataDTO.setOpusercode(taskSubmitDTO.getUserCode());
        dataDTO.setOpusername(taskSubmitDTO.getUserName());
        //dataDTO.setOptime(DateUtil.now());
        //bipSubBIPOcrRequest.setYtenantid(TenantContextHolder.getTenantId());
        bipSubBIPOcrRequest.setData(dataDTO);
        String s = JSONObject.toJSONString(bipSubBIPOcrRequest);
        log.info("BIP提交参数:" + s);
        String ref = (String) WebClientUtil.postJson(bipParamProperties.getSubmitUrl() + "?access_token=" + token.getData().getAccessToken(), s);
        BipResponse bipResponse = JSONObject.parseObject(ref, BipResponse.class);
        log.info("BIP返回:" + ref);
        if (!bipResponse.getCode().equalsIgnoreCase(String.valueOf(HttpStatus.SUCCESS))) {
            throw new Exception("提交影像状态失败，" + bipResponse.getMessage());
        }
        return dataCurrentTask;
    }

    @Override
    public DataCurrentTask rejectTaskStateBusinessService(UpdateTaskDTO updateTaskDTO) throws Exception {
        Token token = null;
        try {
            token = BIpTokenUtils.getToken(JSONObject.toJSONString(bipParamProperties));
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataCurrentTask dataCurrentTask = updateTaskDTO.getDataCurrentTask();
        BipSubBIPOcrRequest bipSubBIPOcrRequest = new BipSubBIPOcrRequest();
        //bipSubBIPOcrRequest.setYtenantid(updateTaskDTO.getTenantId());
        bipSubBIPOcrRequest.setBarcode(dataCurrentTask.getBillNum());
        bipSubBIPOcrRequest.setOpTime(DateUtil.now());
        bipSubBIPOcrRequest.setFactorycode(bipParamProperties.getFactoryCode());
        bipSubBIPOcrRequest.setUuId(IdUtil.simpleUUID());
        bipSubBIPOcrRequest.setBillid(dataCurrentTask.getBusinessSerialNo());
        String state = updateTaskDTO.getUpdateState();
        if (state.equalsIgnoreCase(TaskStateConstants.TASK_STATE_BH_CS)) {
            //驳回重扫
            dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BH_CS);
            state = BIPTaskStateEnum.TASK_STATE_RESCAN.getState();
        }
        if (state.equalsIgnoreCase(TaskStateConstants.TASK_STATE_BH_BS)) {
            //驳回补扫
            dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BH_BS);
            state = BIPTaskStateEnum.TASK_STATE_MAKEUP.getState();
        }
        List<DataCmInfo> dataCmInfoList = cmInfoService.selectDataCmInfoByBusinessSerialNo(dataCurrentTask.getBusinessSerialNo());
        List<DataImageFilesInfo> dataImageFilesInfoList = dataImageFilesInfoService.selectByBatchId(dataCmInfoList.get(0).getBatchId());
        List<DataImageFilesInfo> filterDataImageFilesInfoList = dataImageFilesInfoList.stream().filter((obj) -> !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.INVOICE_CHECK_SUCCESS) && !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.SAVED_SUCCESSFULLY) && !StrUtil.equals(obj.getFileStatus(), FileStatusConstants.INVOICE_UPDATE)).collect(Collectors.toList());
        if (filterDataImageFilesInfoList.size() > 0) {
            throw new Exception("提交影像状态失败，该单据下包含异常状态发票");
        }
        int i = 0;
        for (DataImageFilesInfo imageFilesInfo : dataImageFilesInfoList) {
            for (Map.Entry<String, String> entry : InvoiceConstants.INVOICE_ClASS_TYPE.entrySet()) {
                if (imageFilesInfo.getFileType().equalsIgnoreCase(entry.getKey())) {
                    i += 1;
                }
            }
        }
        BipSubBIPOcrRequest.DataDTO dataDTO = new BipSubBIPOcrRequest.DataDTO();
        dataDTO.setImagestate(state);
        dataDTO.setImagenum(String.valueOf(dataImageFilesInfoList.size()));
        dataDTO.setInvoicenum(String.valueOf(i));
        dataDTO.setOpuserid(updateTaskDTO.getUserId());
        dataDTO.setOpusercode(updateTaskDTO.getUserCode());
        dataDTO.setOpusername(updateTaskDTO.getUserName());
        //dataDTO.setOptime(DateUtil.now());
        //bipSubBIPOcrRequest.setYtenantid(TenantContextHolder.getTenantId());
        bipSubBIPOcrRequest.setData(dataDTO);
        String s = JSONObject.toJSONString(bipSubBIPOcrRequest);
        log.info("BIP驳回参数:" + s);
        String ref = (String) WebClientUtil.postJson(bipParamProperties.getSubmitUrl() + "?access_token=" + token.getData().getAccessToken(), s);
        BipResponse bipResponse = JSONObject.parseObject(ref, BipResponse.class);
        log.info("BIP返回:" + ref);
        if (!bipResponse.getCode().equalsIgnoreCase(String.valueOf(HttpStatus.SUCCESS))) {
            throw new Exception("变更影像状态失败，" + bipResponse.getMessage());
        }
        return dataCurrentTask;
    }

    @Override
    public DataImageFilesInfo doBusinessService(NcImageServiceDTO ncImageServiceDTO) throws ClassNotFoundException {
        DataImageFilesInfo dataImageFilesInfo = ncImageServiceDTO.getDataImageFilesInfo();
        ncImageServiceDTO.setFactoryCode(bipParamProperties.getFactoryCode());
        ncImageServiceDTO.setOpTime(DateUtil.now());
        // 设置缓冲区大小，用以拿到base64
        WebClient webClient = WebClient.builder()
            .exchangeStrategies(builder ->
                builder.codecs(codecs -> codecs.defaultCodecs().
                    maxInMemorySize(20 * 1024 * 1024))).build();
        NccBipOcrRequest nccBipOcrRequest = new NccBipOcrRequest();
        BeanUtil.copyProperties(ncImageServiceDTO, nccBipOcrRequest);
        NccBipOcrRequest.DataDTO dataDTO = new NccBipOcrRequest.DataDTO();
        dataDTO.setFile(ncImageServiceDTO.getFile());
        //nccBipOcrRequest.setYtenantId(TenantContextHolder.getTenantId());
        nccBipOcrRequest.setData(dataDTO);
        nccBipOcrRequest.setFactoryCode(bipParamProperties.getFactoryCode());
        nccBipOcrRequest.setUuId(IdUtil.simpleUUID());
        String request = JSONObject.toJSONString(nccBipOcrRequest);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("data");
        log.info("BIP OCR参数:" + JSON.toJSONString(nccBipOcrRequest, filter));
        List<IdentificationData> identificationDataList = new ArrayList<>();
        HashSet<String> typeSet = new HashSet<>();
        ArrayList<String> msg = new ArrayList<>();
        try {
            Token token = BIpTokenUtils.getToken(JSONObject.toJSONString(bipParamProperties));
            NCCBIPFactory nccbipFactory = new NCCBIPFactory();
            String block = webClient.post().uri(bipParamProperties.getOcrUrl() + "?access_token=" + token.getData().getAccessToken())
                .contentType(MediaType.APPLICATION_STREAM_JSON).bodyValue(request).header("Content-Type", MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToMono(String.class).block();
            log.info("------------------------------识别结果为：" + block);
            NccBipOcrResponse yesfpOcrResponse = JSONObject.parseObject(block, NccBipOcrResponse.class);
            if ("1023070105428".equalsIgnoreCase(yesfpOcrResponse.getCode())) {
                dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                dataImageFilesInfo.setMessage("上传成功");
                dataImageFilesInfoService.updateById(dataImageFilesInfo);
                return dataImageFilesInfo;
            } else if (!yesfpOcrResponse.getCode().equals(String.valueOf(HttpStatus.SUCCESS))) {
                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                dataImageFilesInfo.setMessage(yesfpOcrResponse.getMessage());
                dataImageFilesInfoService.updateById(dataImageFilesInfo);
                throw new OcrException("识别失败！" + yesfpOcrResponse.getMessage());
            } else if (!yesfpOcrResponse.getData().getCode().equalsIgnoreCase(String.valueOf(HttpStatus.SUCCESS))) {
                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                dataImageFilesInfo.setMessage(yesfpOcrResponse.getData().getMsg());
                dataImageFilesInfoService.updateById(dataImageFilesInfo);
                throw new OcrException("识别失败！" + yesfpOcrResponse.getData().getMsg());
            }
            String fileSuffix = FileUtils.getFileSuffix(dataImageFilesInfo.getFileName());
            if (!ArrayUtil.containsIgnoreCase(MimeTypeUtils.IMAGE_EXTENSION, fileSuffix)) {
                UploadResult uploadImg = OssFactory.instance().upload(PDFUtil.PDFToImg(ncImageServiceDTO.getPByte(), 120), BatchIdUtils.getBatchIdPath(dataImageFilesInfo.getBatchId(), "simg_" + dataImageFilesInfo.getFileId()), fileSuffix);
                dataImageFilesInfo.setSurl(uploadImg.getUrl());
                if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.DOCUMENT_OFD_TRANSFOTMATION, fileSuffix)) {
                    UploadResult uploadLimg = OssFactory.instance().upload(OfdUtils.documentToImg(ncImageServiceDTO.getBytes()), BatchIdUtils.getBatchIdPath(dataImageFilesInfo.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), fileSuffix);
                    dataImageFilesInfo.setIurl(uploadLimg.getUrl());
                } else {
                    UploadResult uploadLimg = OssFactory.instance().upload(PDFUtil.convertPdfImage(ncImageServiceDTO.getBytes(), 120), BatchIdUtils.getBatchIdPath(dataImageFilesInfo.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), fileSuffix);
                    dataImageFilesInfo.setIurl(uploadLimg.getUrl());
                }

            }
            List<NccBipOcrResponse.OneDataDTO.DatasDTO> datas = yesfpOcrResponse.getData().getDatas();
            for (NccBipOcrResponse.OneDataDTO.DatasDTO data : datas) {
                IdentificationData identificationData = nccbipFactory.conversionInfo(dataImageFilesInfo, data).changeInfo(dataImageFilesInfo, data);
                identificationDataList.add(identificationData);
                typeSet.add(identificationData.k);
                dataOcrService.ocrInsertBaseEntity(identificationData.k, identificationData.t);

                //非增票推送票据中心
                if (StrUtil.isNotBlank(data.getBillType()) && !StrUtil.equals(data.getBillType(), "invoice")) {
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                    breCheckRepeat(dataImageFilesInfo, identificationData.t, identificationData.k,datas.size()>1);
                    if (StrUtil.equals(FileStatusConstants.INVOICE_ALREADY_EXISTS, dataImageFilesInfo.getFileStatus())) {
                        return dataImageFilesInfo;
                    }
                    log.info("进入非增票推送票据中心,类型:{}", data.getBillType());
                    try {
                        ncImageServiceDTO.setSaveToken(data.getImgOcrToken());
                        ncImageServiceDTO.setInvoiceType(data.getBillType());
                        ncImageServiceDTO.setFpLx(data.getBillType());
                        identificationData.t.setOcrFileType(identificationData.k);
                        InvoiceCheckParamDTO requestCheck = GetRequestUtils.getRequest(identificationData.t);
                        String format = DateUtil.format(requestCheck.getInvoiceDate(), "yyyyMMdd");
                        NccBipSaveLedgerRequest saveLedgerRequest = getNccBipSaveLedgerRequest(ncImageServiceDTO, requestCheck, format);
                        //请求票据中心
                        String saveLock = requestBillCenter(webClient, saveLedgerRequest, token);
                        JSONObject jsonObject = JSON.parseObject(saveLock);
                        assert jsonObject != null;
                        if (StrUtil.isNotBlank(jsonObject.getString("code")) && !StrUtil.equals("200", jsonObject.getString("code"))) {
                            String message = jsonObject.getString("message");
                            log.error("推送票据中心返回异常:{}", message);
                            dataImageFilesInfo.setMessage(message);
                            dataImageFilesInfo.setFileStatus(FileStatusConstants.OCR_SAVE_FAILED);
                            msg.add(StrUtil.concat(true,InvoiceConstants.BIP_INVOICE_TYPE_ENUM.get(identificationData.k),":(",message,")"));
                            return dataImageFilesInfo;
                        } else {
                            //台账推送成功标识
                            ledgerSuccess(identificationData, dataImageFilesInfo);
                        }
                    } catch (Exception e) {
                        log.error("非增票推送票据中心异常: {}", ExceptionUtil.getExceptionMessage(e));
                        dataImageFilesInfo.setMessage(e.getMessage());
                        dataImageFilesInfo.setFileStatus(FileStatusConstants.OCR_SAVE_FAILED);
                        return dataImageFilesInfo;
                    }
                }
            }
        } catch (Exception e) {
            log.error("识别异常: {}", ExceptionUtil.getExceptionMessage(e));
            dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
            dataImageFilesInfo.setMessage("识别失败:" + e.getMessage());
            dataImageFilesInfoService.updateById(dataImageFilesInfo);
            return dataImageFilesInfo;
        }
        Boolean checkMark = false;
        for (IdentificationData identificationData : identificationDataList) {
            //发票查验
            checkMark = getCheck(ncImageServiceDTO, dataImageFilesInfo, webClient, checkMark, identificationData);
            if(StrUtil.equals(FileStatusConstants.OCR_SAVE_FAILED,dataImageFilesInfo.getFileStatus()) || StrUtil.equals(FileStatusConstants.INVOICE_ALREADY_EXISTS, dataImageFilesInfo.getFileStatus())){
                setCheckInfo(identificationDataList, typeSet, dataImageFilesInfo);
                return dataImageFilesInfo;
            }
        }
        if (checkMark) {
            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
        }
        setCheckInfo(identificationDataList, typeSet, dataImageFilesInfo);
        return dataImageFilesInfo;
    }

    private void setCheckInfo(List<IdentificationData> identificationDataList, HashSet<String> typeSet, DataImageFilesInfo dataImageFilesInfo) {
        if (identificationDataList.size() > 1) {
            String typeArr = String.join(",", typeSet);
            dataImageFilesInfo.setIncludeTypeArr(typeArr);
            dataImageFilesInfo.setFileType(InvoiceConstants.INVOICE_MUCH_NCC);
        }
        dataCheckStatisticsService.insertCheckInfo(CheckSupplierEnum.YESFP_NC_SIX_FIVE.getCode(), CheckConstant.SUCCESS_CHECK);
    }
    public Boolean getCheck(NcImageServiceDTO ncImageServiceDTO, DataImageFilesInfo dataImageFilesInfo, WebClient webClient, Boolean checkMark, IdentificationData identificationData) {
        //查验逻辑
        if (identificationData.k.equals(InvoiceConstants.TAX_INVOICE) || identificationData.k.equals(InvoiceConstants.MOTOR_VEHICLE_SALE) || identificationData.k.equals(InvoiceConstants.USED_CAR_SALES)) {
            ncImageServiceDTO.setFactoryCode(bipParamProperties.getFactoryCode());
            dataImageFilesInfo.setMessage("查验成功！");
            NccBipCheckRequest paramDTO = new NccBipCheckRequest();
            InvoiceCheckParamDTO requestCheck = GetRequestUtils.getRequest(identificationData.t);
            paramDTO.setUuId(IdUtil.simpleUUID());
            paramDTO.setBarCode(ncImageServiceDTO.getBarcode());
            paramDTO.setOpTime(DateUtil.now());
            paramDTO.setFactoryCode(ncImageServiceDTO.getFactoryCode());
            paramDTO.setBillId(ncImageServiceDTO.getBillId());
            NccBipCheckRequest.DataDTO invoicesDTO = new NccBipCheckRequest.DataDTO();
            // 发票号码
            invoicesDTO.setFpHm(requestCheck.getInvoiceNumber());
            // 发票代码
            invoicesDTO.setFpDm(requestCheck.getInvoiceCode());
            // 发票日期
            String format = DateUtil.format(requestCheck.getInvoiceDate(), "yyyyMMdd");
            invoicesDTO.setKprq(StrUtil.isEmpty(format) ? "" : format);
            // 总金额
            invoicesDTO.setHjje(requestCheck.getTotal());
            // 校验码
            invoicesDTO.setJym(requestCheck.getCheckCode());
            //价税合计
            invoicesDTO.setJshj(requestCheck.getTotalLowercase());
            //数电票号码
            invoicesDTO.setElectronic_number(requestCheck.getElectronicNumber());

            List<NccBipCheckRequest.DataDTO> list = new ArrayList<>(1);
            list.add(invoicesDTO);
            paramDTO.setData(list);
            NccBipCheckResponse response = null;
            try {
                Token token = BIpTokenUtils.getToken(com.alibaba.fastjson2.JSONObject.toJSONString(bipParamProperties));
                String requestCheckBip = com.alibaba.fastjson2.JSONObject.toJSONString(paramDTO);
                log.info("请求查验体: {}", requestCheckBip);
                String block = webClient.post().uri(bipParamProperties.getCheckUrl() + "?access_token=" + token.getData().getAccessToken())
                    .contentType(MediaType.APPLICATION_STREAM_JSON).bodyValue(requestCheckBip).header("Content-Type", MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToMono(String.class).block();
                log.info("------------------------------查验结果为：" + block);
                response = com.alibaba.fastjson2.JSONObject.parseObject(block, NccBipCheckResponse.class);
                if (!"200".equals(response.getCode()) || BeanUtil.isEmpty(response.getDataBip())) {
                    checkMark = true;
                    dataImageFilesInfo.setMessage(response.getMsg());
                } else if (!"200".equals(response.getDataBip().get(0).getCode()) || BeanUtil.isEmpty(response.getDataBip().get(0).getInvoice())) {
                    checkMark = true;
                    dataImageFilesInfo.setMessage(response.getDataBip().get(0).getMsg());
                } else {
                    // 更新OCR信息
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
                    BaseEntity entity = this.updateInvoicesInfo(dataImageFilesInfo, response, requestCheck.getId());
                    log.info("BIP转换后的OCR信息：{}", entity);
                }
                //推送票据中心
                if (StrUtil.equals(FileStatusConstants.INVOICE_CHECK_SUCCESS, dataImageFilesInfo.getFileStatus())) {
                    try {
                        breCheckRepeat(dataImageFilesInfo, identificationData.t, identificationData.k,false);
                        if (StrUtil.equals(FileStatusConstants.INVOICE_ALREADY_EXISTS, dataImageFilesInfo.getFileStatus())) {
                            return true;
                        }
                        ncImageServiceDTO.setSaveToken(response.getDataBip().get(0).getSaveToken());
                        ncImageServiceDTO.setInvoiceType("invoice");
                        ncImageServiceDTO.setFpLx(response.getDataBip().get(0).getInvoice().getFplx());
                        requestCheck.setCheckCode(response.getDataBip().get(0).getInvoice().getJym());
                        NccBipSaveLedgerRequest saveLedgerRequest = getNccBipSaveLedgerRequest(ncImageServiceDTO, requestCheck, format);
                        //请求票据中心
                        String saveLock = requestBillCenter(webClient, saveLedgerRequest, token);
                        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSON.parseObject(saveLock);
                        assert jsonObject != null;
                        if (StrUtil.isNotBlank(jsonObject.getString("code")) && !StrUtil.equals("200", jsonObject.getString("code"))) {
                            String msg = jsonObject.getString("message");
                            log.error("推送票据中心返回异常:{}", msg);
                            dataImageFilesInfo.setMessage(msg);
                            dataImageFilesInfo.setFileStatus(FileStatusConstants.OCR_SAVE_FAILED);
                        } else {
                            //台账推送成功标识
                            ledgerSuccess(identificationData, dataImageFilesInfo);
                        }
                    } catch (Exception e) {
                        log.error("推送票据中心异常: {}", ExceptionUtil.getExceptionMessage(e));
                        dataImageFilesInfo.setMessage(e.getMessage());
                        dataImageFilesInfo.setFileStatus(FileStatusConstants.OCR_SAVE_FAILED);
                    }
                }
            } catch (Exception e) {
                checkMark = true;
                dataImageFilesInfo.setMessage("查验失败!" + e.getMessage());
                log.error("查验异常: {}", ExceptionUtil.getExceptionMessage(e));
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
            }
        }
        return checkMark;
    }

    private BaseEntity updateInvoicesInfo(DataImageFilesInfo filesInfo, NccBipCheckResponse jsonObject, String ocdId) throws Exception {
        NccBipCheckResponse.DatasDTO datasDTO = jsonObject.getDataBip().get(0);
        NccBipCheckResponse.DatasDTO.InvoiceDTO invoice = datasDTO.getInvoice();
        String fplx = invoice.getFplx();
        filesInfo.setFileType(NccBipInvoiceTypeEnum.getSystemType(fplx));
        if (ArrayUtil.containsIgnoreCase(NccBipInvoiceTypeEnum.USED_CAR_SALES.getBipInvoiceType(), fplx)) {
            DataUsedCarSales usedCarSales = new DataUsedCarSales();
            usedCarSales.setId(ocdId);
            setHongChong(jsonObject, filesInfo, invoice, usedCarSales);
            nccBipBasicOcrInfo.setBasicOcrInfo(jsonObject, usedCarSales);
            usedCarSales.setFileId(filesInfo.getFileId());
            dataOcrService.ocrUpdateByBaseEntity(filesInfo.getFileType(), usedCarSales);
            return usedCarSales;
        } else if (!ArrayUtil.containsIgnoreCase(NccBipInvoiceTypeEnum.MOTOR_VEHICLE_SALE.getBipInvoiceType(), fplx)) {
            DataOcrInfo ocrInfo = new DataOcrInfo();
            ocrInfo.setId(ocdId);
            setHongChong(jsonObject, filesInfo, invoice, ocrInfo);
            nccBipBasicOcrInfo.setBasicOcrInfo(jsonObject, ocrInfo);
            ocrInfo.setDetails(nccBipChangeInvoiceDetails.changeInvoiceDetails(filesInfo.getFileId(), jsonObject, ocdId));
            ocrInfo.setFileId(filesInfo.getFileId());
            dataOcrService.ocrUpdateByBaseEntity(filesInfo.getFileType(), ocrInfo);
            return ocrInfo;
        } else if (ArrayUtil.containsIgnoreCase(NccBipInvoiceTypeEnum.MOTOR_VEHICLE_SALE.getBipInvoiceType(), fplx)) {
            DataMotorVehicleSale motorVehicleSale = new DataMotorVehicleSale();
            motorVehicleSale.setId(ocdId);
            setHongChong(jsonObject, filesInfo, invoice, motorVehicleSale);
            nccBipBasicOcrInfo.setBasicOcrInfo(jsonObject, motorVehicleSale);
            motorVehicleSale.setFileId(filesInfo.getFileId());
            dataOcrService.ocrUpdateByBaseEntity(filesInfo.getFileType(), motorVehicleSale);
            return motorVehicleSale;
        }
        return null;
    }

    private static void setHongChong(NccBipCheckResponse jsonObject, DataImageFilesInfo filesInfo, NccBipCheckResponse.DatasDTO.InvoiceDTO invoice, BaseEntity entity) {
        if (!"200".equals(jsonObject.getCode()) || BeanUtil.isEmpty(jsonObject.getDataBip())) {
            entity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            entity.setCheckResult(jsonObject.getMsg());
        } else if (!"200".equals(jsonObject.getDataBip().get(0).getCode()) || BeanUtil.isEmpty(jsonObject.getDataBip().get(0).getInvoice())) {
            entity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            entity.setCheckResult(jsonObject.getDataBip().get(0).getMsg());
        } else {
            entity.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            if (StrUtil.isNotBlank(invoice.getBred()) || StrUtil.isNotBlank(invoice.getZfbz())) {
                String checkResult = "";
                if (StrUtil.equals("Y", invoice.getBred())) {
                    checkResult = "发票红冲!";
                } else if (StrUtil.equals("Y", invoice.getZfbz())) {
                    checkResult = "发票已作废!";
                } else if (StrUtil.equals("I", invoice.getZfbz())) {
                    checkResult = "发票正在作废!";
                }
                if (StrUtil.isNotBlank(checkResult)) {
                    entity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                    filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
                    entity.setCheckResult(checkResult);
                    filesInfo.setMessage(checkResult);
                }
            }
        }
    }
    private void ledgerSuccess(IdentificationData identificationData, DataImageFilesInfo dataImageFilesInfo) throws Exception {
        HashMap<String, Object> corMap = new HashMap<>(16);
        corMap.put("type", identificationData.k);
        corMap.put("fileId", dataImageFilesInfo.getFileId());
        corMap.put("pushBusinessInfoFlag", "1");
        dataOcrService.ocrInsertOrUpdate(corMap);
    }
    private String requestBillCenter(WebClient webClient, NccBipSaveLedgerRequest saveLedgerRequest, Token token) {
        String jsonString = JSON.toJSONString(saveLedgerRequest);
        log.info("请求票据中心接口报文: {}", jsonString);
        String saveLock = webClient.post().uri(bipParamProperties.getSaveLedger() + "?access_token=" + token.getData().getAccessToken())
            .contentType(MediaType.APPLICATION_STREAM_JSON).bodyValue(jsonString).header("Content-Type", MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToMono(String.class).block();
        log.info("票据中心接口响应报文：{}", JSON.toJSONString(saveLock));
        return saveLock;
    }
    public NccBipSaveLedgerRequest getNccBipSaveLedgerRequest(NcImageServiceDTO ncImageServiceDTO, InvoiceCheckParamDTO requestCheck, String format) throws Exception {
        NccBipSaveLedgerRequest saveLedgerRequest = new NccBipSaveLedgerRequest();
        saveLedgerRequest.setUuId(IdUtil.simpleUUID());
        saveLedgerRequest.setBarCode(ncImageServiceDTO.getBarcode());
        saveLedgerRequest.setOpTime(DateUtil.now());
        saveLedgerRequest.setFactoryCode(ncImageServiceDTO.getFactoryCode());
        saveLedgerRequest.setBillId(ncImageServiceDTO.getBillId());
        NccBipSaveLedgerRequest.DataDTO saveData = new NccBipSaveLedgerRequest.DataDTO();
        saveData.setBillType(ncImageServiceDTO.getInvoiceType());
        DataImageFilesInfo dataImageFilesInfo = ncImageServiceDTO.getDataImageFilesInfo();
        saveData.setFilePath(getURL(dataImageFilesInfo));
        saveData.setSaveToken(ncImageServiceDTO.getSaveToken());
        saveData.setFpdm(requestCheck.getInvoiceCode());
        saveData.setFphm(requestCheck.getInvoiceNumber());
        saveData.setName(requestCheck.getName());
        saveLedgerRequest.setData(saveData);
        NccBipSaveLedgerRequest.DataDTO.SonDataDTO sonDataDTO = getSonDataDTO(ncImageServiceDTO, requestCheck, format);
        saveData.setData(sonDataDTO);
        return saveLedgerRequest;
    }

    private static String getURL(DataImageFilesInfo dataImageFilesInfo) {
        String systemIp = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_IP);
        String systemPort = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_VISIT_PORT);
        String prod = "/prod-api";
        return systemIp + ":" + systemPort + prod + dataImageFilesInfo.getUrl();
    }

    public NccBipSaveLedgerRequest.DataDTO.SonDataDTO getSonDataDTO(NcImageServiceDTO ncImageServiceDTO, InvoiceCheckParamDTO requestCheck, String format) throws Exception {
        NccBipSaveLedgerRequest.DataDTO.SonDataDTO sonDataDTO = new NccBipSaveLedgerRequest.DataDTO.SonDataDTO();
        sonDataDTO.setFpdm(requestCheck.getInvoiceCode());
        sonDataDTO.setFphm(requestCheck.getInvoiceNumber());
        sonDataDTO.setKprq(format);
        sonDataDTO.setHjje(requestCheck.getTotal());
        sonDataDTO.setJshj(requestCheck.getTotalLowercase());
        sonDataDTO.setJym(requestCheck.getCheckCode());
        sonDataDTO.setFplx(ncImageServiceDTO.getFpLx());
        sonDataDTO.setName(requestCheck.getName());
        sonDataDTO.setExit(requestCheck.getExit());
        sonDataDTO.setTicketNum(requestCheck.getTicketNum());
        return sonDataDTO;
    }
    /**
     * 重复校验
     *
     * @param dataImageFilesInfo 重复校验
     */
    private void breCheckRepeat(DataImageFilesInfo dataImageFilesInfo, BaseEntity invoiceMsg, String fileType,boolean flag) throws Exception {
        List<String> imageList = dataImageFilesInfoService.selectByBatchId(dataImageFilesInfo.getBatchId()).stream().map(DataImageFilesInfo::getFileId).collect(Collectors.toList());
        String className = InvoiceConstants.INVOICE_ClASS_TYPE.get(fileType);
        Class<?> invoiceClass = Class.forName(className);
        String invoiceCode = null,invoiceNumber = null, checkInvoice = null;
        try {
            Field invoiceCodeField = invoiceClass.getDeclaredField("invoiceCode");
            invoiceCodeField.setAccessible(true);
            invoiceCode = (String) invoiceCodeField.get(invoiceMsg);
        } catch (Exception e) {
            log.error("invoiceCode: {}", e.getMessage());
        }
        try {
            Field invoiceNumberField = invoiceClass.getDeclaredField("invoiceNumber");
            invoiceNumberField.setAccessible(true);
            invoiceNumber = (String) invoiceNumberField.get(invoiceMsg);
        } catch (Exception e) {
            log.error("invoiceNumber: {}", e.getMessage());
        }
        try {
            Field invoiceNumberField = invoiceClass.getDeclaredField("checkInvoice");
            invoiceNumberField.setAccessible(true);
            checkInvoice = (String) invoiceNumberField.get(invoiceMsg);
        } catch (Exception e) {
            log.error("checkInvoice: {}", e.getMessage());
        }
        QueryWrapper<BaseEntity> baseEntityLaqw = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(invoiceCode)) {
            baseEntityLaqw.eq("invoice_code", invoiceCode);
        }
        if (StrUtil.isNotEmpty(invoiceNumber)) {
            baseEntityLaqw.eq("invoice_number", invoiceNumber);
        }
        if (StrUtil.isNotEmpty(checkInvoice)) {
            baseEntityLaqw.eq("check_invoice", CheckConstants.INVOKE_CHECKED_CODE);
        }
        boolean invoiceIsStaging = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.INVOICE_IS_STAGING));
        if (invoiceIsStaging) {
            baseEntityLaqw.eq("is_staging", Constants.CONFIG_OFF_STATUS);
        }
        if (!Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.INVOICE_CHECK_REPEAT))) {
            baseEntityLaqw.in("file_id", imageList);
        }
        if (StrUtil.isBlank(invoiceCode) && StrUtil.isBlank(invoiceNumber)) {
            return;
        }
        List<BaseEntity> list = invoiceMsg.selectList(baseEntityLaqw);
        if (CollUtil.isNotEmpty(list) && list.size() > 1 || (invoiceIsStaging && StrUtil.equals(Constants.IS_STAGING, dataImageFilesInfo.getIsStaging()) && CollUtil.isNotEmpty(list))) {
            StringBuilder msg = new StringBuilder(InvoiceConstants.BIP_INVOICE_TYPE_ENUM.get(fileType)+"票已存在:(");
            for (BaseEntity repeatBaseEntity : list) {
                Field baseEntityFileId = null;
                try {
                    baseEntityFileId = invoiceClass.getDeclaredField("fileId");
                    baseEntityFileId.setAccessible(true);
                    String o = (String) baseEntityFileId.get(repeatBaseEntity);
                    String repeatBill = getRepeatBus(o);
                    boolean b = StrUtil.isNotBlank(repeatBill) && !dataImageFilesInfo.getFileId().equals(o);
                    if (b || flag) {
                        if(StrUtil.isNotBlank(repeatBill) &&!StrUtil.contains(msg, repeatBill)) {
                            msg.append(repeatBill).append(")");
                        }
                    }
                } catch (NoSuchFieldException e) {
                    log.error("重复校验异常:{}", ExceptionUtil.getExceptionMessage(e));
                }
            }
            dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_ALREADY_EXISTS);
            dataImageFilesInfo.setMessage(msg.toString());
        }
    }
    public String getRepeatBus(String fileId) {
        DataImageFilesInfo dataImageFilesInfo = this.dataImageFilesInfoService.selectById(fileId);
        if (ObjectUtil.isEmpty(dataImageFilesInfo)) {
            return null;
        }
        List<DataCmInfo> dataCmInfoList = cmInfoMapper.selectList(new LambdaQueryWrapper<DataCmInfo>().eq(DataCmInfo::getBatchId,dataImageFilesInfo.getBatchId()));
        if (CollectionUtil.isEmpty(dataCmInfoList)) {
            return null;
        }
        List<DataCurrentTask> currentTaskList = currentTaskMapper.selectList(new LambdaQueryWrapper<DataCurrentTask>().eq(DataCurrentTask::getBusinessSerialNo,dataCmInfoList.get(0).getBusinessSerialNo()));
        if (CollectionUtil.isEmpty(currentTaskList)) {
            return null;
        }
        return currentTaskList.get(0).getBillNum() + "影像任务ID:" + currentTaskList.get(0).getBusinessSerialNo();
    }
}
