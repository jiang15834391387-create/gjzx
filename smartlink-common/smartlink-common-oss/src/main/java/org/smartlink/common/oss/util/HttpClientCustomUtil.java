package org.smartlink.common.oss.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: a
 * Package: org.smartlink.common.oss.util
 * Description:
 *
 * @Author 张志强
 * @Create 2024/9/25 23:07
 * @Version 1.0
 */
public class HttpClientCustomUtil {
    /**
     * 实例化http连接池, 保证单例和懒加载, 防止业务系统不使用本类造成资源浪费
     */
    private static class HttpClientPool {

        private static final CloseableHttpClient HTTP_CLIENT;

        static {
            // 先设置http连接的一些配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(6000)
                    // 建立连接的超时时间
                    .setConnectTimeout(6000)
                    // 请求的超时时间
                    .setSocketTimeout(6000).build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            // 配置最大的连接数
            cm.setMaxTotal(200);
            // 每个路由最大连接数，路由是根据host来管理的
            cm.setDefaultMaxPerRoute(150);

            HTTP_CLIENT = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(requestConfig)
                    // 开启超长时间回收连接
                    .evictExpiredConnections()
                    // 超时时间回收
                    .evictIdleConnections(60L, TimeUnit.SECONDS)
                    .build();
        }

        /**
         * 私有化构造方法，防止外部实例化
         */
        private HttpClientPool() {
        }
    }


    /**
     * 公用静态方法获取http连接池
     */
    public static CloseableHttpClient getHttpClient() {
        return HttpClientPool.HTTP_CLIENT;
    }
}
