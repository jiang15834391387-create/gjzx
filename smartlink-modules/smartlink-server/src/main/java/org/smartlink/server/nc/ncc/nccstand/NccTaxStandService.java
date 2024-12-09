package org.smartlink.server.nc.ncc.nccstand;

import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.exception.NCServiceException;
import org.smartlink.server.nc.ncc.nccstand.request.allinvoice.StandAllInvoiceRequest;
import org.smartlink.server.nc.ncc.nccstand.request.taxinvoice.StandTaxInvoiceRequest;
import org.smartlink.server.nc.ncc.nccstand.response.allinvoice.StandAllInvoiceResponse;
import org.smartlink.server.nc.ncc.nccstand.response.taxinvoice.StandTaxInvoiceResponse;
import org.smartlink.server.nc.properties.NccParamProperties;
import org.smartlink.server.nc.token.response.Token;
import org.smartlink.server.nc.utils.DealRequest;
import org.smartlink.server.nc.utils.SHA256Util;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @description: NCC增值税发票保存台账接口
 * @author: L
 * @create:
 **/
public class NccTaxStandService {

    /**
     * 增值税发票接口推送台账URL
     */
    private static final String SAVE_TAX_INVOICE_SAVE_SERVICE_URL = "/nccloud/api/imag/invoice/tax/upload";
    /**
     * 全票种接口推送台账URL
     */
    private static final String SAVE_ALL_INVOICE_SAVE_SERVICE_URL = "/nccloud/api/imag/invoice/tax/upload_allinvoice";



    /**
     * 推送增值税发票信息至业务系统台账
     * @param token
     * @param paramProperties
     * @return
     */
    public static StandTaxInvoiceResponse sendTaxInvoiceInfo(StandTaxInvoiceRequest standTaxInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody= JSONObject.toJSONString(standTaxInvoiceRequest);
        String signatureData = SHA256Util.getSignatureData(paramProperties.getClientId() + rBody);
        return Objects.requireNonNull(WebClient.create()
                .post()
                .uri(paramProperties.getBaseUrl()+SAVE_TAX_INVOICE_SAVE_SERVICE_URL)
                .header("access_token",token.getData().getAccess_token())
                .header("client_id",paramProperties.getClientId())
                .header("signature", signatureData)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> Mono.just(JSONObject.parseObject(str,StandTaxInvoiceResponse.class)))
                .map(NCServiceException::checkSaveTaxInvoiceResult)
                .block());
    }


    /**
     * 推送全票种发票信息至业务系统台账
     * @param token
     * @param paramProperties
     * @return
     */
    public static StandAllInvoiceResponse sendAllInvoiceInfo(StandAllInvoiceRequest standAllInvoiceRequest, Token token, NccParamProperties paramProperties) throws Exception {
        String rBody= JSONObject.toJSONString(standAllInvoiceRequest);
        String signatureData = SHA256Util.getSignatureData(paramProperties.getClientId() + rBody);
        return Objects.requireNonNull(WebClient.create()
                .post()
                .uri(paramProperties.getBaseUrl()+SAVE_ALL_INVOICE_SAVE_SERVICE_URL)
                .header("access_token",token.getData().getAccess_token())
                .header("client_id",paramProperties.getClientId())
                .header("signature", signatureData)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(DealRequest.dealRequestBody(rBody, token.getData().getSecurity_key()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> Mono.just(JSONObject.parseObject(str,StandAllInvoiceResponse.class)))
                .map(NCServiceException::checkSaveAllInvoiceResult)
                .block());
    }

}
