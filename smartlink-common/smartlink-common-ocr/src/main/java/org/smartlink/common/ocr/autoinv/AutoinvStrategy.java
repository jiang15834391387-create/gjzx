package org.smartlink.common.ocr.autoinv;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.ocr.abstractd.AbstractOcrStrategy;
import org.smartlink.common.ocr.abstractd.ConversionFactory;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.core.IdentificationFactory;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.config.AutoinvOcrProperties;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvDutyPaidProofConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvElectronicMedicalReceiptConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvFlightItineraryConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvInvoiceConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvInvoiceListConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvIntellectualPropertyReceiptConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvMachinePrintedInvoiceConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvManualInvoiceConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvMotorVehicleSaleConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvNonTaxRevenueConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvOnlineCarItineraryConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvPassengerTicketConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvQuotaInvoiceConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvRailwayTicketConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvRollTicketConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvScenicSpotTicketConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvTaxiTicketConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvTollRoadsConversion;
import org.smartlink.common.ocr.autoinv.conversion.AutoinvUsedCarSalesConversion;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.ocr.autoinv.response.AutoinvResult;
import org.smartlink.common.ocr.properties.OcrProperties;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * autoinv 识别策略类
 */
@Slf4j
@Component
public class AutoinvStrategy extends AbstractOcrStrategy implements IdentificationFactory<AutoinvIdentifyResult> {

    private static AutoinvOcrProperties autoinvOcrProperties = new AutoinvOcrProperties();

    @Override
    public void init(OcrProperties properties) {
        super.init(properties);
        try {
            autoinvOcrProperties = properties.getAutoinvOcrProperties();
            log.info("autoinv OCR初始化完成！初始化参数：{}", properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isInit = true;
    }

    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, String base64, String fileSuffix) throws OcrException {
        log.info("进入autoinv OCR识别");

        // 构建请求URL
        String url = autoinvOcrProperties.getUrl() + "?access_token=" + autoinvOcrProperties.getAccessToken();

        // 构建请求参数
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("image", base64);

        // 发送请求
        final List<AutoinvIdentifyResult> responseData = WebClient.create().post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(AutoinvResult.class)
            .defaultIfEmpty(new AutoinvResult())
            .flatMap(e -> {
                if (e == null || e.getResult() == null) {
                    return Mono.just(Collections.<AutoinvIdentifyResult>emptyList());
                }
                return Mono.justOrEmpty(e.getResult());
            })
            .onErrorResume(ex -> {
                log.error("autoinv OCR 识别请求失败: {}", ex.getMessage(), ex);
                return Mono.just(Collections.emptyList());
            })
            .block();

        if (CollUtil.isEmpty(responseData)) {
            log.info("autoinv OCR 识别结果为空");
            return null;
        }

        // 转换识别结果为系统统一格式
        List<IdentificationData> identificationDataList = Stream.of(ConversionFactory.getConversionFactory(dataImageFilesInfo, new ArrayList<>(responseData)))
            .filter(Objects::nonNull)
            .flatMap(changeIdentifyInfo -> {
                List<IdentificationData> dataList = changeIdentifyInfo.changeInfo(dataImageFilesInfo, new ArrayList<>(responseData));
                return dataList != null ? dataList.stream() : Stream.empty();
            })
            .collect(Collectors.toList());

        return identificationDataList;
    }

    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, List<AutoinvIdentifyResult> identifyResults) throws OcrException {
        if (CollUtil.isEmpty(identifyResults)) {
            return null;
        }

        // 遍历所有识别结果，根据发票类型返回对应的转换类
        for (AutoinvIdentifyResult identifyResult : identifyResults) {
            // 使用JSONObject的get方法动态获取type字段值
            String autoinvType = identifyResult.getStr("type");
            log.info("autoinv识别结果类型：{}", autoinvType);

            // 转换为系统统一发票类型
            String systemType = AutoinvInvoiceTypeMap.getSystemType(autoinvType);

            if (StringUtils.isEmpty(systemType)) {
                log.error("未找到对应的系统发票类型，autoinv类型：{}", autoinvType);
                continue;
            }

            // 根据系统发票类型返回对应的转换类
            switch (systemType) {
                //增值税、电子发票、区块链发票等
                case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
                case InvoiceConstants.GLORITY_TAX_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
                case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                    return AutoinvInvoiceConversion.getInstance();

                //增值税普通发票(卷票)
                case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                    return AutoinvRollTicketConversion.getInstance();

                //机动车销售统一发票
                case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                    return AutoinvMotorVehicleSaleConversion.getInstance();

                //二手车销售统一发票
                case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                    return AutoinvUsedCarSalesConversion.getInstance();

                //增值税发票销货清单
                case InvoiceConstants.DIGITAL_INVOICE_LIST:
                    return AutoinvInvoiceListConversion.getInstance();

                //航空运输电子客票行程单
                case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                    return AutoinvFlightItineraryConversion.getInstance();

                //火车票
                case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                    return AutoinvRailwayTicketConversion.getInstance();

                //出租车发票
                case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                    return AutoinvTaxiTicketConversion.getInstance();

                //客运票
                case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                    return AutoinvPassengerTicketConversion.getInstance();

                //过路费发票
                case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                    return AutoinvTollRoadsConversion.getInstance();

                //定额发票
                case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                    return AutoinvQuotaInvoiceConversion.getInstance();

                //机打发票
                case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                    return AutoinvMachinePrintedInvoiceConversion.getInstance();

                //通用手工发票
                case InvoiceConstants.GLORITY_MANUAL_INVOICE_CODE:
                    return AutoinvManualInvoiceConversion.getInstance();

                //政府非税收入
                case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                    return AutoinvNonTaxRevenueConversion.getInstance();

                //完税证明
                case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                    return AutoinvDutyPaidProofConversion.getInstance();

                //电子医疗票据
                case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                    return AutoinvElectronicMedicalReceiptConversion.getInstance();

                //网约车行程单
                case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                    return AutoinvOnlineCarItineraryConversion.getInstance();

//            //知识产权收费收据
//            case "10904":
//                return AutoinvIntellectualPropertyReceiptConversion.getInstance();
//
//            //景区门票
//            case "10905":
//                return AutoinvScenicSpotTicketConversion.getInstance();

//            //其它类型发票
//            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
//                return AutoinvOtherInvoiceConversion.getInstance();

                default: {
                    log.error("未实现的发票类型转换，系统类型：{}", systemType);
                    continue;
                }
            }
        }
        return null;
    }
}
