package org.smartlink.business.invoice.autoinvcheck;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.enumd.CheckInvoiceStatusEnumd;
import org.smartlink.business.invoice.autoinvcheck.conversion.AutoinvChangeInvoiceDetails;
import org.smartlink.business.invoice.autoinvcheck.conversion.AutoinvChangeUsedCarSales;
import org.smartlink.business.invoice.autoinvcheck.conversion.AutoinvMotorVehicleSale;
import org.smartlink.business.invoice.autoinvcheck.conversion.AutoinvRailwayTicketConversion;
import org.smartlink.business.invoice.autoinvcheck.conversion.AutoinvFlightItineraryConversion;
import org.smartlink.business.invoice.check.abstractd.AbstractCheckStrategy;
import org.smartlink.business.invoice.autoinvcheck.conversion.AutoinvBasicOcrInfo;
import org.smartlink.common.check.constant.CheckConstant;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.properties.AutoinvCheckProperties;
import org.smartlink.common.check.properties.CheckProperties;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.DataImageFilesInfoMapper;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

/**
 * autoinv查验策略
 *
 */
@Slf4j
@Component
public class AutoinvCheckStrategy extends AbstractCheckStrategy {
    private static AutoinvCheckProperties autoinvCheckProperties = new AutoinvCheckProperties();

    @Autowired
    private DataImageFilesInfoMapper dataImageFilesInfoMapper;

    @Autowired
    private AutoinvBasicOcrInfo basicOcrInfo;

    @Autowired
    private AutoinvChangeInvoiceDetails changeInvoiceDetails;

    @Autowired
    private AutoinvChangeUsedCarSales changeUsedCarSales;

    @Autowired
    private AutoinvMotorVehicleSale motorVehicleSale;

    @Autowired
    private AutoinvRailwayTicketConversion railwayTicketConversion;

    @Autowired
    private AutoinvFlightItineraryConversion flightItineraryConversion;

    @Override
    public void init(CheckProperties properties) {
        super.init(properties);
        // 初始化配置
        final String detailInfo = this.properties.getDetailInfo();
        final JSONObject jsonObject = JSONUtil.parseObj(detailInfo);

        // JSON转对象
        autoinvCheckProperties = JSONUtil.toBean(jsonObject, AutoinvCheckProperties.class);
        log.info("autoinv查验初始化完成！初始化参数：{}", properties);

        isInit = true;
    }

