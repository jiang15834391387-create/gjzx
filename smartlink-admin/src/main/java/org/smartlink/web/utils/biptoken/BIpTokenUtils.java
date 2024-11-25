package org.smartlink.web.utils.biptoken;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.smartlink.web.context.TenantContextHolder;
import org.smartlink.web.domain.SysTenant;
import org.smartlink.web.service.nc.ISysTenantService;
import org.smartlink.web.utils.biptoken.request.BipTokenRequest;
import org.smartlink.web.utils.biptoken.response.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 获取BIP接口token
 * @author: L
 * @create:
 **/
@Slf4j
@Component
public class BIpTokenUtils {

    @Autowired
    ISysTenantService iSystemTenantService;

    private static BIpTokenUtils bIpTokenUtils;

    @PostConstruct
    public void init(){
        bIpTokenUtils = this;
        iSystemTenantService = this.iSystemTenantService;
    }

    /**
     * 获取BIP token方法
     *
     * @param bipTokenRequest 请求对象
     * @return 返回结果对象
     */
    public static Token getToken(BipTokenRequest bipTokenRequest) throws Exception {
        Map<String, String> params = new HashMap<>();
        // 除签名外的其他参数
        params.put("appKey", bipTokenRequest.getAppKey());
        String s = String.valueOf(System.currentTimeMillis());
        params.put("timestamp", s);
        // 计算签名
        String signature = HmacSHA256Utils.sign(params, bipTokenRequest.getAppSecret());
        params.put("signature", signature);

        HttpGet get = new HttpGet(bipTokenRequest.getOpenApiUrl() + "?appKey=" + bipTokenRequest.getAppKey() + "&timestamp=" + s + "&signature=" + signature);
        CloseableHttpClient httpClient=getHttpClient();
        String responseString = httpClient.execute(get, response -> EntityUtils.toString(response.getEntity()));
        get.releaseConnection();
        return JSONObject.parseObject(responseString, Token.class);
    }
    public static Token getToken(String bipTokenRequestJson) throws Exception {
        SysTenant systemTenant = BIpTokenUtils.bIpTokenUtils.iSystemTenantService.queryById(TenantContextHolder.getTenantId());
        if (ObjectUtil.isEmpty(systemTenant)){
            throw new Exception("该租户未开通影像");
        }
        BipTokenRequest bipTokenRequest=JSONObject.parseObject(bipTokenRequestJson,BipTokenRequest.class);
        bipTokenRequest.setAppKey(systemTenant.getTenantId());
        bipTokenRequest.setAppSecret(systemTenant.getAppSecret());
        Map<String, String> params = new HashMap<>();
        // 除签名外的其他参数
        params.put("appKey", bipTokenRequest.getAppKey());
        String s = String.valueOf(System.currentTimeMillis());
        params.put("timestamp", s);
        // 计算签名
        String signature = HmacSHA256Utils.sign(params, bipTokenRequest.getAppSecret());
        params.put("signature", signature);
        String url=bipTokenRequest.getOpenApiUrl() + "?appKey=" + bipTokenRequest.getAppKey() + "&timestamp=" + s + "&signature=" + signature;
        log.info("租户信息:{} , 开放平台Token请求报文:{}",JSONObject.toJSONString(bipTokenRequest), url);
        HttpGet get = new HttpGet(url);
        CloseableHttpClient httpClient=getHttpClient();
        String responseString = httpClient.execute(get, response -> EntityUtils.toString(response.getEntity()));
        get.releaseConnection();
        log.info("开放平台Token请求路径:{}开放平台Token响应报文:{}",url, responseString);
        Token token = JSONObject.parseObject(responseString, Token.class);
        if (StrUtil.equalsIgnoreCase("10018",token.getCode())){
            throw new Exception(token.getMessage());
        }
        String accessToken = token.getData().getAccessToken();
        if (StrUtil.isNotBlank(accessToken)) {
            String encode = URLEncoder.encode(accessToken);
            log.info("编码Token:{}",encode);
            token.getData().setAccessToken(encode);
        }
        return token;
    }

    public static void main(String[] args) {
        String s = "{\"code\":\"00000\",\"message\":\"成功！\",\"data\":{\"expire\":4147,\"access_token\":\"1f7ce6d4166d40cd81eb1f1e4d5c5a94\"}}";
        Token token = JSONObject.parseObject(s, Token.class);
        System.out.println(token);
    }

