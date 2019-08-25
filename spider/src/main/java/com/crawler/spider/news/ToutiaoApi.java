package com.crawler.spider.news;

import com.crawler.spider.utils.HttpClientUtils;
import com.crawler.spider.utils.Utils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Builder
public class ToutiaoApi {
    private static String URL = "https://www.toutiao.com/pgc/ma/?" +
            "page_type=1&max_behot_time=&uid=50502347096&media_id=50502347096&output=json&" +
            "is_json=1&count=20&from=user_profile_app&version=2&callback=jsonp3";

    public String getToutiaoData() {
        Map<String, String> map = Utils.getToutiaoAsCp();

        String as = map.get("as");
        String cp = map.get("cp");

        URL += "&as=" + as + "&cp=" + cp;
        return HttpClientUtils.get(URL);
    }
}
