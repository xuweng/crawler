package com.crawler.spider.vertx;

import com.crawler.spider.webclient.JdVertxWebClient;
import com.crawler.spider.webclient.Request;
import org.junit.Test;

public class VertxWebClientTests implements Request {
    private JdVertxWebClient jdVertxWebClient = new JdVertxWebClient();

    @Test
    @Override
    public void get() {
        jdVertxWebClient.get();
    }
}
