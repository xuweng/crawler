package com.crawler.spider.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * 多线程-HttpClient连接池管理HTTP请求实例
 */
@Slf4j
public class MultiThreadHttpUtils {
    public static void get(String[] uris) {
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(getPoolingHttpClientConnectionManager()).build();
        //为每一个URI创建一个线程
        GetThread[] threads = new GetThread[uris.length];
        IntStream.range(0, threads.length).forEach(i -> {
            HttpGet httpGet = new HttpGet(uris[i]);
            threads[i] = new GetThread(httpClient, httpGet);
        });
        //启动线程
        for (GetThread thread : threads) {
            thread.start();
        }
        //join 线程
        for (GetThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行Get请求线程
     */
    public static class GetThread extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpClientContext context;
        private final HttpGet httpGet;

        GetThread(CloseableHttpClient httpClient, HttpGet httpGet) {
            this.httpClient = httpClient;
            this.context = HttpClientContext.create();
            this.httpGet = httpGet;
        }

        @Override
        public void run() {
            try (CloseableHttpResponse response = httpClient.execute(httpGet, context);) {
                // 执行请求
                // 判断返回状态是否为200
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");

                    log.info("content:{}", content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        //连接池对象
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //将最大连接数增加到200
        connectionManager.setMaxTotal(200);
        //将每个路由的默认最大连接数增加到20
        connectionManager.setDefaultMaxPerRoute(20);

        return connectionManager;
    }
}
