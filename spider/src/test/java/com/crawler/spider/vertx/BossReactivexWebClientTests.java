package com.crawler.spider.vertx;

import com.crawler.spider.webclient.BossReactivexWebClient;
import org.junit.Before;
import org.junit.Test;

public class BossReactivexWebClientTests {
    private BossReactivexWebClient bossReactivexWebClient;

    @Before
    public void init() {
        bossReactivexWebClient = new BossReactivexWebClient();
    }

    @Test
    public void start() {
        bossReactivexWebClient.start();
    }
}
