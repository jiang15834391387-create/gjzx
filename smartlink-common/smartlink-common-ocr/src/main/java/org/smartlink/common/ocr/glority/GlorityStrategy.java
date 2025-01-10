package org.smartlink.common.ocr.glority;

import cn.hutool.core.collection.CollUtil;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.ocr.abstractd.AbstractOcrStrategy;
import org.smartlink.common.ocr.abstractd.ConversionFactory;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.config.GlorityOcrProperties;
import org.smartlink.common.ocr.glority.response.GlorityResult;
import org.smartlink.common.ocr.glority.response.IdentifyResults;
import org.smartlink.common.ocr.properties.OcrProperties;
//import org.smartlink.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
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
import java.util.stream.Collectors;

/**
 * 票小秘识别类
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlorityStrategy extends AbstractOcrStrategy {

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
        List<IdentificationData> collect = responseData.stream().map(e -> ConversionFactory.getConversionFactory(dataImageFilesInfo, e).changeInfo(dataImageFilesInfo, e)).collect(Collectors.toList());
        for (IdentificationData identificationData : collect) {
            if(!InvoiceConstants.IMAGE_OTHERS.equals(identificationData.k)){
                return collect;
            }
        }
        List<IdentificationData> collect2 = new ArrayList<>(1);
        collect2.add( collect.get(0));
        return collect2;
    }

}
