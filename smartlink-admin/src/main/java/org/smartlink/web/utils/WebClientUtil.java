package org.smartlink.web.utils;


import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * webClient 请求工具类
 * @author L
 */

@Slf4j
public class WebClientUtil {

    private static final ReactorResourceFactory factory = new ReactorResourceFactory();

    private static final WebClient webClient;

    static {
        factory.setUseGlobalResources(false);
        factory.setConnectionProvider(ConnectionProvider.create("httpClient", 50));
        factory.setLoopResources(LoopResources.create("httpClient", 50, true));

        Function<HttpClient, HttpClient> mapper = client ->
                client.tcpConfiguration(c ->
                        c.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)     //连接超时时间
                                .option(ChannelOption.TCP_NODELAY, true)          //启用Nagle算法   提高实时性
                                .doOnConnected(conn -> {
                                    conn.addHandlerLast(new ReadTimeoutHandler(10)); //设置读的超时时间
                                    conn.addHandlerLast(new WriteTimeoutHandler(10));//设置写的超时时间
                                }));


        HttpClient secure = HttpClient.create()
                .secure(t -> t.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)));



        webClient = WebClient.builder()
                .exchangeStrategies(builder ->
                        builder.codecs(codecs -> codecs.defaultCodecs().
                                maxInMemorySize(20 * 1024 * 1024))).build();
    }

    public static WebClient getWebClient(){
        return webClient;
    }


    /**
     * <p>get请求</p>
     * @param url 请求地址
     * @return java.lang.String
     */

    public static Object get(String url) {
        return get(url, new HashMap<>());
    }

    /**
     * <p>get请求</p>
     * @param url 请求地址
     * @param map 请求参数
     * @return  返回值 Object
     */
    public static Object get(String url, Map<String, Object> map) {
        if (map.size() > 0) {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(url);
            if (url.contains("?")) {
                stringBuffer.append("&");
            } else {
                stringBuffer.append("?");
            }
            for (String key : map.keySet()) {
                stringBuffer.append(key).append("=").append(map.get(key).toString()).append("&");
            }
            url = stringBuffer.toString();
        }
        String responseResult = null;
        Mono<String> mono = webClient.get().uri(url).retrieve().bodyToMono(String.class);
        responseResult = mono.block();

        return responseResult;
    }

    /**
     * <p>get请求</p>
     * @param url 请求地址
     * @param map 请求参数
     * @return 返回值 Object
     */
    public static Object getBytes(String url, Map<String, Object> map) {
        if (map.size() > 0) {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(url);
            if (url.contains("?")) {
                stringBuffer.append("&");
            } else {
                stringBuffer.append("?");
            }
            for (String key : map.keySet()) {
                stringBuffer.append(key).append("=").append(map.get(key).toString()).append("&");
            }
            url = stringBuffer.toString();
        }
        byte[] bytes = null;
        Mono<ClientResponse> exchange = webClient.get().uri(url).exchange();
        ClientResponse response = exchange.block();
        if (response.statusCode() == HttpStatus.OK) {
            Mono<byte[]> mono =  response.bodyToMono(byte[].class);
            bytes = mono.block();

            //判断是否需要解压，即服务器返回是否经过了gzip压缩--start
            List<String> header = response.headers().header("Content-Encoding");
            if (header.contains("gzip")) {
                GZIPInputStream gzipInputStream = null;
                ByteArrayOutputStream out = null;
                try {
                    gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
                    out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int offset = -1;
                    while ((offset = gzipInputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, offset);
                    }
                    bytes = out.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        gzipInputStream.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //判断是否需要解压，即服务器返回是否经过了gzip压缩--end
        }

        return bytes;
    }


    /**
     * <p>post请求</p>
     * @param url  请求地址
     * @return 返回值 Object
     */
    public static Object post(String url) {
        return post(url, new HashMap<>());
    }

    /**
     * <p>post请求</p>
     * @param url 请求地址
     * @param params  请求参数
     * @return 返回值 Object
     */
    public static Object post(String url, Map<String, Object> params) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if (params != null && params.size() > 0) {
            map.setAll(params);
        }
        String responseResult = null;
        Mono<String> mono = webClient.post().uri(url).bodyValue(map).retrieve().bodyToMono(String.class);
        responseResult = mono.block();

        return responseResult;
    }
    /**
     * <p>post请求</p>
     * @param url  请求地址
     * @param params   请求参数
     * @return 返回值 Object
     */
    public static Object postBytes(String url, Map<String, Object> params) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if (params != null && params.size() > 0) {
            map.setAll(params);
        }
        byte[] bytes = null;
        Mono<ClientResponse> exchange = webClient.post().uri(url).bodyValue(map).exchange();
        ClientResponse response = exchange.block();
        if (response.statusCode() == HttpStatus.OK) {
            Mono<byte[]> mono =  response.bodyToMono(byte[].class);
            bytes = mono.block();

            //判断是否需要解压，即服务器返回是否经过了gzip压缩--start
            List<String> header = response.headers().header("Content-Encoding");
            if (header.contains("gzip")) {
                GZIPInputStream gzipInputStream = null;
                ByteArrayOutputStream out = null;
                try {
                    gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
                    out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int offset;
                    while ((offset = gzipInputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, offset);
                    }
                    bytes = out.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        gzipInputStream.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //判断是否需要解压，即服务器返回是否经过了gzip压缩--end
        }


        return bytes;
    }

    /**
     * <p>post请求，form表单提交</p>
     * @param url   请求地址
     * @param params  请求参数（这里要注意将参数值转为字符串，否则会报类型错误）
     * @return 返回值 Object
     */
    public static Object postForm(String url, Map<String, Object> params) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                map.add(key, params.get(key).toString());
            }
        }
        String responseResult;
        Mono<String> mono = webClient.post().uri(url).contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(map).retrieve().bodyToMono(String.class);
        responseResult = mono.block();

        return responseResult;
    }
    /**
     * <p>post请求，json</p>
     * @param url  请求地址
     * @param json json请求参数
     * @return 返回值 Object
     */

    public static Object postJson(String url, String json) {
        return postJson(url, json, false);
    }

    /**
     * <p>post请求，json</p>
     * @param url  请求地址
     * @param json json请求参数
     * @param gzip  是否压缩
     * @return 返回值 Object
     */
    public static Object postJson(String url, String json, boolean gzip) {
        String responseResult;
        if (gzip) {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            return postBytes(url, bytes, true);
        } else {
            responseResult = webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(json).retrieve().bodyToMono(String.class).block();
        }

        return responseResult;
    }
    /**
     * <p>post请求，字节流</p>
     * @param url  请求地址
     * @param bytes json请求参数
     * @return 返回值 Object
     */
    public static Object postBytes(String url, byte[] bytes) {
        return postBytes(url, bytes, false);
    }

    /**
     * <p>post请求，字节流</p>
     * @param url  请求地址
     * @param bytes  请求参数
     * @param gzip  是否压缩
     * @return 返回值 Object
     */
    public static String postBytes(String url, byte[] bytes, boolean gzip) {
        String responseResult;
        Mono<String> mono = null;
        WebClient.RequestBodySpec requestBodySpec = webClient.post().uri(url).contentType(MediaType.APPLICATION_OCTET_STREAM);
        if (gzip) {
            try {
                //headers.add("Content-Encoding", "gzip");
                ByteArrayOutputStream originalContent = new ByteArrayOutputStream();
                originalContent.write(bytes);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
                originalContent.writeTo(gzipOut);
                gzipOut.finish();
                bytes = baos.toByteArray();
                mono = requestBodySpec.header("Content-Encoding", "gzip").bodyValue(bytes).retrieve().bodyToMono(String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mono = requestBodySpec.bodyValue(bytes).retrieve().bodyToMono(String.class);
        }
        responseResult = mono.block();

        return responseResult;

    }

    /**
     * <p>post请求，流</p>
     * @param url  请求地址
     * @param is 请求参数
     * @return 返回值 Object
     */
    public static Object postStream(String url, InputStream is) {
        return postStream(url, is, false);
    }

    /**
     * <p>post请求，流</p>
     * @param url  请求地址
     * @param is 请求参数
     * @param gzip  是否压缩
     * @return 返回值 Object
     */
    public static Object postStream(String url, InputStream is, boolean gzip) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int ch;
        byte[] bytes = null;
        try {
            while ((ch = is.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, ch);
            }
            bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postBytes(url, bytes, gzip);

    }


    /**
     * <p>post请求，文件</p>
     * @param url  请求地址
     * @param files  文件参数
     * @return 返回值 Object
     */
    public static Object postFiles(String url, List<File> files) {
        return postFiles(url, new HashMap<>(), files);
    }

    /**
     * <p>post请求，文件</p>
     * @param url  请求地址
     * @param files  文件参数
     * @return 返回值 Object
     */
    public static Object postFileUpload(String url, List<File> files) {
        String responseResult = "";
        for (File file : files) {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("files", new FileSystemResource(file));
            responseResult = webClient.post().uri(url).bodyValue(builder.build()).retrieve().bodyToMono(String.class).block();
        }
            return responseResult;
    }

    /**
     *
     * @param url 请求地址
     * @param params 其他参数
     * @param files 文件参数
     * @return Object
     */
    public static Object postFiles(String url, Map<String, Object> params, List<File> files) {
        String responseResult;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if (params != null && params.size() > 0) {
            map.setAll(params);
        }

        for (File file : files) {
            map.add("files", new FileSystemResource(file));
        }
        Mono<String> mono = webClient.post().uri(url).contentType(MediaType.MULTIPART_FORM_DATA).bodyValue(map).retrieve().bodyToMono(String.class);
        responseResult = mono.block();

        return responseResult;
    }

    public static String sendHttpWebService(String urlWsdl, String soap) {
        /*WebClient webClient = WebClient.builder().exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> {
            configurer.defaultCodecs()
                    .maxInMemorySize(16 * 1024 * 1024);
        }).build()).build();*/
        WebClient.ResponseSpec response = webClient.post().uri(urlWsdl)
                .header("Connection", "keep-alive")
                .header("Content-Type", "text/xml;charset=UTF-8")
                .bodyValue(soap).retrieve();
        Mono<String> mono = response.bodyToMono(String.class);
        return mono.block();
    }

    /**
     * <p>post请求，json</p>
     * @param url  请求地址
     * @param json json请求参数对象
     * @return 返回值 Object
     */
    public static Object postObjectJson(String url, String json) {
        return webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(json).retrieve().bodyToMono(Object.class).block();
    }

}
