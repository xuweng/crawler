package com.crawler.spider.utils;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Vertx使用流畅的api
 * Vertx对象是Vert.x的控制中心,是核心对象
 * Vertx对象是是工厂
 */
public class VertxUtils {
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
        return Vertx.vertx(vertxOptions);
    }
}
