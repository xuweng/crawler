package com.crawler.spider.httpClient;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;

/**
 * Vertx使用流畅的api
 * Vertx对象是Vert.x的控制中心,是核心对象
 * Vertx对象是是工厂
 */
public class BossHttpClient {
    public void s() {
//        Vertx vertx = Vertx.vertx();
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
        HttpClient httpClient = vertx.createHttpClient();

        String requestURI = "https://www.zhipin.com/wapi/zpCommon/data/position.json";
        httpClient.get(requestURI);
    }
}
