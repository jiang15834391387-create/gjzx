package org.smartlink.web.ncc.syncocr;

import com.alibaba.fastjson.JSONObject;
import org.smartlink.web.exception.NCServiceException;
import org.smartlink.web.ncc.syncocr.request.allinvoice.SyncAllInvoiceRequest;
import org.smartlink.web.ncc.syncocr.request.taxinvoice.SyncTaxInvoiceRequest;
import org.smartlink.web.ncc.syncocr.response.allinvoice.SyncAllInvoiceResponse;
import org.smartlink.web.ncc.syncocr.response.taxinvoice.SyncTaxInvoiceResponse;
import org.smartlink.web.properties.NccParamProperties;
import org.smartlink.web.token.response.Token;
import org.smartlink.web.utils.DealRequest;
import org.smartlink.web.utils.SHA256Util;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @description: NCC同步OCR信息接口
 * @author: L
 * @create:
 **/
public class NccSyncTaxInvoiceService {

    private static String TAX_INVOICE_SYNC_SERVICE_URL = "/nccloud/api/imag/invoice/ocr/syncrecord";

    private static String ALL_INVOICE_SYNC_SERVICE_URL = "/nccloud/api/imag/invoice/ocr/syncrecord_allinvoice";

    /**
     * 推送增值税发票信息至业务系统台账
     * @param token
     * @param paramProperties
     * @return
     */
    public static SyncTaxInvoiceResponse syncOcrResult(SyncTaxInvoiceRequest syncTaxInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody= JSONObject.toJSONString(syncTaxInvoiceRequest);
        return   Objects.requireNonNull(WebClient.create()
                .post()
                .uri(paramProperties.getBaseUrl()+TAX_INVOICE_SYNC_SERVICE_URL)
                .header("access_token",token.getData().getAccess_token())
                .header("client_id",paramProperties.getClientId())
                .header("signature", SHA256Util.getSignatureData(paramProperties.getClientId() + rBody))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> Mono.just(JSONObject.parseObject(str, SyncTaxInvoiceResponse.class)))
                .map(NCServiceException::checkSyncTaxInvoiceResult)
                .block());
    }

    /**
     * 推送全票种发票信息至业务系统台账
     * @param syncAllInvoiceRequest
     * @param token
     * @param paramProperties
     * @return
     * @throws Exception
     */
    public static SyncAllInvoiceResponse syncAllInvoiceOcrResult (SyncAllInvoiceRequest syncAllInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody= JSONObject.toJSONString(syncAllInvoiceRequest);
        return   Objects.requireNonNull(WebClient.create()
                .post()
                .uri(paramProperties.getBaseUrl()+ALL_INVOICE_SYNC_SERVICE_URL)
                .header("access_token",token.getData().getAccess_token())
                .header("client_id",paramProperties.getClientId())
                .header("signature", SHA256Util.getSignatureData(paramProperties.getClientId() + rBody))
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> Mono.just(JSONObject.parseObject(str, SyncAllInvoiceResponse.class)))
                .map(NCServiceException::checkSyncAllInvoiceResult)
                .block());
    }

}
