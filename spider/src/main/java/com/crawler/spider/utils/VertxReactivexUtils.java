package com.crawler.spider.utils;

import com.crawler.spider.webclient.MyCustomException;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.client.predicate.ErrorConverter;
import io.vertx.reactivex.ext.web.client.predicate.ResponsePredicate;
import io.vertx.reactivex.ext.web.client.predicate.ResponsePredicateResult;

import java.util.Objects;
import java.util.function.Function;

/**
 * Vertx使用流畅的api
 * Vertx对象是Vert.x的控制中心,是核心对象
 * Vertx对象是是工厂
 */
public class VertxReactivexUtils {
    /**
     * 大多数应用程序只需要一个Vert.x实例，但如果您需要（例如）
     * 事件总线或不同服务器和客户端组之间的隔离，则可以创建多个Vert.x实例。
     *
     * @return Vertx
     */
    public static Vertx getVertx() {
        return Vertx.vertx();
    }

    /**
     * 在创建Vertx对象时指定选项
     * 创建Vert.x对象时，如果默认值不适合您，还可以指定选项：
     * 该VertxOptions对象具有许多设置，允许您配置群集，高可用性，池大小和各种其他设置等内容。
     *
     * @param workerPoolSize 池大小
     * @return Vertx
     */
    public static Vertx getVertx(int workerPoolSize) {
        workerPoolSize = workerPoolSize >= 1 ? workerPoolSize : 40;
        return Vertx.vertx(new VertxOptions().setWorkerPoolSize(workerPoolSize));
    }

    /**
     * 在创建Vertx对象时指定选项
     * 创建Vert.x对象时，如果默认值不适合您，还可以指定选项：
     * 该VertxOptions对象具有许多设置，允许您配置群集，高可用性，池大小和各种其他设置等内容。
     *
     * @param vertxOptions 选项
     * @return Vertx
     */
    public static Vertx getVertx(VertxOptions vertxOptions) {
        Objects.requireNonNull(vertxOptions);
        return Vertx.vertx(vertxOptions);
    }

    /**
     * 使用默认选项创建实例
     *
     * @param vertx
     * @return
     */
    public static WebClient getWebClient(Vertx vertx) {
        Objects.requireNonNull(vertx);
        return WebClient.create(vertx);
    }

    /**
     * 配置选项
     *
     * @param vertx
     * @param webClientOptions
     * @return
     */
    public static WebClient getWebClient(Vertx vertx, WebClientOptions webClientOptions) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(webClientOptions);

        return WebClient.create(vertx, webClientOptions);
    }

    /**
     * 使用默认选项创建实例
     *
     * @param vertx
     * @return
     */
    public static HttpClient getHttpClient(Vertx vertx) {
        Objects.requireNonNull(vertx);
        return vertx.createHttpClient();
    }

    /**
     * 配置选项
     *
     * @param vertx
     * @param httpClientOptions
     * @return
     */
    public static HttpClient getHttpClient(Vertx vertx, HttpClientOptions httpClientOptions) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(httpClientOptions);

        return vertx.createHttpClient(httpClientOptions);
    }

    /**
     * 当现有谓词不符合您的需求时，还可以创建自定义谓词：
     *
     * @return 自定义谓词
     */
    public static Function<HttpResponse<Void>, ResponsePredicateResult> getMethodsPredicate() {
        return resp -> {
            String methods = resp.getHeader("Access-Control-Allow-Methods");
            if (methods != null) {
                if (methods.contains("POST")) {
                    return ResponsePredicateResult.success();
                }
            }
            return ResponsePredicateResult.failure("Does not work");
        };
    }

    /**
     * 创建自定义故障
     * 默认情况下，响应谓词（包括预定义的谓词）使用默认的错误转换器，它会丢弃正文并传达一条简单的消息。您可以通过更改错误转换器来自定义异常类：
     *
     * @return 谓词
     */
    public static ResponsePredicate handleResponsePredicate() {
        ErrorConverter converter = ErrorConverter.createFullBody(result -> {
            // Invoked after the response body is fully received
            HttpResponse<Buffer> response = result.response();
            if (response.getHeader("content-type").equals("application/json")) {
                // Error body is JSON data
                JsonObject body = response.bodyAsJsonObject();
                return new MyCustomException(body.getString("code"), body.getString("message"));
            }
            // Fallback to defaut message
            return new MyCustomException(result.message());
        });

        return ResponsePredicate.create(ResponsePredicate.SC_SUCCESS, converter);
    }
}