    public static Token getTokenByTenementNo(String bipTokenRequestJson,String tenementNo) throws Exception {
        SysTenant systemTenant = BIpTokenUtils.bIpTokenUtils.iSystemTenantService.queryById(tenementNo);
        if (ObjectUtil.isEmpty(systemTenant)){
            throw new Exception("该租户未开通影像");
        }
        BipTokenRequest bipTokenRequest=JSONObject.parseObject(bipTokenRequestJson,BipTokenRequest.class);
        bipTokenRequest.setAppKey(systemTenant.getTenantId());
        bipTokenRequest.setAppSecret(systemTenant.getAppSecret());
        Map<String, String> params = new HashMap<>();
        // 除签名外的其他参数
        params.put("appKey", bipTokenRequest.getAppKey());
        String s = String.valueOf(System.currentTimeMillis());
        params.put("timestamp", s);
        // 计算签名
        String signature = HmacSHA256Utils.sign(params, bipTokenRequest.getAppSecret());
        params.put("signature", signature);
        String url=bipTokenRequest.getOpenApiUrl() + "?appKey=" + bipTokenRequest.getAppKey() + "&timestamp=" + s + "&signature=" + signature;
        log.info("租户信息:{} , 开放平台Token请求报文:{}",JSONObject.toJSONString(bipTokenRequest), url);
        HttpGet get = new HttpGet(url);
        CloseableHttpClient httpClient=getHttpClient();
        String responseString = httpClient.execute(get, response -> EntityUtils.toString(response.getEntity()));
        get.releaseConnection();
        log.info("开放平台Token请求路径:{} , 开放平台Token响应报文:{}",url, responseString);
        Token token = JSONObject.parseObject(responseString, Token.class);
        if (StrUtil.equalsIgnoreCase("10018",token.getCode())){
            throw new Exception(token.getMessage());
        }
        return token;
    }

//    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
//        Map<String, String> params = new HashMap<>();
//        // 除签名外的其他参数
//        params.put("appKey", "4f06a1dbbe4a43ea80c0b28a2f31fb3c");
//        String s = String.valueOf(System.currentTimeMillis());
//        params.put("timestamp", s);
//        // 计算签名
//        String signature = HmacSHA256Utils.sign(params, "92af342246f5444e9840196b5cfb7c5c");
//        params.put("signature", signature);
//
//        HttpGet get = new HttpGet("https://bip-pre.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken" + "?appKey=" + "4f06a1dbbe4a43ea80c0b28a2f31fb3c" + "&timestamp=" + s + "&signature=" + signature);
//        CloseableHttpClient httpClient=getHttpClient();
//        String responseString = httpClient.execute(get, response -> EntityUtils.toString(response.getEntity()));
//        get.releaseConnection();
//        System.out.println(JSONObject.parseObject(responseString, Token.class));
//    }

//    public static void main(String[] args) {
//
//        Map<String, String> params = new TreeMap<>();
//        // 除签名外的其他参数
//        params.put("appKey", "");
//        String timestamp = "1649383055738";
////         String timestamp = String.valueOf(System.currentTimeMillis());
//        params.put("timestamp", timestamp);
//        // 计算签名
//        String signature = SignHelper.sign(params, appSecret);
//        params.put("signature", signature);
//        for (String s : params.keySet()) {
//            System.out.println(s + ":" + params.get(s));
//        }
//    }

    private static PoolingHttpClientConnectionManager cm = null;

    private static CloseableHttpClient httpClient;

    /**
     * 记录开放平台请求结果
     */
    public static class Response {
        /**
         * 该请求的 http 状态码
         * 200 为正常的返回结果
         */
        private int status;

        /**
         * 请求返回消息
         * 当 status == 200 时会返回 response body 中的字符串
         * 当 status !== 200 时会返回具体的错误信息
         */
        private String result;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    static{
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(500);
        cm.setDefaultMaxPerRoute(50);

        RequestConfig globalConfig = RequestConfig.custom()
                // 连接池获取连接超时
                .setConnectionRequestTimeout(10000)
                // 连接建立超时
                .setConnectTimeout(10000)
                // 等待响应超时
                .setSocketTimeout(50000)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();

        httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(globalConfig).build();
    }

    private static CloseableHttpClient getHttpClient(){
        return httpClient;
    }

}
