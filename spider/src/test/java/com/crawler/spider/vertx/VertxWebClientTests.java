package com.crawler.spider.vertx;

import com.crawler.spider.webclient.VertxWebClient;
import org.junit.Test;

public class VertxWebClientTests {
    private VertxWebClient vertxWebClient = new VertxWebClient();

    @Test
    public void get() throws Exception {
        vertxWebClient.get();
    }
}
