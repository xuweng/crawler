package com.crawler.spider.news;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Builder
public class ToutiaoNewsPuller implements NewsPuller {

    private static final String TOUTIAO_URL = "https://www.toutiao.com";

    private static String url = "https://www.toutiao.com";
    private static NewsService newsService = NewsService.builder().build();

    @Override
    public void pullNews() {
        log.info("开始拉取今日头条热门新闻！");
        // 1.load html from url
        Document html = null;
        try {
            html = getHtmlFromUrl(url, true);
        } catch (Exception e) {
            log.error("获取今日头条主页失败！");
            e.printStackTrace();
            return;
        }

        // 2.parse the html to news information and load into POJO
        Map<String, News> newsMap = new HashMap<>();
        for (Element a : html.select("a[href~=/group/.*]:not(.comment)")) {
            log.info("标签a: \n{}", a);
            String href = TOUTIAO_URL + a.attr("href");
            String title = StringUtils.isNotBlank(a.select("p").text()) ?
                    a.select("p").text() : a.text();
            String image = a.select("img").attr("src");

            News news = newsMap.get(href);
            if (news == null) {
                News n = News.builder().build();
                n.setSource("今日头条");
                n.setUrl(href);
                n.setCreateDate(new Date());
                n.setImage(image);
                n.setTitle(title);
                newsMap.put(href, n);
            } else {
                if (a.hasClass("img-wrap")) {
                    news.setImage(image);
                } else if (a.hasClass("title")) {
                    news.setTitle(title);
                }
            }
        }

        log.info("今日头条新闻标题拉取完成!");
        log.info("开始拉取新闻内容...");
        newsMap.values().parallelStream().forEach(news -> {
            log.info("===================={}====================", news.getTitle());
            Document contentHtml = null;
            try {
                contentHtml = getHtmlFromUrl(news.getUrl(), true);
            } catch (Exception e) {
                log.error("获取新闻《{}》内容失败！", news.getTitle());
                return;
            }
            Elements scripts = contentHtml.getElementsByTag("script");
            scripts.forEach(script -> {
                String regex = "articleInfo: \\{\\s*[\\n\\r]*\\s*title: '.*',\\s*[\\n\\r]*\\s*content: '(.*)',";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(script.toString());
                if (matcher.find()) {
                    String content = matcher.group(1)
                            .replace("&lt;", "<")
                            .replace("&gt;", ">")
                            .replace("&quot;", "\"")
                            .replace("&#x3D;", "=");
                    log.info("content: {}", content);
                    news.setContent(content);
                }
            });
        });
        newsMap.values()
                .stream()
                .filter(news -> StringUtils.isNotBlank(news.getContent()) && !news.getContent().equals("null"))
                .forEach(newsService::saveNews);
        log.info("今日头条新闻内容拉取完成!");

    }
}
