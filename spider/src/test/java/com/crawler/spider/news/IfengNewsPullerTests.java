package com.crawler.spider.news;

import org.junit.Test;

public class IfengNewsPullerTests {
    private IfengNewsPuller ifengNewsPuller = IfengNewsPuller.builder().build();

    @Test
    public void pullNewsTest() {
        ifengNewsPuller.pullNews();
    }
}
