package com.cloud.demo.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1.
 * Spring Cloud Feign 基于 Netflix Feign 实现，整合了 Spring Cloud ribbon 与 Spring Cloud Hystrix, 除了提供这两者的强大功能之外，
 * 它还提供了一种声明式的 Web 服务客户端定义方式。
 * 2.
 * Spring Cloud Feign 具备可插拔的注解支持，包括 Feign 注解和 JAX-RS 注解。 同时，为了适应 Spring 的广大用户，它在 Netflix Feign
 * 的基础上扩展了对 Spring MVC 的注解支待。
 * 3.
 * Spring Cloud Feign 对于参数绑定，只需按照 Spring Mvc 的注解即可。区别是：在定义各参数绑定时，@RequestParam、@RequestHeader
 * 等可以指定参数名称的注解，它们的 value 千万不能少。在 SpringMVC 程序中，这些注解会根据参数名来作为默认值，但是在 Feign 中绑定
 * 参数必须通过 value 属性来指明具体的参数名，
 *
 * @author : cuixiuyin
 * @date : 2019/6/23
 */
// 开启 Spring Cloud Feign 的支持功能
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class FeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class, args);
    }

}
