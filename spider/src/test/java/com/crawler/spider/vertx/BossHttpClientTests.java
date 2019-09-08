package com.crawler.spider.vertx;

import com.crawler.spider.httpClient.BossHttpClient;
import org.junit.Before;
import org.junit.Test;

public class BossHttpClientTests {
    private BossHttpClient bossHttpClient;

    @Before
    public void init() {
        bossHttpClient = new BossHttpClient();
    }

    @Test
    public void getNow() {
        bossHttpClient.getNow();
    }
}
