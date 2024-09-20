package org.smartlink.web.controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PostExample {

    public static void main(String[] args) {
        try {
            // 创建HttpClient实例
            HttpClient httpClient = HttpClientBuilder.create().build();

            // 创建POST请求
            HttpPost request = new HttpPost("https://yq.runjian.com:31516/v1/oauth2/accessToken");

            // 添加请求头
            request.addHeader("Content-Type", "application/json");

            // 添加请求体（JSON数据）
            String jsonBody = "{\"appKey\": \"yz-yxxt\",\n" +
                    "\"appSecret\": \"30c3e6d495834663ba978636e91c9951\"\n" +
                    "}";
            request.setEntity(new StringEntity(jsonBody));

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(request);

            // 读取响应内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // 打印响应内容
            System.out.println("Response: " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}