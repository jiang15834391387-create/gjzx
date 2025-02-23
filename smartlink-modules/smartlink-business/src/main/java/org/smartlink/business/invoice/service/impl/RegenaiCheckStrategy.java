package org.smartlink.business.invoice.service.impl;

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
import org.smartlink.business.enumd.CheckInvoiceStatusEnumd;
import org.smartlink.business.invoice.conversion.*;
import org.smartlink.business.invoice.service.abstractd.AbstractCheckStrategy;
import org.smartlink.common.check.constant.CheckConstant;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.enumd.ResponseCodeEnum;
import org.smartlink.common.check.properties.CheckProperties;
import org.smartlink.common.check.properties.RuiZhenCheckProperties;
import org.smartlink.common.check.utils.RuiZhenRequestUtil;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.DataImageFilesInfoMapper;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 睿真查验
 *
 */
@Slf4j
@Component
public class RegenaiCheckStrategy extends AbstractCheckStrategy {
    private static RuiZhenCheckProperties ruiZhenCheckProperties = new RuiZhenCheckProperties();
    private final DataImageFilesInfoMapper dataImageFilesInfoMapper;
    private final RegenaiBasicOcrInfo basicOcrInfo;
    private final RegenaiChangeInvoiceDetails changeInvoiceDetails;
    private final RegenaiChangeUsedCarSales changeUsedCarSales;
    private final RegenaiMotorVehicleSale motorVehicleSale;
    public RegenaiCheckStrategy(DataImageFilesInfoMapper dataImageFilesInfoMapper, RegenaiBasicOcrInfo basicOcrInfo, RegenaiChangeInvoiceDetails changeInvoiceDetails, RegenaiChangeUsedCarSales changeUsedCarSales, RegenaiMotorVehicleSale motorVehicleSale) {
        this.dataImageFilesInfoMapper = dataImageFilesInfoMapper;
        this.basicOcrInfo = basicOcrInfo;
        this.changeInvoiceDetails = changeInvoiceDetails;
        this.changeUsedCarSales = changeUsedCarSales;
        this.motorVehicleSale = motorVehicleSale;
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
    public BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) throws IOException {
        String bodyString = RuiZhenRequestUtil.getGlobalInfo(dto, ruiZhenCheckProperties);
        log.info("睿真查验接口请求参数：" + bodyString);
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(ruiZhenCheckProperties.getUrl());
        post.setHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(bodyString, "UTF-8");
        post.setEntity(entity);
        String response = null;
        BaseEntity baseEntity = null;

            HttpResponse result = client.execute(post);
            HttpEntity responseEntity = result.getEntity();
            if (responseEntity != null) {
                response = EntityUtils.toString(responseEntity, "UTF-8");
                if (StrUtil.isEmpty(response)) {
                    log.error("睿真查验失败，失败原因：查验服务返回缺少体：{}", response);
                    filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                    filesInfo.setMessage("查验失败，失败原因：查验服务返回缺少体response");
                    filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                    baseEntity=new BaseEntity();
                    baseEntity.setCheckResult(response);
                    baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                    //修改文件状态
                    this.dataImageFilesInfoMapper.updateById(filesInfo);
                    return baseEntity;
                }
            }

        log.info("睿真查验返回结果：{}", response);
        // 解析 JSON 响应
        JSONObject responseResult = JSONUtil.parseObj(response);
        // 检查顶层 result 是否等于 1 表示成功
        if (responseResult.getInt("result") != 1) {
            String message = responseResult.getStr("message");
            log.error("睿真查验失败：" + message);
            filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            filesInfo.setMessage("查验失败"+message);
            filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            baseEntity=new BaseEntity();
            baseEntity.setCheckResult(message);
            baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            //修改文件状态
            this.dataImageFilesInfoMapper.updateById(filesInfo);
            return baseEntity;
        }
        // 获取 response.data.identify_results
        JSONObject data = responseResult.getJSONObject("response").getJSONObject("data");
        JSONArray identifyResults = data.getJSONArray("identify_results");
        if (identifyResults == null || identifyResults.isEmpty()) {
            String message = "未找到查验结果";
            log.error("睿真查验失败：" + message);
            filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            filesInfo.setMessage(message);
            filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            baseEntity=new BaseEntity();
            baseEntity.setCheckResult(message);
            baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            //修改文件状态
            this.dataImageFilesInfoMapper.updateById(filesInfo);
            return baseEntity;
        }
        // 遍历 identify_results 验证 validation.code 和 details 是否符合要求
        for (Object results : identifyResults) {
            JSONObject identifyResult = (JSONObject) results;
            JSONObject validation = identifyResult.getJSONObject("validation");
            // 检查 validation.code 是否为 10000
            if (validation.getInt("code") != 10000) {
                setMessages(validation.getInt("code"), filesInfo);
                filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                baseEntity=new BaseEntity();
                baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                //修改文件状态
                this.dataImageFilesInfoMapper.updateById(filesInfo);
                return baseEntity;
            }

        }
        JSONObject details = null;
        // 遍历 identify_results 数组
        for (int i = 0; i < identifyResults.toArray().length; i++) {
            JSONObject results = identifyResults.getJSONObject(i);
            // 获取 details 对象
            details = results.getJSONObject("details");
            // 判断 details 是否为空
            if (details == null) {
                for (Object back : identifyResults) {
                    JSONObject identifyResult = (JSONObject) back;
                    JSONObject validation = identifyResult.getJSONObject("validation");
                    // 检查 validation.code 是否为 10000
                    if (validation.getInt("code") != 10000) {
                        setMessages(validation.getInt("code"), filesInfo);
                        filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                        filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                        baseEntity=new BaseEntity();
                        baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                        //修改文件状态
                        this.dataImageFilesInfoMapper.updateById(filesInfo);
                        return baseEntity;
                    }

                }
            }
        }
        // 如果所有校验都通过
        log.info("睿真发票接口查验成功");
        filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode());
        filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode());
        filesInfo.setMessage("查验成功");
        baseEntity=new BaseEntity();
        baseEntity.setCheckResult("查验成功");
        baseEntity.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
        // 更新OCR信息
        baseEntity = updateInvoicesInfo(filesInfo, details);
        log.info("睿真查验转换后的OCR信息：{}", baseEntity);
        //修改文件状态
        this.dataImageFilesInfoMapper.updateById(filesInfo);
        return baseEntity;

    }
    public BaseEntity updateInvoicesInfo(DataImageFilesInfo filesInfo, JSONObject jsonObject) {
        String invoiceType = filesInfo.getInvoice();
        if (StrUtil.equals(InvoiceConstants.GLORITY_USED_CAR_SALES_CODE, invoiceType)) {
            //二手车销售统一发票
             DataUsedCarSales dataUsedCarSales = new DataUsedCarSales();
             // 填充二手车销售统一发票信息
             this.changeUsedCarSales.changeUsedCarSales(jsonObject, dataUsedCarSales);
             dataUsedCarSales.setId(IdUtil.simpleUUID());
             dataUsedCarSales.setFileId(filesInfo.getFileId());
             dataUsedCarSales.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
             return dataUsedCarSales;
        }else if(StrUtil.equals(InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE, invoiceType)){
            //机动车销售统一发票
            DataMotorVehicleSale dataMotorVehicleSale = new DataMotorVehicleSale();
            // 填充机动车销售统一发票信息
            this.motorVehicleSale.changeMotorVehicleSale(jsonObject, dataMotorVehicleSale);
            dataMotorVehicleSale.setId(IdUtil.simpleUUID());
            dataMotorVehicleSale.setFileId(filesInfo.getFileId());
            dataMotorVehicleSale.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            return dataMotorVehicleSale;
        } else {
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
    private void setMessages(Integer code, DataImageFilesInfo filesInfo) {
        String message ="";
        switch (code){
            case 10001:
                message= ResponseCodeEnum.NO_SUCH_TICKET.getMessage();
                break;
            case 10002:
                message = ResponseCodeEnum.INCONSISTENT_INFO.getMessage();
                break;
            case 10003:
                message = ResponseCodeEnum.VERIFICATION_LIMIT_EXCEEDED.getMessage();
                break;
            case 10004:
                message = ResponseCodeEnum.UNSUPPORTED_TICKET_TYPE.getMessage();
                break;
            case 10005:
                message = ResponseCodeEnum.INVALID_PARAMETERS.getMessage();
                break;
            case 10006:
                message = ResponseCodeEnum.OTHER_ERRORS.getMessage();
                break;
        }
        filesInfo.setMessage("查验失败:"+message);
        log.error("查验失败：" + message);
    }
}
