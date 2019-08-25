package com.crawler.spider.news;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class News {
    private String url;
    private String title;
    private String source;
    private Date createDate;
    private String content;
    private String image;
}
