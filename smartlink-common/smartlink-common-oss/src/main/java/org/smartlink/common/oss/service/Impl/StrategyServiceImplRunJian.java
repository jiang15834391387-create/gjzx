package org.smartlink.common.oss.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
import java.util.HashMap;
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
    @Value("${runjian.relObjectIdUrl}")
    private String relObjectIdUrl;
    @Value("${runjian.fileInfoUrl}")
    private String fileInfoUrl;
    @Value("${runjian.fileViewUrl}")
    private String fileViewUrl;

    @Autowired
    private RunJianUtil runJianUtil;


    @Override
    public Map upload(File file, String uid) {
        String putObject = BaseUrl + putObjectUrl;
        //拼接accesstoken
        String putObjectUrl = runJianUtil.spliceAccessToken(putObject);
//        CloseableHttpClient httpClient = HttpClientCustomUtil.getHttpClient();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(putObjectUrl);
            //添加基本请求头
            runJianUtil.setHttpClientHeader(httpPost, uid);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            //添加参数
            Path filePath = file.toPath();
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = ContentType.APPLICATION_OCTET_STREAM.getMimeType();
            }
            multipartEntityBuilder.addBinaryBody(
                "file",
                file,
                ContentType.create(contentType),
                file.getName()
            );
            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);
            //构建测试数据
            String path = "";
            String objectId = "";
            String objectType = "";
            multipartEntityBuilder.addTextBody("path", path);
            multipartEntityBuilder.addTextBody("objectId", objectId);
            multipartEntityBuilder.addTextBody("objectType", objectType);

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
    public long download(String fileIds,  HttpServletResponse response,String uid) {
        String url = BaseUrl + getPutObjectUrl;
        // 设置请求URL,拼接token
        String requestUrl = runJianUtil.spliceAccessToken(url);
        String fileId = fileIds;
        String compressType = "";
        try {
            // 使用URIBuilder构建带有查询参数的URL
            URIBuilder uriBuilder = new URIBuilder(requestUrl);
            uriBuilder.addParameter("fileId", fileId);
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
                int contentLength = fileByteArray.length;
                return contentLength;
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
//        String fileId = "1838466485701021698,1838756901222584322";
        String compressType = "zip";
        try {
            // 使用URIBuilder构建带有查询参数的URL
            URIBuilder uriBuilder = new URIBuilder(requestUrl);
            uriBuilder.addParameter("fileId", fileId);
            uriBuilder.addParameter("compressType", compressType);
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
    public void relObjectId(String uid) {
        String url = BaseUrl + relObjectIdUrl;
        // 设置请求URL,拼接token
        String requestUrl = runJianUtil.spliceAccessToken(url);
        CloseableHttpClient httpClient = HttpClientCustomUtil.getHttpClient();
        try {
            HttpPost httpPost = new HttpPost(requestUrl);
            runJianUtil.setHttpClientHeader(httpPost, uid);
            httpPost.setHeader("Content-Type", "application/json");

            //构建测试数据
            String fileId = "1838466485701021698";
            String objectId = "2";
            String objectType = "劳务合同";
            String operateType = "A";

            // 创建Map并填充数据
            Map<String, Object> map = new HashMap<>();
            map.put("fileId", fileId);
            map.put("objectId", objectId);
            map.put("objectType", objectType);
            map.put("operateType", operateType);

            // 使用Jackson库将Map转换为JSON字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(map);
            log.info("请求参数:{}", jsonBody);
            // 设置请求体
            httpPost.setEntity(new StringEntity(jsonBody));

            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 获取响应实体
                Map<String, Object> stringObjectMap = ResponseUtil.handleResponse(response);
            }
        } catch (Exception e) {
            throw new OssException("关联业务主键，错误信息:[" + e.getMessage() + "]");
        }
    }

    @Override
    public void fileInfo(String uid) {
        String url = BaseUrl + fileInfoUrl;
        // 设置请求URL,拼接token
        String requestUrl = runJianUtil.spliceAccessToken(url);
        CloseableHttpClient httpClient = HttpClientCustomUtil.getHttpClient();
        try {
            HttpPost httpPost = new HttpPost(requestUrl);
            runJianUtil.setHttpClientHeader(httpPost, uid);
            httpPost.setHeader("Content-Type", "application/json");

            //构建测试数据
            String fileId = "1838466485701021698";
            String objectId = "1";
            String objectType = "劳务合同";
            boolean appKey = false;

            Map<String, Object> map = new HashMap<>();
            map.put("fileId", fileId);
            map.put("objectId", objectId);
            map.put("objectType", objectType);
            map.put("operateType", appKey);

            // 使用Jackson库将Map转换为JSON字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(map);
            log.info("传参jsonBody:{} " + jsonBody);
            // 设置请求体
            httpPost.setEntity(new StringEntity(jsonBody));
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                log.info("响应,response:{}", response.toString());
                Map<String, Object> stringObjectMap = ResponseUtil.handleResponse(response);
                log.info("响应解析为map格式,map:{}", stringObjectMap);

            }
        } catch (Exception e) {
            log.error("业务主键联查⽂件信息错误:", e);
            throw new OssException("业务主键联查⽂件信息，错误信息:[" + e.getMessage() + "]");
        }

    }

    @Override
    public String fileViewUrl(String uid) {
        String url = BaseUrl + fileViewUrl;
        // 设置请求URL,拼接token
        String requestUrl = runJianUtil.spliceAccessToken(url);

        String fileId = "1838466485701021698";
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
            log.info("响应:{}", response1);
            try {
                // 获取响应的内容
                if (response1.getEntity() != null) {
                    Map<String, Object> stringObjectMap = ResponseUtil.handleResponse(response1);
                    if ((Integer) stringObjectMap.get("errcode") != HttpServletResponse.SC_OK) {
                        throw new OssException("获取文件预览路径响应为空");
                    }
                    String fileUrl = (String) stringObjectMap.get("data");
                    return fileUrl;
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
