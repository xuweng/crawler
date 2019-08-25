package com.crawler.spider.news;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashSet;

@Slf4j
@Builder
public class IfengNewsPuller implements NewsPuller {
    private static String url = "http://www.ifeng.com/";
    private static NewsService newsService = NewsService.builder().build();

    @Override
    public void pullNews() {
        log.info("开始拉取凤凰新闻！");
        // 1.获取首页
        Document html= null;
        try {
            html = getHtmlFromUrl(url, false);
        } catch (Exception e) {
            log.error("==============获取凤凰首页失败: {} =============", url);
            e.printStackTrace();
            return;
        }
        // 2.jsoup获取新闻<a>标签
        Elements newsATags = html.select("div#headLineDefault")
                .select("ul.FNewMTopLis")
                .select("li")
                .select("a");

        // 3.从<a>标签中抽取基本信息，封装成news
        HashSet<News> newsSet = new HashSet<>();
        for (Element a : newsATags) {
            String url = a.attr("href");
            String title = a.text();
            News n = News.builder().build();
            n.setSource("凤凰");
            n.setUrl(url);
            n.setTitle(title);
            n.setCreateDate(new Date());
            newsSet.add(n);
        }
        // 4.根据新闻url访问新闻，获取新闻内容
        newsSet.parallelStream().forEach(news -> {
            log.info("开始抽取凤凰新闻《{}》内容：{}", news.getTitle(), news.getUrl());
            Document newsHtml = null;
            try {
                newsHtml = getHtmlFromUrl(news.getUrl(), false);
                Elements contentElement = newsHtml.select("div#main_content");
                if (contentElement.isEmpty()) {
                    contentElement = newsHtml.select("div#yc_con_txt");
                }
                if (contentElement.isEmpty())
                    return;
                String content = contentElement.toString();
                String image = NewsUtils.getImageFromContent(content);
                news.setContent(content);
                news.setImage(image);
                newsService.saveNews(news);
                log.info("抽取凤凰新闻《{}》成功！", news.getTitle());
            } catch (Exception e) {
                log.error("凤凰新闻抽取失败:{}", news.getUrl());
                e.printStackTrace();
            }
        });

        log.info("凤凰新闻抽取完成！");
    }
}
