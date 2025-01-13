package org.smartlink.common.check.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.smartlink.common.check.constant.CheckConstant;
import org.smartlink.common.check.constant.FileStatusConstants;
import org.smartlink.common.check.constant.InvoiceConstants;
import org.smartlink.common.check.conversion.RuiZhenBasicOcrInfo;
import org.smartlink.common.check.conversion.RuiZhenChangeInvoiceDetails;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.invoice.DataImageFilesInfo;
import org.smartlink.common.check.invoice.DataOcrInfo;
import org.smartlink.common.check.mapper.DataImageFilesInfoMapper;
import org.smartlink.common.check.properties.CheckProperties;
import org.smartlink.common.check.properties.RuiZhenCheckProperties;
import org.smartlink.common.check.service.IDataImageFilesInfoService;
import org.smartlink.common.check.service.abstractd.AbstractCheckStrategy;
import org.smartlink.common.check.utils.RuiZhenRequestUtil;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 睿真查验
 *
 */
@Slf4j
@Component
public class RuiZhenCheckStrategy extends  AbstractCheckStrategy {
    private static RuiZhenCheckProperties ruiZhenCheckProperties = new RuiZhenCheckProperties();
    private final DataImageFilesInfoMapper dataImageFilesInfoMapper;
    private final RuiZhenBasicOcrInfo basicOcrInfo;
    private final RuiZhenChangeInvoiceDetails changeInvoiceDetails;

    public RuiZhenCheckStrategy(IDataImageFilesInfoService imageFilesInfoService, DataImageFilesInfoMapper dataImageFilesInfoMapper, RuiZhenBasicOcrInfo basicOcrInfo, RuiZhenChangeInvoiceDetails changeInvoiceDetails) {
        this.dataImageFilesInfoMapper = dataImageFilesInfoMapper;
        this.basicOcrInfo = basicOcrInfo;
        this.changeInvoiceDetails = changeInvoiceDetails;
    }

    @Override
    public void init(CheckProperties properties) {
        super.init(properties);
        // 初始化配置
        final String detailInfo = this.properties.getDetailInfo();
        final JSONObject jsonObject = JSONUtil.parseObj(detailInfo);

        // JSON转对象
        ruiZhenCheckProperties = JSONUtil.toBean(jsonObject, RuiZhenCheckProperties.class);
        log.info("睿真查验初始化完成！初始化参数：{}", properties);

        isInit = true;
    }

    @Override
    public BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) {
        String bodyString = RuiZhenRequestUtil.getGlobalInfo(dto, ruiZhenCheckProperties);
        log.info("睿真查验接口请求参数：" + bodyString);
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(ruiZhenCheckProperties.getUrl());
        post.setHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(bodyString, "UTF-8");
        post.setEntity(entity);
        String response = null;
        BaseEntity baseEntity = new BaseEntity();
        try {
            HttpResponse result = client.execute(post);
            HttpEntity responseEntity = result.getEntity();
            if (responseEntity != null) {
                response = EntityUtils.toString(responseEntity, "UTF-8");
                if (StrUtil.isEmpty(response)) {
                    log.error("睿真查验失败，失败原因：查验服务返回缺少体：{}", response);
                    filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
                    filesInfo.setMessage(response);
                    baseEntity.setCheckResult(response);
                    baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                    return baseEntity;
                }
            }
        } catch (IOException e) {
            log.error("执行睿真查验HTTP请求时发生异常原因：{}", e.getMessage());
            filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
            filesInfo.setMessage("查验HTTP请求失败:" + e.getMessage());
            baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            baseEntity.setCheckResult("查验HTTP请求失败:" + e.getMessage());
            return baseEntity;
        }
        log.info("睿真查验返回结果：{}", response);
        // 解析 JSON 响应
        JSONObject responseResult = JSONUtil.parseObj(response);
        // 检查顶层 code 是否等于 1 表示成功
        if (responseResult.getInt("result") != 1) {
            String message = responseResult.getStr("message");
            log.error("睿真查验失败：" + message);
            filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
            filesInfo.setMessage(message);
            baseEntity.setCheckResult(message);
            baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            return baseEntity;
        }
        // 获取 response.data.identify_results
        JSONObject data = responseResult.getJSONObject("response").getJSONObject("data");
        JSONArray identifyResults = data.getJSONArray("identify_results");
        if (identifyResults == null || identifyResults.isEmpty()) {
            String message = "未找到查验结果";
            log.error("睿真查验失败：" + message);
            filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
            filesInfo.setMessage(message);
            baseEntity.setCheckResult(message);
            baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            return baseEntity;
        }
        // 遍历 identify_results 验证 validation.code 和 items 是否符合要求
        for (Object result : identifyResults) {
            JSONObject identifyResult = (JSONObject) result;
            JSONObject validation = identifyResult.getJSONObject("validation");
            // 检查 validation.code 是否为 10000
            if (validation.getInt("code") != 10000) {
                String message = validation.getStr("message");
                log.error("睿真查验失败：" + message);
                filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
                filesInfo.setMessage(message);
                baseEntity.setCheckResult(message);
                baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                return baseEntity;
            }

        }
        JSONObject details = null;
        // 遍历 identify_results 数组
        for (int i = 0; i < identifyResults.toArray().length; i++) {
            JSONObject result = identifyResults.getJSONObject(i);
            // 获取 details 对象
            details = result.getJSONObject("details");
            // 判断 details 是否为空
            if (details == null) {
                String message = "校验失败：明细details 为空";
                log.error(message);
                filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
                filesInfo.setMessage(message);
                baseEntity.setCheckResult(message);
                baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                return baseEntity;
            }
        }
        // 如果所有校验都通过
        log.info("睿真发票接口查验成功");
        filesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_SUCCESS);
        baseEntity.setCheckResult("查验成功");
        baseEntity.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
        // 更新OCR信息
        baseEntity = updateInvoicesInfo(filesInfo, details);
        log.info("睿真查验转换后的OCR信息：{}", baseEntity);
        filesInfo.setMessage("睿真发票接口查验成功！");
        //修改文件状态
        this.dataImageFilesInfoMapper.updateById(filesInfo);
        return baseEntity;

    }

    public BaseEntity updateInvoicesInfo(DataImageFilesInfo filesInfo, JSONObject jsonObject) {
        String invoiceType = filesInfo.getInvoice();
        if (StrUtil.equals(InvoiceConstants.GLORITY_USED_CAR_SALES_CODE, invoiceType)) {
            //二手车销售统一发票(TODO)
            return null;
        }else if(StrUtil.equals(InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE, invoiceType)){
            //机动车销售统一发票(TODO)
            return null;
        }else {
            //增值税
            DataOcrInfo dataOcrInfo = new DataOcrInfo();
            //填充OCR基本信息
            this.basicOcrInfo.setBasicOcrInfo(jsonObject, dataOcrInfo);
            // 填充OCR详情信息 changeInvoiceDetails
            dataOcrInfo.setDetails(this.changeInvoiceDetails.changeInvoiceDetails(filesInfo.getFileId(), jsonObject));
            dataOcrInfo.setId(IdUtil.simpleUUID());
            dataOcrInfo.setFileId(filesInfo.getFileId());
            dataOcrInfo.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            return dataOcrInfo;
        }
    }
}
