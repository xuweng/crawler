package com.crawler.spider.news;

import com.crawler.spider.utils.HttpClientUtils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class ToutiaoApi {
    private static final String URL = "https://www.toutiao.com/pgc/ma/?" +
            "page_type=1&max_behot_time=&uid=50502347096&media_id=50502347096&output=json&" +
            "is_json=1&count=20&from=user_profile_app&version=2&as=A125DA2BD89A381&cp=5AB81AE3C8116E1&callback=jsonp3";

    public String getToutiaoData() {
        return HttpClientUtils.get(URL);
    }
}
