package org.smartlink.web.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StzdSignatureUtil {
    public static String generateSignature(String appKey, String timestamp, String appSecret) {
        try {
            // 拼接数据
            String data = "appKey" + appKey + "timestamp" + timestamp;
            // 创建HMAC SHA-256实例
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            // 计算HMAC-SHA256值
            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // 对结果进行Base64编码
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}