package com.cloud.demo.zuul.dynamic.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

/**
 * 1. 作为系统的统一入口，屏蔽了系统内部各个微服务的细节。
 * 2. 可以与服务治理框架相结合，实现自动化的服务实例维护以及负载均衡的路由转发。
 * 3. 可以实现接口权限校验与微服务业务逻辑的解耦
 *
 * @author : cuixiuyin
 * @date : 2019/6/23
 */

// 开启 Zuul 的Api 网关服务功能
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class DynamicRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicRouteApplication.class, args);
    }


    // 该注解来使 zuul 的配置内容动态化
    @RefreshScope
    @ConfigurationProperties("zuul")
    public ZuulProperties zuulProperties() {
        return new ZuulProperties();
    }

}
