package com.crawler.spider.news;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ToutiaoApiTests {
    private static final ToutiaoApi toutiaoApi = ToutiaoApi.builder().build();

    @Test
    public void getToutiaoDataTest() {
        log.info("getToutiaoData:{}", toutiaoApi.getToutiaoData());
    }
}
