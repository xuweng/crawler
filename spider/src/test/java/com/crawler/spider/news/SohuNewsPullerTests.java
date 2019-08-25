package com.crawler.spider.news;

import org.junit.Test;

public class SohuNewsPullerTests {
    private SohuNewsPuller sohuNewsPuller = SohuNewsPuller.builder().build();

    @Test
    public void pullNewsTest() {
        sohuNewsPuller.pullNews();
    }
}
