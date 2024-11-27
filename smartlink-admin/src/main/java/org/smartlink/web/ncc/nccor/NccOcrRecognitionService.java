package org.smartlink.web.ncc.nccor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.web.exception.NCServiceException;
import org.smartlink.web.ncc.nccor.request.allinvoice.UploadAllInvoiceRequest;
import org.smartlink.web.ncc.nccor.request.allinvoice.UploadElectronicInvoiceRequest;
import org.smartlink.web.ncc.nccor.request.taxinvoice.UploadElectronicInvoiceNonStandardRequest;
import org.smartlink.web.ncc.nccor.request.taxinvoice.UploadTaxInvoiceRequest;
import org.smartlink.web.ncc.nccor.response.allinvoice.UploadAllInvoiceResponse;
import org.smartlink.web.ncc.nccor.response.allinvoice.UploadElectronicInvoiceResponse;
import org.smartlink.web.ncc.nccor.response.taxinvoice.UploadElectronicInvoiceNonStandardResponse;
import org.smartlink.web.ncc.nccor.response.taxinvoice.UploadTaxInvoiceResponse;
import org.smartlink.web.properties.NccParamProperties;
import org.smartlink.web.token.response.Token;
import org.smartlink.web.utils.DealRequest;
import org.smartlink.web.utils.SHA256Util;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @description: NCC业务系统获取增值税发票OCR接口
 * @author: L
 * @create:
 **/
@Component
@Slf4j
public class NccOcrRecognitionService {

    /**
     * NCC增值税发票接口URL
     */
    private static final String TAX_INVOICE_SERVICE_URL = "/nccloud/api/imag/invoice/ocr/ocr";

    /**
     * NCC全票种接口URL
     */
    private static final String ALL_INVOICE_SERVICE_URL = "/nccloud/api/imag/invoice/ocr/ocr_allinvoice";

    /**
     * 2207版本电子发票接口URL
     */
    private static final String ELECTRONIC_INVOICE_2207_SERVICE_URL = "/nccloud/api/imag/invoice/tax/uploadeinvoice";

    /**
     * 2111及以下版本电子发票接口URL
     */
    private static final String ELECTRONIC_INVOICE_2111_SERVICE_URL = "/nccloud/api/imag/invoice/tax/einvupload";

    /**
     * NCC识别增值税发票接口
     *
     * @param uploadTaxRequest 请求参数
     * @param token            token
     * @param paramProperties  系统参数
     * @return 返回
     * @throws Exception
     */
    public static UploadTaxInvoiceResponse getTaxInvoiceInfo(UploadTaxInvoiceRequest uploadTaxRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody = JSONObject.toJSONString(uploadTaxRequest);

        return Objects.requireNonNull(WebClient.create()
                .post()
                .uri(paramProperties.getBaseUrl() + TAX_INVOICE_SERVICE_URL)
                .header("access_token", token.getData().getAccess_token())
                .header("client_id", paramProperties.getClientId())
                .header("signature", SHA256Util.getSignatureData(paramProperties.getClientId() + rBody))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> Mono.just(JSONObject.parseObject(str, UploadTaxInvoiceResponse.class)))
                .map(NCServiceException::getNccOcrResult)
                .block());
    }

    /**
     * NCC全票种识别接口
     *
     * @param uploadAllInvoiceRequest 请求参数
     * @param token                   token
     * @param paramProperties         系统参数
     * @return 返回
     */
    public static UploadAllInvoiceResponse getAllInvoiceInfo(UploadAllInvoiceRequest uploadAllInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody = JSONObject.toJSONString(uploadAllInvoiceRequest);
        return Objects.requireNonNull(WebClient.create()
                .post()
                .uri(paramProperties.getBaseUrl() + ALL_INVOICE_SERVICE_URL)
                .header("access_token", token.getData().getAccess_token())
                .header("client_id", paramProperties.getClientId())
                .header("signature", SHA256Util.getSignatureData(paramProperties.getClientId() + rBody))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> Mono.just(JSONObject.parseObject(str, UploadAllInvoiceResponse.class)))
                .map(NCServiceException::getNccOcrResult)
                .block());
    }

    /**
     * NCC2207电子发票保存台账接口
     *
     * @param uploadElectronicInvoiceRequest 请求参数
     * @param token                          token
     * @param paramProperties                系统参数
     * @return 返回
     */
    public static UploadElectronicInvoiceResponse getElectronicInvoiceInfo(UploadElectronicInvoiceRequest uploadElectronicInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody = JSONObject.toJSONString(uploadElectronicInvoiceRequest);
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(builder ->
                        builder.codecs(codecs -> codecs.defaultCodecs().
                                maxInMemorySize(20 * 1024 * 1024))).build();
        return Objects.requireNonNull(webClient
                .post()
                .uri(paramProperties.getBaseUrl() + ELECTRONIC_INVOICE_2207_SERVICE_URL)
                .header("access_token", token.getData().getAccess_token())
                .header("client_id", paramProperties.getClientId())
                .header("signature", SHA256Util.getSignatureData(paramProperties.getClientId() + rBody))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> Mono.just(
                        JSONObject.parseObject(str, UploadElectronicInvoiceResponse.class)))
                .map(NCServiceException::checkElectronicSaveResult)
                .block());
    }

    /**
     * NCC2111及以下版本电子发票保存台账接口
     *
     * @param uploadElectronicInvoiceNonStandardRequest 请求参数
     * @param token                          token
     * @param paramProperties                系统参数
     * @return 返回
     */
    public static UploadElectronicInvoiceNonStandardResponse getNonStandardElectronicInvoiceInfo(UploadElectronicInvoiceNonStandardRequest uploadElectronicInvoiceNonStandardRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody = JSONObject.toJSONString(uploadElectronicInvoiceNonStandardRequest);
        return Objects.requireNonNull(WebClient.create()
                .post()
                .uri(paramProperties.getBaseUrl() + ELECTRONIC_INVOICE_2111_SERVICE_URL)
                .header("access_token", token.getData().getAccess_token())
                .header("client_id", paramProperties.getClientId())
                .header("signature", SHA256Util.getSignatureData(paramProperties.getClientId() + rBody))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap((str) -> {
                    log.info("业务系统非标准电子发票上传接口返回最原始报文："+str);
                    return Mono.just(
                            JSONObject.parseObject(str, UploadElectronicInvoiceNonStandardResponse.class));
                }).map(NCServiceException::checkElectronicSaveResult)
                .block());
    }


}
