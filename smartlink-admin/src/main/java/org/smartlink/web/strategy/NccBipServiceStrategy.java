package org.smartlink.web.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.dto.BipDeleteOcrRequest;
import org.smartlink.web.domain.dto.NcDeleteServiceDTO;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.enumd.NccBipInvoiceAllTypeEnum;
import org.smartlink.web.properties.BipParamProperties;
import org.smartlink.web.service.nc.IDataCurrentTaskService;
import org.smartlink.web.service.nc.IDataImageFilesInfoService;
import org.smartlink.web.service.nc.IDataOcrService;
import org.smartlink.web.utils.ExceptionUtil;
import org.smartlink.web.utils.biptoken.BIpTokenUtils;
import org.smartlink.web.utils.biptoken.response.Token;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class NccBipServiceStrategy implements INcStrategy{

    private BipParamProperties bipParamProperties = new BipParamProperties();
    private final IDataCurrentTaskService iDataCurrentTaskService;
    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataOcrService dataOcrService;

    public NccBipServiceStrategy(IDataCurrentTaskService iDataCurrentTaskService, IDataImageFilesInfoService dataImageFilesInfoService, IDataOcrService dataOcrService) {
        this.iDataCurrentTaskService = iDataCurrentTaskService;
        this.dataImageFilesInfoService = dataImageFilesInfoService;
        this.dataOcrService = dataOcrService;
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
}
