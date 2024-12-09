package org.smartlink.server.nc.ncc.nccverify;

import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.exception.NCServiceException;
import org.smartlink.server.nc.ncc.nccverify.request.VerifyTaxInvoiceRequest;
import org.smartlink.server.nc.ncc.nccverify.response.VerifyTaxInvoiceResponse;
import org.smartlink.server.nc.properties.NccParamProperties;
import org.smartlink.server.nc.token.response.Token;
import org.smartlink.server.nc.utils.DealRequest;
import org.smartlink.server.nc.utils.SHA256Util;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @description: NCC查验接口
 * @author: L
 * @create:
 **/
public class NccTaxVerifyService {

    private static String serviceUrl = "/nccloud/api/imag/invoice/verify/verify";

    /**
     * 增值税发票查验接口
     * @param verifyTaxInvoiceRequest 请求参数
     * @param token token参数
     * @param paramProperties 系统参数
     * @return 返回
     * @throws Exception
     */
    public static VerifyTaxInvoiceResponse verifyTaxInvoiceInfo(VerifyTaxInvoiceRequest verifyTaxInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody= JSONObject.toJSONString(verifyTaxInvoiceRequest);
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(builder ->
                        builder.codecs(codecs -> codecs.defaultCodecs().
                                maxInMemorySize(20 * 1024 * 1024))).build();
        return   Objects.requireNonNull(webClient
                .post()
                .uri(paramProperties.getBaseUrl()+serviceUrl)
                .header("access_token",token.getData().getAccess_token())
                .header("client_id",paramProperties.getClientId())
                .header("signature", SHA256Util.getSignatureData(paramProperties.getClientId() + rBody))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap((str) ->{
                    return Mono.just(JSONObject.parseObject(str, VerifyTaxInvoiceResponse.class));
                })
                .map(NCServiceException::checkVerifyResult)
                .block());
    }

}
