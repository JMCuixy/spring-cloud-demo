package com.cloud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : cuixiuyin
 * @date : 2019/6/23
 */
// 自动化配置， 创建 DiscoveryClient 接口针对 Eureka 客户端的 EurekaDiscoveryClient 实例
@EnableDiscoveryClient
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
