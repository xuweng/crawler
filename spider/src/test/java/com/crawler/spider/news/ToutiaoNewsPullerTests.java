package com.crawler.spider.news;

import org.junit.Test;

public class ToutiaoNewsPullerTests {
    private ToutiaoNewsPuller toutiaoNewsPuller = ToutiaoNewsPuller.builder().build();

    @Test
    public void pullNewsTest() {
        toutiaoNewsPuller.pullNews();
    }
}
