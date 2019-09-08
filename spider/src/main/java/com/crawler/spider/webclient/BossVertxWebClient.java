package com.crawler.spider.webclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hh
 */
@Slf4j
public class BossVertxWebClient extends AbstractVerticle {
    @Override
    public void start() {
        vertx = Vertx.vertx(new VertxOptions());
        WebClient client = WebClient.create(vertx);

        String host = "https://www.zhipin.com/wapi/zpCommon/data/position.json";
        String requestURI = "/";

        client.get(host, requestURI).send(ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                log.info("Got HTTP response with status:{},with data:{}", response.statusCode(),
                        response.body().toString("ISO-8859-1"));
            } else {
                log.error("Got HTTP response with status", ar.cause());
            }
        });
    }
}
