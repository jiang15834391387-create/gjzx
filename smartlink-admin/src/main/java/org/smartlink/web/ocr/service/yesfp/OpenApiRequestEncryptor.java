package org.smartlink.web.ocr.service.yesfp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * bip税务云加密类
 */
public class OpenApiRequestEncryptor {

    private static final String ALGORITHM = "HmacSHA256";

    public static String signature(Map<String, String> params, String appSecret) throws Exception {
        String source = buildSource(params);
        byte[] bytes;
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            mac.init(secretKey);
            bytes = mac.doFinal(source.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new Exception("exception when do open api token request signature", e);
        }

        String hash = Base64.getEncoder().encodeToString(bytes);
        try {
            return URLEncoder.encode(hash, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new Exception("implementation error, utf-8 not found, What year is it?");
        }
    }

    private static String buildSource(Map<String, String> params) {
        Map<String, String> treeMap;
        if (params instanceof TreeMap) {
            treeMap = params;
        } else {
            treeMap = new TreeMap<>(params);
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            builder.append(entry.getKey()).append(entry.getValue());
        }
        return builder.toString();
    }
}
