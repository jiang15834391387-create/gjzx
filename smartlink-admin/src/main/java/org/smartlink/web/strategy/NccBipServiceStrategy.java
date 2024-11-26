package org.smartlink.web.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.constant.HttpStatus;
import org.smartlink.web.constant.FileStatusConstants;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.constant.TaskStateConstants;
import org.smartlink.web.domain.DataCmInfo;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.dto.BipDeleteOcrRequest;
import org.smartlink.web.domain.dto.NcDeleteServiceDTO;
import org.smartlink.web.domain.dto.TaskSubmitDTO;
import org.smartlink.web.domain.dto.UpdateTaskDTO;
import org.smartlink.web.domain.dto.biprequest.BipSubBIPOcrRequest;
import org.smartlink.web.domain.dto.bipresponse.BipResponse;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.enumd.NccBipInvoiceAllTypeEnum;
import org.smartlink.web.enums.BIPTaskStateEnum;
import org.smartlink.web.properties.BipParamProperties;
import org.smartlink.web.service.nc.IDataCmInfoService;
import org.smartlink.web.service.nc.IDataCurrentTaskService;
import org.smartlink.web.service.nc.IDataImageFilesInfoService;
import org.smartlink.web.service.nc.IDataOcrService;
import org.smartlink.web.utils.ExceptionUtil;
import org.smartlink.web.utils.WebClientUtil;
import org.smartlink.web.utils.biptoken.BIpTokenUtils;
import org.smartlink.web.utils.biptoken.response.Token;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NccBipServiceStrategy implements INcStrategy{

    private BipParamProperties bipParamProperties = new BipParamProperties();
    private final IDataCurrentTaskService iDataCurrentTaskService;
    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataOcrService dataOcrService;
    private final IDataCmInfoService cmInfoService;

    public NccBipServiceStrategy(IDataCurrentTaskService iDataCurrentTaskService, IDataImageFilesInfoService dataImageFilesInfoService, IDataOcrService dataOcrService, IDataCmInfoService cmInfoService) {
        this.iDataCurrentTaskService = iDataCurrentTaskService;
        this.dataImageFilesInfoService = dataImageFilesInfoService;
        this.dataOcrService = dataOcrService;
        this.cmInfoService = cmInfoService;
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
}
