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
import org.smartlink.common.ocr.glority.response.GlorityResult;
import org.smartlink.common.ocr.glority.response.IdentifyResults;
import org.smartlink.common.ocr.properties.OcrProperties;
//import org.smartlink.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
            log.info("票小米Ocr初始化完成！初始化参数：{}", properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isInit = true;
    }
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void printBeans() {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
    }

    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, String base64) throws OcrException {
        log.info("进入票小米OCR识别");
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
                boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_CUT));
                if (true) { // 根据条件判断是否需要添加 extract_level
                    with.with("extract_level", 1); // 动态值或固定值
                }
        // 发送请求
        final List<IdentifyResults> responseData = WebClient.create().post()
                .uri(glorityOcrProperties.getUrl())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(with)
                .retrieve()
                .bodyToMono(GlorityResult.class)
//                .map(OcrConversionException::checkGlorityResult)
                .flatMap(e -> Mono.just(e.getResponse().getData().getIdentify_results())).block();
        if (CollUtil.isEmpty(responseData)) {
            log.info("票小秘返回 NULL ");
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
//        for (IdentificationData identificationData : collect) {
//            if(!InvoiceConstants.IMAGE_OTHERS.equals(identificationData.k)){
//                return collect;
//            }
//        }
//        List<IdentificationData> collect2 = new ArrayList<>(1);
//        collect2.add( identificationDataList);
        return identificationDataList;
    }

    @Override
    public ChangeIdentifyInfo conversionInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResults) throws OcrException {
        for(IdentifyResults identifyResult:identifyResults){
            switch (identifyResult.getType()) {
                //增值税、机打
                case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
                case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
                case InvoiceConstants.GLORITY_TAX_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_QUKUAILIAN_CODE:
                case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
                case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
                case InvoiceConstants.DIGITAL_INVOICE_LIST:
                case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
                    return InvoiceConversion.getInstance();
//            case GLORITY_QUOTA_INVOICE_CODE:
//                return QuotaInvoiceConversion.getInstance();
//            case GLORITY_AIRCRAFT_INVOICE_CODE:
//                return AircraftInvoiceConversion.getInstance();
//            case GLORITY_TAXI_TICKETS_CODE:
//                return TaxiTicketsConversion.getInstance();
//            case GLORITY_RAILWAY_TICKET_CODE:
//                return RailwayTicketConversion.getInstance();
//            case GLORITY_RECEIPT_CODE:
//                return ReceiptConversion.getInstance();
//            case GLORITY_MOTOR_VEHICLE_SALE_CODE:
//                return MotorVehicleSaleConversion.getInstance();
//            case GLORITY_PASSENGER_TICKET_CODE:
//                return PassengerTicketConversion.getInstance();
//            case GLORITY_DIDI_ITINERARY_CODE:
//                return DidiItineraryConversion.getInstance();
//            case GLORITY_USED_CAR_SALES_CODE:
//                return UsedCarSalesConversion.getInstance();
//            case GLORITY_FLIGHT_ITINERARY_CODE:
//                return FlightItineraryConversion.getInstance();
//            case GLORITY_DUTY_PAID_PROOF_CODE:
//                return DutyPaidProofConversion.getInstance();
//            case GLORITY_STEAMER_TICKET_CODE:
//                return SteamerTicketConversion.getInstance();
//            case GLORITY_TOLL_ROADS_CODE:
//                return TollRoadsConversion.getInstance();
                default: {
                    return null;
                }
            }
        }
        return null;
    }
}
