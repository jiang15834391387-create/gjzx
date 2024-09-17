package org.smartlink.web;

import org.smartlink.common.core.utils.signature.SignatureUtil;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HttpClient {

    public static void main(String[] args) {
        // 设置参数
        String appKey = "yz-yxxt";
        String appSecret = "30c3e6d495834663ba978636e91c9951";
        String uid = "99239";
        String nonce = UUID.randomUUID().toString(); // 使用UUID生成随机nonce
        long timestamp = System.currentTimeMillis() / 1000L; // 当前时间戳，以秒为单位

        // 生成签名
        String signature = SignatureUtil.signature(appSecret, String.valueOf(timestamp), nonce, uid);

        // 构建请求URL
        String urlStr = "https://yq.runjian.com:31573/file/v1/putObject";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("x-fio-appid", appKey);
            connection.setRequestProperty("x-fio-signature", signature);
            connection.setRequestProperty("x-fio-nonce", nonce);
            connection.setRequestProperty("x-fio-timestamp", String.valueOf(timestamp));
            connection.setRequestProperty("x-fio-uid", uid);
            connection.setRequestProperty("Content-Type", "multipart/form-data");

            // 设置允许输出
            connection.setDoOutput(true);

            // 创建输出流，这里假设您有文件要上传，需要构造合适的请求体
            OutputStream outputStream = connection.getOutputStream();
            // 这里应该添加文件数据，示例中省略
            // ...

            // 关闭输出流
            outputStream.close();

            // 获取响应状态码
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            // 处理响应内容
            // ...

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}