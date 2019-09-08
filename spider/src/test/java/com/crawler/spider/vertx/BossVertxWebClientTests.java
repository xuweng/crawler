package com.crawler.spider.vertx;

import com.crawler.spider.webclient.BossVertxWebClient;
import com.crawler.spider.webclient.Request;
import org.junit.Before;
import org.junit.Test;

public class BossVertxWebClientTests {
    private BossVertxWebClient bossVertxWebClient;

    @Before
    public void init() {
        bossVertxWebClient = new BossVertxWebClient();
    }

    @Test
    public void startTest() {
        bossVertxWebClient.start();
    }
}
