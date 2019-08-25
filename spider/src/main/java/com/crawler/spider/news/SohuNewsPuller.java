package com.crawler.spider.news;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;

@Slf4j
@Builder
public class SohuNewsPuller implements NewsPuller {
    private static String url = "http://news.sohu.com/";
    private static NewsService newsService = NewsService.builder().build();

    @Override
    public void pullNews() {
        log.info("开始拉取搜狐新闻！");
        // 1.获取首页
        Document html = null;
        try {
            html = getHtmlFromUrl(url, false);
        } catch (Exception e) {
            log.error("==============获取搜狐首页失败: {}=============", url);
            e.printStackTrace();
            return;
        }
        // 2.jsoup获取新闻<a>标签
        Elements newsATags = html.select("div.focus-news")
                .select("div.list16")
                .select("li")
                .select("a");

        // 3.从<a>标签中抽取基本信息，封装成news
        HashSet<News> newsSet = new HashSet<>();
        for (Element a : newsATags) {
            String url = a.attr("href");
            String title = a.attr("title");
            News n = News.builder().build();
            n.setSource("搜狐");
            n.setUrl(url);
            n.setTitle(title);
            n.setCreateDate(new Date());
            newsSet.add(n);
        }
        // 4.根据新闻url访问新闻，获取新闻内容
        newsSet.forEach(news -> {
            log.info("开始抽取搜狐新闻内容：{}", news.getUrl());
            Document newsHtml = null;
            try {
                newsHtml = getHtmlFromUrl(news.getUrl(), false);
                Element newsContent = newsHtml.select("div#article-container")
                        .select("div.main")
                        .select("div.text")
                        .first();
                String title = newsContent.select("div.text-title").select("h1").text();
                String content = newsContent.select("article.article").first().toString();
                String image = NewsUtils.getImageFromContent(content);

                news.setTitle(title);
                news.setContent(content);
                news.setImage(image);
                newsService.saveNews(news);
                log.info("抽取搜狐新闻《{}》成功！", news.getTitle());
            } catch (Exception e) {
                log.error("新闻抽取失败:{}", news.getUrl());
                e.printStackTrace();
            }
        });
    }
}
