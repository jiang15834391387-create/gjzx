package org.smartlink.web.ncc.testapi;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.web.exception.NCServiceException;
import org.smartlink.web.ncc.testapi.response.TestOpenApiResponse;
import org.smartlink.web.ncc.testapi.resqust.TestOpenApiRequest;
import org.smartlink.web.token.response.Token;
import org.smartlink.web.utils.SHA256Util;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @description: 测试与NCC接口联通性
 * @author: L
 * @create:
 **/
@Slf4j
public class TestApiService {

    private static String requestBody = "{\"data\":{\"factoryCode\":\"shy\",\"methodName\":\"testNcc\"}}";

    /**
     * 测试账号访问OPEN API的权限
     */
    private final static String TEST_OPEN_API_URL = "/nccloud/api/imag/demimag/imagconfig/getnccserverinfo";


    /**
     * 测试与NCC open api接口联通性
     * @param token
     * @param baseUrl
     * @param clientId
     * @return
     */
    public static TestOpenApiResponse testOpenApi(Token token, String baseUrl, String clientId, TestOpenApiRequest testOpenApiRequest) throws Exception{
        String url = baseUrl + TEST_OPEN_API_URL;
        String bodyValue = JSONObject.toJSONString(testOpenApiRequest);
        log.info("调用NCC OPEN API接口请求报文："+bodyValue);
        return WebClient.create()
                .post()
                .uri(url)
                .header("access_token",token.getData().getAccess_token())
                .header("client_id",clientId)
                .header("signature", SHA256Util.getSignatureData(clientId+bodyValue))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(bodyValue)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> {
                    log.info("调用NCC OPEN API接口返回最原始报文："+str);
                    return Mono.just(JSONObject.parseObject(str, TestOpenApiResponse.class));
                })
                .map(NCServiceException::getNccOpenApiResult)
                .block();
    }
}
