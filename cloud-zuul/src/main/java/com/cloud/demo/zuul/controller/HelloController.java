package com.cloud.demo.zuul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author : cuixiuyin
 * @date : 2019/7/18
 */
@RestController
public class HelloController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/local/hello")
    public String hello() {
        return restTemplate.getForObject("http://cloud-eureka-client/hello", String.class);
    }
}
