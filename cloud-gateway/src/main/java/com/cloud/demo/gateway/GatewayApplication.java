package com.cloud.demo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;


@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    /**
     * 限流：
     * <p>
     * 1. Spring Cloud Gateway 默认实现 Redis限流，如果扩展只需要实现 Ratelimter 接口即可。
     * 2. 可以通过自定义KeyResolver来指定限流的Key,比如我们需要根据用户、IP、URI来做限流等等，通过exchange对象可以获取到请求信息。
     *
     * @return
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

}
