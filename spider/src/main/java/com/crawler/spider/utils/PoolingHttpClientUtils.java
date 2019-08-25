package com.crawler.spider.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * 单线程-使用连接池管理HTTP请求
 */
@Slf4j
public class PoolingHttpClientUtils {
    public static String get(String url) {
        log.info("url:{}", url);
        //创建HTTP的连接池管理对象
        PoolingHttpClientConnectionManager connectionManager = getPoolingHttpClientConnectionManager();

        long start = System.currentTimeMillis();
        String content = doGet(connectionManager, url);
        long end = System.currentTimeMillis();
        log.info("consume time:{}", (end - start));

        //清理无效连接
        new IdleConnectionEvictor(connectionManager).start();

        return content;
    }

    /**
     * 请求重试处理
     *
     * @param tryTimes 重试次数
     * @return
     */
    private static HttpRequestRetryHandler retryHandler(final int tryTimes) {

        return (exception, executionCount, context) -> {
            // 如果已经重试了n次，就放弃
            if (executionCount >= tryTimes) {
                return false;
            }
            // 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            // 不要重试SSL握手异常
            if (exception instanceof SSLHandshakeException) {
                return false;
            }
            // 超时
            if (exception instanceof InterruptedIOException) {
                return false;
            }
            // 目标服务器不可达
            if (exception instanceof UnknownHostException) {
                return true;
            }
            // 连接被拒绝
            // SSL握手异常
            if (exception instanceof SSLException) {
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            return !(request instanceof HttpEntityEnclosingRequest);
        };
    }

    /**
     * doGet
     *
     * @param connectionManager
     * @param url               请求地址
     * @return
     * @throws Exception
     */
    public static String doGet(HttpClientConnectionManager connectionManager, String url) {
        //从连接池中获取client对象，多例
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRetryHandler(retryHandler(5)).build();

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        // 构建请求配置信息
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) // 创建连接的最长时间
                .setConnectionRequestTimeout(500) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(10 * 1000) // 数据传输的最长时间10s
                .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
                .build();
        // 设置请求配置信息
        httpGet.setConfig(config);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // 执行请求
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 此处不能关闭httpClient，如果关闭httpClient，连接池也会销毁
        // httpClient.close();
        return null;
    }

    /**
     * 监听连接池中空闲连接，清理无效连接
     */
    public static class IdleConnectionEvictor extends Thread {

        private final HttpClientConnectionManager connectionManager;

        private volatile boolean shutdown;

        IdleConnectionEvictor(HttpClientConnectionManager connectionManager) {
            this.connectionManager = connectionManager;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        //3s检查一次
                        wait(3000);
                        // 关闭失效的连接
                        connectionManager.closeExpiredConnections();
                    }
                }
            } catch (InterruptedException ex) {
                // 结束
                ex.printStackTrace();
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    public static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        //创建HTTP的连接池管理对象
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //将最大连接数增加到200
        connectionManager.setMaxTotal(200);
        //将每个路由的默认最大连接数增加到20
        connectionManager.setDefaultMaxPerRoute(20);
        //将http://www.baidu.com:80的最大连接增加到50
        //HttpHost httpHost = new HttpHost("http://www.baidu.com",80);
        //connectionManager.setMaxPerRoute(new HttpRoute(httpHost),50);

        return connectionManager;
    }
}
