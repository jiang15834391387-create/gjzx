package org.smartlink.web.ocr.service.yesfp;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.web.constant.Constants;
import org.smartlink.web.constant.OcrConstant;
import org.smartlink.web.ocr.exception.OcrException;
import org.smartlink.web.ocr.service.yesfp.config.YesfpProperties;
import org.smartlink.web.ocr.service.yesfp.reponse.OpenApiAccessTokenData;
import org.smartlink.web.ocr.service.yesfp.request.YesfpNonTaxInvoiceRequest;
import org.smartlink.web.ocr.service.yesfp.request.YesfpOcrSaveResult;
import org.smartlink.web.ocr.service.yesfp.request.YesfpTaxInvoiceRequest;
import org.smartlink.web.ocr.service.yesfp.utils.SignUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @description: 税务云保存台账类
 * @author: L
 * @create:
 **/
@Slf4j
public class YesfpOcrSaveService {

    public static void nonTaxInvoiceToStand(YesfpNonTaxInvoiceRequest yesfpNonTaxInvoiceRequest, YesfpProperties yesfpProperties) throws Exception {
        String request = JsonUtils.toJsonString(yesfpNonTaxInvoiceRequest);
        String sign;
        String saveUrl;
        if(StrUtil.equals(yesfpProperties.getInterFaceType(), Constants.BIP_YESFP_INTERFACE_TYPE)){
            OpenApiAccessTokenData openApiAccessToken;
            try {
                openApiAccessToken = TenantAuthProvider.getOpenApiAccessToken(yesfpProperties.getAppKey(), yesfpProperties.getAppSecret(), RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY));
                sign = openApiAccessToken.getAccess_token();
                saveUrl = RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY) + yesfpProperties.getSaveUrl() + "?access_token="+sign;
            } catch (Exception e) {
                throw new Exception(e);
            }
        }else{
            try {
                sign = SignUtils.sign(request,yesfpProperties.getPrivateKeyType(), yesfpProperties.getP12Path(), yesfpProperties.getPassword());
                saveUrl = yesfpProperties.getBaseUrl()+yesfpProperties.getSaveUrl()+"?appid=" + yesfpProperties.getAppId();
            } catch (Exception e) {
                throw new OcrException("签名验证失败！");
            }
        }
        log.info("税务云非增值税发票保存报销台账报文："+ JSONObject.toJSONString(yesfpNonTaxInvoiceRequest));
        WebClient.create().post().uri(saveUrl)
                .header("sign", sign)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(yesfpNonTaxInvoiceRequest)
                .retrieve()
                .bodyToMono(YesfpOcrSaveResult.class)
                .map(ConversionException::yesfpOcrSave)
                .block();
    }


    public static void taxInvoiceToStand(YesfpTaxInvoiceRequest yesfpTaxInvoiceRequest, YesfpProperties yesfpProperties) throws Exception {
        String request = JsonUtils.toJsonString(yesfpTaxInvoiceRequest);
        String sign ;
        String submitUrl;
        if(StrUtil.equals(yesfpProperties.getInterFaceType(), Constants.BIP_YESFP_INTERFACE_TYPE)){
            OpenApiAccessTokenData openApiAccessToken;
            try {
                openApiAccessToken = TenantAuthProvider.getOpenApiAccessToken(yesfpProperties.getAppKey(), yesfpProperties.getAppSecret(), RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_TOEKEN_KEY));
                sign = openApiAccessToken.getAccess_token();
                submitUrl = RedisUtils.getCacheObject(OcrConstant.CACHE_BIP_YESFP_GATEWAY_KEY) + yesfpProperties.getSubmitUrl() + "?access_token="+sign;
            } catch (Exception e) {
                throw new Exception(e);
            }
        }else{
            try {
                sign = SignUtils.sign(request,yesfpProperties.getPrivateKeyType(), yesfpProperties.getP12Path(), yesfpProperties.getPassword());
                submitUrl = yesfpProperties.getBaseUrl()+yesfpProperties.getSubmitUrl() + "?appid=" + yesfpProperties.getAppId();
            } catch (Exception e) {
                throw new OcrException("签名验证失败！");
            }
        }
        // yesfpNonTaxInvoiceRequest.getInvoices().get(0).setSaveToken(saveToken);
        log.info("税务云增值税发票保存报销台账请求报文："+ JSONObject.toJSONString(yesfpTaxInvoiceRequest));
        WebClient.create().post().uri(submitUrl)
                .header("sign", sign)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(yesfpTaxInvoiceRequest)
                .retrieve()
                .bodyToMono(YesfpOcrSaveResult.class)
                .map(CheckInvokeException::yesfpOcrSave)
                .block();
    }

}
