package com.crawler.spider.webclient;

import com.crawler.spider.entity.User;
import com.crawler.spider.utils.VertxReactivexUtils;
import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.client.predicate.ResponsePredicate;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * Vert.x提供了一个简单，可扩展，类似于actor的部署和并发模型
 * 该模型并未声称是严格的actor模型实现，但它确实具有相似性，特别是在并发，扩展和部署方面。
 * <p>
 * 要使用此模型，请将代码编写为Verticle集。
 * <p>
 * Verticle是由Vert.x部署和运行的代码块。Vert.x实例默认维护N个事件循环线程（其中N默认为core * 2）。
 * Verticle可以用Vert.x支持的任何语言编写，单个应用程序可以包含用多种语言编写的Verticle。
 * <p>
 * 您可以将Verticle视为有点像Actor模型中的actor。
 *
 * @author hh
 */
@Slf4j
public class BossReactivexWebClient extends AbstractVerticle {
    //部署Verticle时调用
    @Override
    public void start() {
        vertx = VertxReactivexUtils.getVertx();
        WebClient client = VertxReactivexUtils.getWebClient(vertx);

        String host = "https://www.zhipin.com/wapi/zpCommon/data/position.json";
        String requestURI = "/";

        Single<HttpResponse<User>> single = client
                .get(host, requestURI)
                .addQueryParam("param", "param_value")
                .timeout(5000)
                .as(BodyCodec.json(User.class))
                .expect(ResponsePredicate.SC_SUCCESS)
                .expect(ResponsePredicate.JSON)
                .rxSend();

        single.subscribe(response -> {
            // Decode the body as a json object
            User user = response.body();

            log.info("Got HTTP response with status:{}", response.statusCode());
            log.info("getFirstName:{},getLastName:{}", user.getFirstName(), user.getLastName());
        });
        //Single可以多次订阅
        single.subscribe(response -> {
            // Decode the body as a json object
            User user = response.body();

            log.info("Got HTTP response with status:{}", response.statusCode());
            log.info("getFirstName:{},getLastName:{}", user.getFirstName(), user.getLastName());
        });
    }

    //可选 - 在取消部署Verticle时调用
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    /**
     * 异步启动
     *
     * @param startFuture
     * @throws Exception
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
    }

    /**
     * 异步版本的stop方法
     *
     * @param stopFuture
     * @throws Exception
     */
    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }
}