    @Override
    public BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) throws IOException {
        // 构建请求参数
        JSONObject requestParams = buildRequestParams(dto);
        log.info("autoinv查验接口请求参数：{}", requestParams);

        String response = null;
        BaseEntity baseEntity = null;

        try {
            // 发送请求
            response = WebClient.create().post()
                    .uri(autoinvCheckProperties.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestParams.toString()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (StrUtil.isEmpty(response)) {
                log.error("autoinv查验失败，失败原因：查验服务返回缺少体：{}", response);
                filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                filesInfo.setMessage("查验失败，失败原因：查验服务返回缺少体response");
                filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                baseEntity = new BaseEntity();
                baseEntity.setCheckResult(response);
                baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                //修改文件状态
                this.dataImageFilesInfoMapper.updateById(filesInfo);
                return baseEntity;
            }

        } catch (Exception e) {
            log.error("autoinv查验请求异常：{}", e.getMessage(), e);
            filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            filesInfo.setMessage("查验失败，失败原因：" + e.getMessage());
            filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            baseEntity = new BaseEntity();
            baseEntity.setCheckResult(e.getMessage());
            baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            //修改文件状态
            this.dataImageFilesInfoMapper.updateById(filesInfo);
            return baseEntity;
        }

        log.info("autoinv查验返回结果：{}", response);
        // 解析 JSON 响应
        JSONObject responseResult = JSONUtil.parseObj(response);

        // 获取响应状态信息
        boolean success = responseResult.getBool("Success", false);
        String statusCode = responseResult.getStr("StatusCode");
        String message = responseResult.getStr("Message");

        log.info("autoinv查验状态码：{}，消息：{}", statusCode, message);

        // 根据状态码处理查验结果（参考接口文档中的响应状态码说明）
        switch (statusCode) {
            case "1001":
                // 认可的发票：查询到对应发票数据，票据信息与参数信息完全一致
                break;
            case "1004":
                // 发票信息不一致
            case "1002":
                // 查验次数超
            case "1003":
                // 查无此票
            case "1005":
                // 其它错误
            case "1006":
                // 参数无效
            case "1007":
                // 所查验发票地区服务暂不可用
            case "1020":
                // 没有查验权限
                log.error("autoinv查验失败：{}，状态码：{}", message, statusCode);
                filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                filesInfo.setMessage("查验失败：" + message + "（状态码：" + statusCode + "）");
                filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                baseEntity = new BaseEntity();
                baseEntity.setCheckResult(message);
                baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                //修改文件状态
                this.dataImageFilesInfoMapper.updateById(filesInfo);
                return baseEntity;
            default:
                // 未知状态码
                log.error("autoinv查验返回未知状态码：{}，消息：{}", statusCode, message);
                filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                filesInfo.setMessage("查验失败：未知状态码（" + statusCode + "）" + message);
                filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
                baseEntity = new BaseEntity();
                baseEntity.setCheckResult(message);
                baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
                //修改文件状态
                this.dataImageFilesInfoMapper.updateById(filesInfo);
                return baseEntity;
        }

        // 获取 Data 部分（只有1001状态会走到这里）
        JSONObject data = responseResult.getJSONObject("Data");
        if (data == null) {
            // 对于1001状态，Data应该不为空，为空则视为异常
            log.error("autoinv查验成功但返回数据为空，状态码：{}", statusCode);
            filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            filesInfo.setMessage("查验失败：返回数据为空（状态码：" + statusCode + "）");
            filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_FAILED_CODE.getCode());
            baseEntity = new BaseEntity();
            baseEntity.setCheckResult("返回数据为空");
            baseEntity.setCheckInvoice(CheckConstant.ERROE_CHECK);
            //修改文件状态
            this.dataImageFilesInfoMapper.updateById(filesInfo);
            return baseEntity;
        }

        // 如果所有校验都通过
        log.info("autoinv发票接口查验成功");
        filesInfo.setFileStatus(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode());
        filesInfo.setCheckStatus(CheckInvoiceStatusEnumd.VERIFICATION_SUCCESSFUL_CODE.getCode());
        filesInfo.setMessage("查验成功");
        baseEntity = new BaseEntity();
        baseEntity.setCheckResult("查验成功");
        baseEntity.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
        // 更新OCR信息
        baseEntity = updateInvoicesInfo(filesInfo, data);
        log.info("autoinv查验转换后的OCR信息：{}", baseEntity);
        //修改文件状态
        this.dataImageFilesInfoMapper.updateById(filesInfo);
        return baseEntity;
    }

    /**
     * 构建请求参数
     */
    private JSONObject buildRequestParams(InvoiceCheckParamDTO dto) {
        JSONObject params = new JSONObject();

        // Code
        if (StrUtil.isNotBlank(dto.getCode())) {
            params.put("Code", dto.getCode());
        }

        // No
        if (StrUtil.isNotBlank(dto.getNumber())) {
            params.put("No", dto.getNumber());
        }

        // VCode
        if (StrUtil.isNotBlank(dto.getCheck_code())) {
            params.put("VCode", dto.getCheck_code());
        }

        // Amount（优先 pretax_amount，其次 total）
        String amount = dto.getPretax_amount();
        if (StrUtil.isNotBlank(amount)) {
            try {
                params.put("Amount", Double.parseDouble(amount));
            } catch (NumberFormatException e) {
                log.error("金额格式转换失败：{}", amount);
            }
        }

        // Date（只保留数字）
        if (StrUtil.isNotBlank(dto.getDate())) {
            String dateStr = dto.getDate().replaceAll("\\D", "");
            if (StrUtil.isNotBlank(dateStr)) {
                params.put("Date", dateStr);
            }
        }

        // PermitCode（必传）
        params.put("PermitCode", autoinvCheckProperties.getPermitCode());

        return params;
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
        } else if (StrUtil.equals(InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE, invoiceType)) {
            //机动车销售统一发票
            DataMotorVehicleSale dataMotorVehicleSale = new DataMotorVehicleSale();
            // 填充机动车销售统一发票信息
            this.motorVehicleSale.changeMotorVehicleSale(jsonObject, dataMotorVehicleSale);
            dataMotorVehicleSale.setId(IdUtil.simpleUUID());
            dataMotorVehicleSale.setFileId(filesInfo.getFileId());
            dataMotorVehicleSale.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            return dataMotorVehicleSale;
        } else if (StrUtil.equals(InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE, invoiceType)) {
            //铁路电子客票
            DataRailwayTicket dataRailwayTicket = new DataRailwayTicket();
            // 填充铁路电子客票信息
            this.railwayTicketConversion.changeRailwayTicket(jsonObject, dataRailwayTicket);
            dataRailwayTicket.setId(IdUtil.simpleUUID());
            dataRailwayTicket.setFileId(filesInfo.getFileId());
            dataRailwayTicket.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            return dataRailwayTicket;
        } else if (StrUtil.equals(InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE, invoiceType)) {
            //航空运输电子客票行程单
            DataFlightItinerary dataFlightItinerary = new DataFlightItinerary();
            // 填充航空运输电子客票行程单信息
            this.flightItineraryConversion.changeFlightItinerary(jsonObject, dataFlightItinerary);
            dataFlightItinerary.setId(IdUtil.simpleUUID());
            dataFlightItinerary.setFileId(filesInfo.getFileId());
            dataFlightItinerary.setCheckInvoice(CheckConstant.SUCCESS_CHECK);
            return dataFlightItinerary;
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
}
