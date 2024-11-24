package org.smartlink.web.token;


import org.smartlink.web.exception.NCServiceException;
import org.smartlink.web.token.request.NccTokenRequest;
import org.smartlink.web.token.response.Token;
import org.smartlink.web.utils.EncryptionUtil;
import org.smartlink.web.utils.SHA256Util;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

/**
 * @description: 获取NCC接口token
 * @author: L
 * @create:
 **/
public class NccToken {

    /**
     * 请求地址
     */
    private final static String TOKEN_URL = "/nccloud/opm/accesstoken";

    /**
     * 获取NCC token方法
     * @param nccTokenRequest 请求对象
     * @return 返回结果对象
     */
    public static Token getToken(NccTokenRequest nccTokenRequest) throws Exception {
//        WebClient webClient = WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
//                .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> {
//                    configurer.defaultCodecs()
//                            .maxInMemorySize(16 * 1024 * 1024);
//                }).build()).build();
        BodyInserters.FormInserter<String> with = BodyInserters.fromFormData("grant_type", "password")
            .with("client_id", nccTokenRequest.getClientId())
            .with("client_secret", EncryptionUtil.pubEncrypt(nccTokenRequest.getClientSecret()))
            .with("username", nccTokenRequest.getNccUserName())
            .with("password", EncryptionUtil.pubEncrypt(nccTokenRequest.getNccPassword()))
            .with("biz_center", nccTokenRequest.getBizCenter())
            .with("signature", SHA256Util.getSignatureData(nccTokenRequest.getClientId() + nccTokenRequest.getClientSecret() + nccTokenRequest.getNccUserName() + nccTokenRequest.getNccPassword()));

//        System.out.println("请求参数1111111："+with.);

        return Objects.requireNonNull(WebClient.create()
                .post()
                .uri(nccTokenRequest.getBaseUrl()+TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id",nccTokenRequest.getClientId())
                        .with("client_secret", EncryptionUtil.pubEncrypt(nccTokenRequest.getClientSecret()))
                        .with("username",nccTokenRequest.getNccUserName())
                        .with("password",EncryptionUtil.pubEncrypt(nccTokenRequest.getNccPassword()))
                        .with("biz_center", nccTokenRequest.getBizCenter())
                        .with("signature", SHA256Util.getSignatureData(nccTokenRequest.getClientId() + nccTokenRequest.getClientSecret() + nccTokenRequest.getNccUserName()+ nccTokenRequest.getNccPassword())))
                .retrieve().bodyToMono(Token.class)
                .map(NCServiceException::getNCCTokenResult)
                .block();
    }
}
