package com.cloud.demo.gateway.config;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.springframework.cloud.gateway.filter.factory.FallbackHeadersGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : cuixiuyin
 * @date : 2019/7/24
 */
@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        // 得到访问地址的两种方式
        // 1. 返回一个RemoteAddressResolver，它始终采用X-Forwarded-for头中找到的第一个IP地址。这种方法容易受到欺骗，因为恶意客户端可能会为解析程序接受的“x-forwarded-for”设置初始值。
        Supplier<XForwardedRemoteAddressResolver> trustAll = XForwardedRemoteAddressResolver::trustAll;
        // 2. 获取一个索引，该索引与在Spring Cloud网关前运行的受信任基础设施数量相关
        Function<Integer, XForwardedRemoteAddressResolver> maxTrustedIndex = XForwardedRemoteAddressResolver::maxTrustedIndex;
        return builder.routes()
                .route(r -> r.path("/foo/{segment}")
                        .filters(f -> f.stripPrefix(1)
                                // 在请求头上加上信息
                                .addRequestHeader("X-Request-Foo", "Bar")
                                // 在请求参数上加上信息
                                .addRequestParameter("foo", "bar")
                                // 在响应头上加上信息
                                .addResponseHeader("X-Response-Foo", "bar")
                                // 断路器配置
                                .hystrix(h -> h.setName("myCommandName").setFallbackUri("forward:/fallback"))
                                // fallback 增加头部异常信息
                                .fallbackHeaders(FallbackHeadersGatewayFilterFactory.Config::getCauseExceptionMessageHeaderName)
                                // 为所有请求增加前缀
                                .prefixPath("/")
                                // 设置了该 Filter 后，GatewayFilter 将不使用由 HTTP 客户端确定的host header ，而是发送原始host header。
                                .preserveHostHeader()
                                // header 被发送到下游之前删除它
                                .removeRequestHeader("X-Request-Foo")
                                // 这将在返回到网关 client之前从响应中删除 x-response-foo 头。
                                .removeResponseHeader("X-Response-Foo")
                                // 将调用转发到下游之前强制执行 WebSession::save 操作。
                                .saveSession()
                                // 对于一个 /foo/bar请求，在做下游请求前，路径将被设置为 /bar
                                .setPath("/{segment}")
                                // 强制重写头部值
                                .setResponseHeader("X-Response-Foo", "bar")
                                // parts 参数指示在将请求发送到下游之前，要从请求中去除的路径中的节数。
                                .stripPrefix(1)
                                // 请求大小限制，单位 B
                                .setRequestSize(5000000L)

                        )
                        .uri("http://localhost:2222")
                ).build();
    }
}
