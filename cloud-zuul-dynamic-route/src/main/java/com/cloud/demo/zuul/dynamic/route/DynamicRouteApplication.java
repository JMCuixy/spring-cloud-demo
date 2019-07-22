package com.cloud.demo.zuul.dynamic.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


// 开启 Zuul 的Api 网关服务功能
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class DynamicRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicRouteApplication.class, args);
    }


    /**
     * 刷新地址：POST http://127.0.0.1:5006/actuator/refresh
     * 路由查看地址：GET http://127.0.0.1:5006/actuator/routes
     *
     * @return
     */
    @Bean
    @Primary
    //该注解来使 zuul 的配置内容动态化
    @RefreshScope
    @ConfigurationProperties(prefix = "zuul")
    public ZuulProperties zuulProperties() {
        return new ZuulProperties();
    }

}
