package org.smartlink.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName: requestHeaderUtil
 * Package: org.smartlink.web.util
 * Description:
 *
 * @Author 张志强
 * @Create 2024/9/23 9:16
 * @Version 1.0
 */
public class requestHeaderUtil {

    /**
     * 获取请求头基本参数
     * @return
     */
    public static Map<String,Object> getRequestHeader() {
        //时间戳毫秒
        long currentedTimeMillis = System.currentTimeMillis();
        //秒
        long currentedTimeSeconds = currentedTimeMillis / 1000;

        String nonce = UUID.randomUUID().toString();
        Map<String,Object> requestHeaderBase = new HashMap<String,Object>();
        requestHeaderBase.put("x-fio-appid", "pc");
        //生成签名信息
        String signature = SignatureUtil.signature("30c3e6d495834663ba978636e91c9951", String.valueOf(currentedTimeSeconds), nonce, "10011");
        requestHeaderBase.put("x-fio-signature", signature);
        requestHeaderBase.put("x-fio-nonce", nonce);
        requestHeaderBase.put("x-fio-timestamp", currentedTimeSeconds);
        requestHeaderBase.put("x-fio-uid", "10011");
        return requestHeaderBase;

    }
}
