package org.smartlink.common.oss.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName: RunJianUtil
 * Package: com.example.strategydemo.util
 * Description:
 *
 * @Author 张志强
 * @Create 2024/9/24 16:11
 * @Version 1.0
 */
@Slf4j
@Component
public class RunJianUtil {

    @Value("${runjian.appKey}")
    private String appKey;
    @Value("${runjian.appSecret}")
    private String appSecret;
    @Value("${runjian.baseUrl}")
    private String BaseUrl;
    @Value("${runjian.accessTokenUrl}")
    private String AccessTokenUrl;
    @Value("${runjian.uid}")
    private String uid;

    /**
     * 获取润建token值
     */
    public String getAccessToken() throws IOException {
        //构建请求，获取access_token
        String url = BaseUrl + AccessTokenUrl;
        // 创建HttpClient实例
        HttpClient httpClient = HttpClientBuilder.create().build();
        // 创建POST请求
        HttpPost request = new HttpPost(url);
        // 添加请求头
        request.addHeader("Content-Type", "application/json");

        // 添加请求体（JSON数据）
        Map<String, Object> map = new HashMap<>();
        map.put("appKey", appKey);
        map.put("appSecret", appSecret);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(map);

        request.setEntity(new StringEntity(jsonBody));
        HttpResponse response = httpClient.execute(request);
        //解析响应为map集合
        Map<String, Object> mapResponse = ResponseUtil.handleResponse(response);
        //判断响应结果
        if (mapResponse == null) {
            log.info("响应结果为空:{}", response);
            throw new RuntimeException("响应结果为空");
        }
        if ((Integer) mapResponse.get("errcode") != HttpURLConnection.HTTP_OK) {
            log.info("获取文件系统返回结果错误,响应结果为:{}", response);
            throw new RuntimeException("获取文件系统返回结果错误");
        }
        //获取其中的数据
        Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
        String accessToken = (String) data.get("accessToken");
        return accessToken;
    }

    /**
     * 设置润建请求头中的统一请求头
     * x-fio-appid 应⽤id
     * x-fio-signature 签名信息
     * x-fio-nonce 应⽤⽣成的⾮重复UUID（⼗分钟内不能重复），⽤于结合时间戳防⽌重放
     * x-fio-timestamp 时间戳（单位秒）
     * x-fio-uid ⽤户标识（润建⼯号）
     */
    public HttpURLConnection setHttpURLConnectionHeader(HttpURLConnection connection) throws IOException {
        long currentedTimeMillis = System.currentTimeMillis() / 1000;
        //获取秒单位
        String timestamp = String.valueOf(currentedTimeMillis);
        // 创建一个UUID
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        //获取签名
        String signature = SignatureUtil.signature("30c3e6d495834663ba978636e91c9951", timestamp, nonce, "10011");

        connection.setRequestProperty("x-fio-appid", appKey);
        connection.setRequestProperty("x-fio-timestamp", timestamp);
        connection.setRequestProperty("x-fio-nonce", nonce);
        connection.setRequestProperty("x-fio-signature", signature);
        connection.setRequestProperty("x-fio-uid", uid);
        return connection;
    }

    public String spliceAccessToken(String url) throws IOException {
        String accessToken = getAccessToken();
        return url + "?accessToken=" + accessToken;
    }

    public HttpUriRequest setHttpClientHeader(HttpUriRequest httpRequest) throws IOException {
        long currentedTimeMillis = System.currentTimeMillis() / 1000;
        //获取秒单位
        String timestamp = String.valueOf(currentedTimeMillis);
        // 创建一个UUID
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        //获取签名
        String signature = SignatureUtil.signature("30c3e6d495834663ba978636e91c9951", timestamp, nonce, "10011");

        httpRequest.setHeader("x-fio-appid", appKey);
        httpRequest.setHeader("x-fio-timestamp", timestamp);
        httpRequest.setHeader("x-fio-nonce", nonce);
        httpRequest.setHeader("x-fio-signature", signature);
        httpRequest.setHeader("x-fio-uid", uid);
        return httpRequest;
    }

}
