package com.cloud.demo.config.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cuixiuyin
 * @date : 2019/7/21
 */
//该注解来使配置内容动态化
@RefreshScope
@RestController
public class ConfigClientAdmin {

    @Value("${from:from}")
    private String from;

    @Autowired
    private Environment environment;


    @RequestMapping("/from")
    public String from() {
        String fromEnv = environment.getProperty("from");
        return from + "_" + fromEnv;
    }
}
