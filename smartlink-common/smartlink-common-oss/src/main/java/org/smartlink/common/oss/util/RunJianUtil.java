package org.smartlink.common.oss.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.smartlink.common.core.constant.GlobalConstants;
import org.smartlink.common.oss.constant.OssConstant;
import org.smartlink.common.oss.exception.OssException;
import org.smartlink.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;
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

    /**
     * 获取润建token值
     */
    public String getAccessToken() {
        int i = 6;
        while (i >= 0) {
            try {
                String accessToken = RedisUtils.getCacheObject(OssConstant.RUN_JIAN_TOKEN_KEY);
                if (StringUtils.isNotBlank(accessToken)) {
                    return accessToken;
                }
                String requestId = UUID.randomUUID().toString().replaceAll("-", "");
                // 增加分布式锁
                String cacheRepeatKey = GlobalConstants.REPEAT_SUBMIT_KEY + "runjian:token";
                if (!RedisUtils.setObjectIfAbsent(cacheRepeatKey, requestId, Duration.ofMillis(3000))) {
                    i--;
                    Thread.sleep(500);
                    continue;
                }
                //构建请求，获取access_token
                String url = BaseUrl + AccessTokenUrl;
                // 创建HttpClient实例
                HttpClient httpClient = HttpClientCustomUtil.getHttpClient();
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
                    log.info("获取文件系统accessToken返回结果错误,响应结果为:{}", response);
                    throw new RuntimeException("获取文件系统accessToken返回结果错误");
                }
                //获取其中的数据
                Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
                accessToken = (String) data.get("accessToken");

                if (StringUtils.isNotBlank(accessToken)) {
                    RedisUtils.setCacheObject(OssConstant.RUN_JIAN_TOKEN_KEY, accessToken, Duration.ofMillis(110 * 60 * 1000));
                }
                RedisUtils.deleteObject(cacheRepeatKey);

                return accessToken;
            } catch (Exception e) {
                log.error("获取accessToken有误:", e);
                throw new OssException("获取accessToken有误:[" + e.getMessage() + "]");
            }
        }
        throw new OssException("获取accessToken有误:[重试获取accessToken异常]");
    }

    /**
     *拼接token值
     * @param url
     * @return
     */
    public String spliceAccessToken(String url) {
        String accessToken = getAccessToken();
        return url + "?accessToken=" + accessToken;
    }

    /**
     * 设置润建请求头中的统一请求头
     * x-fio-appid 应⽤id
     * x-fio-signature 签名信息
     * x-fio-nonce 应⽤⽣成的⾮重复UUID（⼗分钟内不能重复），⽤于结合时间戳防⽌重放
     * x-fio-timestamp 时间戳（单位秒）
     * x-fio-uid ⽤户标识（润建⼯号）
     */
    public HttpUriRequest setHttpClientHeader(HttpUriRequest httpRequest, String uid) throws IOException {

        long currentedTimeMillis = System.currentTimeMillis() / 1000;
        //获取秒单位
        String timestamp = String.valueOf(currentedTimeMillis);
        // 创建一个UUID
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        //获取签名
        String signature = SignatureUtil.signature(appSecret, timestamp, nonce, uid);

        httpRequest.setHeader("x-fio-appid", appKey);
        httpRequest.setHeader("x-fio-timestamp", timestamp);
        httpRequest.setHeader("x-fio-nonce", nonce);
        httpRequest.setHeader("x-fio-signature", signature);
        httpRequest.setHeader("x-fio-uid", uid);
        return httpRequest;
    }

}
