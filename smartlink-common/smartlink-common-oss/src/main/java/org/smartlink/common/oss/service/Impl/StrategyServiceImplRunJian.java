package org.smartlink.common.oss.service.Impl;

import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.smartlink.common.oss.exception.OssException;
import org.smartlink.common.oss.service.StrategyService;
import org.smartlink.common.oss.util.HttpClientCustomUtil;
import org.smartlink.common.oss.util.ResponseUtil;
import org.smartlink.common.oss.util.RunJianUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * ClassName: StrategyServiceImpl
 * Package: com.example.strategydemo.service.Impl
 * Description:
 *
 * @Author zzq
 * @Create 2024/2/2 20:16
 * @Version 1.0
 */
@Slf4j
@Service()
public class StrategyServiceImplRunJian implements StrategyService {

    @Value("${runjian.baseUrl}")
    private String BaseUrl;

    @Value("${runjian.putObjectUrl}")
    private String putObjectUrl;

    @Value("${runjian.getObjectUrl}")
    private String getPutObjectUrl;

    @Value("${runjian.fileViewUrl}")
    private String fileViewUrl;

    @Autowired
    private RunJianUtil runJianUtil;


    @Override
    public Map upload(File file, String fileName, String uid) {
        String putObject = BaseUrl + putObjectUrl;
        // 拼接accesstoken
        String putObjectUrl = runJianUtil.spliceAccessToken(putObject);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(putObjectUrl);
            // 添加基本请求头
            runJianUtil.setHttpClientHeader(httpPost, uid);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 添加参数
            Path filePath = file.toPath();

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = ContentType.APPLICATION_OCTET_STREAM.getMimeType();
            }

            multipartEntityBuilder.addBinaryBody(
                "file",
                file,
                ContentType.create(contentType),
                // 原始文件名
                fileName
            );

            // 构建数据
            String objectId = "yxxt" + IdUtil.simpleUUID();
            String objectType = "yxxt";

            multipartEntityBuilder.addTextBody("objectId", objectId);
            multipartEntityBuilder.addTextBody("objectType", objectType);


            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);


            CloseableHttpResponse response = httpClient.execute(httpPost);

            Map<String, Object> stringObjectMap = ResponseUtil.handleResponse(response);

            if ((Integer) stringObjectMap.get("errcode") != HttpServletResponse.SC_OK) {
                log.info("文件上传服务器错误,响应内容:{}", stringObjectMap);
                throw new IOException("文件上传服务器错误");
            }
            return stringObjectMap;
        } catch (IOException e) {
            log.info("文件上传服务器错误,", e);
            throw new OssException("上传文件系统错误:" + e.getMessage());
        }
    }

    @Override
    public long download(String fileIds, HttpServletResponse response, String uid) {
        String url = BaseUrl + getPutObjectUrl;
        // 设置请求URL,拼接token
        String requestUrl = runJianUtil.spliceAccessToken(url);
        String compressType = "zip";
        try {
            // 使用URIBuilder构建带有查询参数的URL
            URIBuilder uriBuilder = new URIBuilder(requestUrl);
            uriBuilder.addParameter("fileId", fileIds);
            uriBuilder.addParameter("compressType", compressType);
            URI uri = uriBuilder.build();
            // 创建HttpGet请求
            HttpGet httpGet = new HttpGet(uri);
            // 设置自定义的HTTP头
            runJianUtil.setHttpClientHeader(httpGet, uid);
            CloseableHttpClient httpClient = HttpClientCustomUtil.getHttpClient();
            // 执行请求
            try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet)) {
                int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
                if (statusCode != HttpServletResponse.SC_OK) {
                    throw new OssException("获取文件异常");
                }
                // 获取响应实体
                HttpEntity entity = closeableHttpResponse.getEntity();

                if (entity == null) {
                    throw new OssException("文件下载失败");
                }
                byte[] fileByteArray = EntityUtils.toByteArray(entity);
                OutputStream out = response.getOutputStream();
                // 将实体内容写入到servlet响应输出流
//                entity.writeTo(out);
                out.write(fileByteArray);
                return fileByteArray.length;
            }
        } catch (Exception e) {
            throw new OssException("文件下载失败，错误信息:[" + e.getMessage() + "]");
        }
    }


    @Override
    public byte[] downloadByte(String fileId, String uid) {
        String url = BaseUrl + getPutObjectUrl;
        // 设置请求URL,拼接token
        String requestUrl = runJianUtil.spliceAccessToken(url);
        try {
            // 使用URIBuilder构建带有查询参数的URL
            URIBuilder uriBuilder = new URIBuilder(requestUrl);
            uriBuilder.addParameter("fileId", fileId);
            URI uri = uriBuilder.build();
            // 创建HttpGet请求
            HttpGet httpGet = new HttpGet(uri);
            // 设置自定义的HTTP头
            runJianUtil.setHttpClientHeader(httpGet, uid);
            CloseableHttpClient httpClient = HttpClientCustomUtil.getHttpClient();
            // 执行请求
            try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet)) {
                // 获取响应实体
                HttpEntity entity = closeableHttpResponse.getEntity();
                if (entity == null) {
                    throw new OssException("文件下载失败");
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // 将实体内容写入到servlet响应输出流
                entity.writeTo(byteArrayOutputStream);
                return entity.toString().getBytes();
            }
        } catch (Exception e) {
            throw new OssException("文件下载失败，错误信息:[" + e.getMessage() + "]");
        }
    }


    @Override
    public String fileViewUrl(String fileId, String uid) {
        String url = BaseUrl + fileViewUrl;
        // 设置请求URL,拼接token
        String requestUrl = runJianUtil.spliceAccessToken(url);

        CloseableHttpClient httpClient = HttpClientCustomUtil.getHttpClient();
        try {
            // 使用URIBuilder构建带有查询参数的URL
            URIBuilder uriBuilder = new URIBuilder(requestUrl);
            uriBuilder.addParameter("fileId", fileId);

            URI uri = uriBuilder.build();
            // 创建HttpGet请求
            HttpGet httpGet = new HttpGet(uri);
            // 设置自定义的HTTP头
            runJianUtil.setHttpClientHeader(httpGet, uid);
            // 执行请求
            CloseableHttpResponse response1 = httpClient.execute(httpGet);
            try {
                // 获取响应的内容
                if (response1.getEntity() != null) {
                    Map<String, Object> stringObjectMap = ResponseUtil.handleResponse(response1);
                    if ((Integer) stringObjectMap.get("errcode") != HttpServletResponse.SC_OK) {
                        throw new OssException("获取文件预览路径响应为空");
                    }
                    log.info("获取文件预览路径响应为:{}", stringObjectMap.get("data"));
                    return (String) stringObjectMap.get("data");
                }
                return null;
            } catch (Exception e) {
                throw new OssException("获取文件预览路径响应为空");
            }
        } catch (Exception e) {
            log.error("获取文件预览路径错误:", e);
            throw new OssException("获取文件预览路径错误:[" + e.getMessage() + "]");
        }
    }
}
