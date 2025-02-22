package org.smartlink.common.ocr.glority;

import cn.hutool.core.collection.CollUtil;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.file.Constants;
import org.smartlink.common.core.utils.file.ParamConstants;
import org.smartlink.common.ocr.abstractd.AbstractOcrStrategy;
import org.smartlink.common.ocr.abstractd.ConversionFactory;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.core.IdentificationFactory;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.config.GlorityOcrProperties;
import org.smartlink.common.ocr.glority.conversion.InvoiceConversion;
import org.smartlink.common.ocr.glority.conversion.*;

import org.smartlink.common.ocr.glority.response.GlorityResult;
import org.smartlink.common.ocr.glority.response.IdentifyResults;
import org.smartlink.common.ocr.properties.OcrProperties;
//import org.smartlink.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 票小秘识别类
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlorityStrategy extends AbstractOcrStrategy implements IdentificationFactory<IdentifyResults> {

    private static GlorityOcrProperties glorityOcrProperties = new GlorityOcrProperties();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void init(OcrProperties properties) {
        super.init(properties);
        // 初始化配置
        // 使用 Jackson 将 JSON 字符串直接转换为对象
        try {
            glorityOcrProperties = properties.getDetailInfo();
            log.info("票小秘Ocr初始化完成！初始化参数：{}", properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isInit = true;
    }

    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, String base64, String fileSuffix) throws OcrException {
        log.info("进入票小秘OCR识别");
        // 计数
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 使用md5加密
        final String token = SecureUtil.md5(glorityOcrProperties.getAppKey() + "+" + timestamp + "+" + glorityOcrProperties.getAppSecret());
        //
        final BodyInserters.FormInserter<Object> with = BodyInserters.fromMultipartData("image_data", base64)
                .with("app_key", glorityOcrProperties.getAppKey())
                .with("timestamp", timestamp)
                .with("token", token);
                //是否切图
                boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, ParamConstants.SYS_OCR_CUT));
                if (isCrop || fileSuffix.equals("pdf")) {
                    with.with("extract_level", 1);
                }
        // 发送请求
        final List<IdentifyResults> responseData = WebClient.create().post()
                .uri(glorityOcrProperties.getUrl())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(with)
                .retrieve()
                .bodyToMono(GlorityResult.class)
//                .map(OcrConversionException::checkGlorityResult)
//                .flatMap(e -> Mono.just(e.getResponse().getData().getIdentify_results())).block();
            .defaultIfEmpty(new GlorityResult()) // 避免 bodyToMono() 为空
            .flatMap(e -> {
                if (e == null || e.getResponse() == null || e.getResponse().getData() == null) {
                    return Mono.just(Collections.<IdentifyResults>emptyList()); // 显式指定泛型
                }
                return Mono.justOrEmpty(e.getResponse().getData().getIdentify_results());
            })
            .onErrorResume(ex -> {
                log.error("OCR 识别请求失败: {}", ex.getMessage(), ex); //记录异常详细信息
                return Mono.just(null); //捕获异常并返回 null
            })
            .block();
        if (CollUtil.isEmpty(responseData)) {
            log.info("OCR 识别为 NULL ");
            return null;
        }
//        log.info("票小秘识别结果:" + JSONObject.toJSONString(responseData));
        List<IdentificationData> identificationDataList = Stream.of(ConversionFactory.getConversionFactory(dataImageFilesInfo, new ArrayList<>(responseData)))
            .filter(Objects::nonNull) // 确保 ChangeIdentifyInfo 不为 null
            .flatMap(changeIdentifyInfo -> {
                // 调用 changeInfo 方法
                List<IdentificationData> dataList = changeIdentifyInfo.changeInfo(dataImageFilesInfo, new ArrayList<>(responseData));
                return dataList != null ? dataList.stream() : Stream.empty(); // 处理可能为 null 的返回值
            })
            .collect(Collectors.toList());

        return identificationDataList;
    }

    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResults) throws OcrException {
        for(IdentifyResults identifyResult:identifyResults){
            switch (identifyResult.getType()) {
                //增值税、机打
                case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
                case InvoiceConstants.GLORITY_TAX_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
                case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_LIST:
                case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
                    return InvoiceConversion.getInstance();
                //机动车
                case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                    return MotorVehicleSaleConversion.getInstance();
                //航空运输电子客票行程单
                case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                    return FlightItineraryConversion.getInstance();
                //二手车
                case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                    return UsedCarSalesConversion.getInstance();
                //船票
                case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                    return SteamerTicketConversion.getInstance();
                //医疗票
                case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                    return DataMedicalTreatmentConversion.getInstance();
                //医疗票明细票
                case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
                    return DataMedicalTreatmentDetailsConversion.getInstance();
                //定额发票
                case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                    return QuotaInvoiceConversion.getInstance();
                //出租车发票
                case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                    return TaxiTicketsConversion.getInstance();
                //火车发票
                case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                    return RailwayTicketConversion.getInstance();
                //客运车发票
                case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                    return PassengerTicketConversion.getInstance();
                //过路费发票
                case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                    return TollRoadsConversion.getInstance();
                //小票
                case InvoiceConstants.GLORITY_RECEIPT_CODE:
                    return ReceiptConversion.getInstance();
                //出行发票
                case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                    return DidiItineraryConversion.getInstance();
                //完税证明发票
                case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                    return DutyPaidProofConversion.getInstance();
                //完非税收入类发票
                case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                    return DataNonTaxConversion.getInstance();
                //海关进口货物报关单发票
                case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                    return DataCustomsImxportGoodsConversion.getInstance();
                //海关出口货物报关单发票
                case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                    return DataCustomsExportGoodsConversion.getInstance();
                //海关专用缴款书发票
                case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                    return DataCustomsSpecialPaymentConversion.getInstance();
                //货物运输电子收款凭证发票
                case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                    return DataElectronicTransportationGoodsConversion.getInstance();
                default: {
                    return null;
                }
            }
        }
        return null;
    }
}
