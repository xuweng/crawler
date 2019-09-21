package com.webcollector.study;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.RegexRule;

/**
 * 简书爬虫
 */
public class JianShuCrawler extends BreadthCrawler {
    private final static String crawlPath = "/Users/luying/data/db/jianshu";

    private final static String seed = "http://www.jianshu.com/u/e39da354ce50";

    RegexRule regexRule = new RegexRule();

    public JianShuCrawler() {
        super(crawlPath, false);

        //添加爬取种子,也就是需要爬取的网站地址,以及爬取深度
        CrawlDatum datum = new CrawlDatum(seed)
                .meta("depth", "1");
        addSeed(datum);

        //设置线程数,根据自己的需求来搞
        setThreads(2);

        //添加正则表达式规则
        regexRule.addRule("http://.*");
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String name = page.select("a.name").text();
        System.out.println(name);
    }

    public static void main(String[] args) throws Exception {
        //测试
        JianShuCrawler crawler = new JianShuCrawler();
        crawler.start(2);
    }
}
