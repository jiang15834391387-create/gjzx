package org.smartlink.web.ocr.service.yesfp;


import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.web.constant.OcrConstant;
import org.smartlink.web.ocr.service.yesfp.reponse.OpenApiAccessTokenData;
import org.smartlink.web.ocr.service.yesfp.reponse.OpenApiAccessTokenResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description: BIP税务云获取token类
 * @author: ChenJiangHong
 * @create: 2023-07-13 17:39
 **/
@Slf4j
public class TenantAuthProvider {


    private static String URL_TOKEN = "/open-auth/selfAppAuth/getAccessToken";

    public static OpenApiAccessTokenData getOpenApiAccessToken(String appKey, String appSecret, String tokenUrl) throws Exception {
        long timestamp = System.currentTimeMillis();

        OpenApiAccessTokenData openApiAccessTokenData = RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_REQUEST_TOKEN_INFO);

        if (openApiAccessTokenData == null || openApiAccessTokenData.expired()) {
            OpenApiAccessTokenData tokenData = callOpenApiAccessToken(appKey, appSecret, tokenUrl, timestamp);
            tokenData.setExpiredAt(timestamp);

            RedisUtils.setCacheObject(OcrConstant.CACHE_BIP_YESFP_REQUEST_TOKEN_INFO, tokenData);
            return tokenData;
        } else {
            return openApiAccessTokenData;
        }
    }

    private static OpenApiAccessTokenData callOpenApiAccessToken(String appKey, String appSecret, String tokenUrl,Long timestamp ) throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("appKey", appKey);
        params.put("timestamp", String.valueOf(timestamp));
        String signature = OpenApiRequestEncryptor.signature(params, appSecret);
        String url = tokenUrl+URL_TOKEN+"?signature="+signature+"&appKey="+appKey+"&timestamp="+timestamp;
        URI uri = new URI(url);
        ResponseEntity<OpenApiAccessTokenResult> openApiAccessTokenResult = new RestTemplate().getForEntity(uri, OpenApiAccessTokenResult.class);
        /*OpenApiAccessTokenResult openApiAccessTokenResult = WebClient.create().get().uri(url)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> {
                    log.info("BIP税务云获取TOKEN接口返回最原始报文：" + str);
                    return Mono.just(JSONObject.parseObject(str, OpenApiAccessTokenResult.class));
                })
                .map(ConversionException::checkOpenApiAccessTokenResult).block();*/
        return openApiAccessTokenResult.getBody().getData();
    }

}
