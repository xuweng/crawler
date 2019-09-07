package com.crawler.spider.webclient;

import com.crawler.spider.utils.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;


/**
 * @author hh
 */
@Slf4j
public class VertxWebClient extends AbstractVerticle {

    public void get() {
        Runner.runExample(VertxWebClient.class);
    }

    @Override
    public void start() throws Exception {

        WebClient client = WebClient.create(vertx);

        client.get("https://book.jd.com/", "/").send(ar -> {
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
