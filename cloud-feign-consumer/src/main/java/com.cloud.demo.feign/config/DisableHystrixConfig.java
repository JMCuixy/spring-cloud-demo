package com.cloud.demo.feign.config;

import feign.Feign;
import feign.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 禁用某个依赖服务的 Hystrix 功能
 *
 * @author : cuixiuyin
 * @date : 2019/7/16
 */
@Configuration
public class DisableHystrixConfig {


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Feign.Builder feignBuilder() {
        return new Feign.Builder();
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }
}
