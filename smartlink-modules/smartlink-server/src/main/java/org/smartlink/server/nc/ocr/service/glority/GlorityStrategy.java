package org.smartlink.server.nc.ocr.service.glority;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.properties.OcrProperties;
import org.smartlink.server.nc.ocr.service.abstractd.AbstractOcrStrategy;
import org.smartlink.server.nc.ocr.service.abstractd.ConversionFactory;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.glority.config.GlorityOcrProperties;
import org.smartlink.server.nc.ocr.service.glority.response.GlorityResult;
import org.smartlink.server.nc.ocr.service.glority.response.IdentifyResults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 票小秘识别类
 *
 * @author L
 * @date
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlorityStrategy extends AbstractOcrStrategy {

    private static GlorityOcrProperties glorityOcrProperties = new GlorityOcrProperties();

    @Override
    public void init(OcrProperties properties) {
        super.init(properties);
        // 初始化配置
        final String detailInfo = this.properties.getDetailInfo();
        final cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(detailInfo);
        // JSON转对象
        glorityOcrProperties = JSONUtil.toBean(jsonObject, GlorityOcrProperties.class);
        log.info("票小米Ocr初始化完成！初始化参数：{}", properties);
        isInit = true;
    }


    @Override
    public List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, byte[] bytes) throws OcrException {
        log.info("进入票小米OCR识别");
        // 计数
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 使用md5加密
        final String token = SecureUtil.md5(glorityOcrProperties.getAppKey() + "+" + timestamp + "+" + glorityOcrProperties.getAppSecret());
        //
        final BodyInserters.FormInserter<Object> with = BodyInserters.fromMultipartData("image_data", Base64Encoder.encode(bytes))
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
                .map(ConversionException::checkGlorityResult)
                .flatMap(e -> Mono.just(e.getResponse().getData().getIdentify_results())).block();
        if (CollUtil.isEmpty(responseData)) {
            log.info("票小秘返回 NULL ");
            Objects.requireNonNull(responseData);
        }
        log.info("票小秘识别结果:" + JSONObject.toJSONString(responseData));
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

//    @Override
//    public List<ItemContractListBean> getIdentificationContractData(DataContract dataContract, byte[] bytes) {
//        return null;
//    }
//
//    @Override
//    public CcintDocumentResult getIdentificationDocument(byte[] byteOld) {
//        return null;
//    }
}
