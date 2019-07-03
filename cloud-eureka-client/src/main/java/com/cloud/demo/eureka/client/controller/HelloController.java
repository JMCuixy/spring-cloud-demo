package com.cloud.demo.eureka.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @author : cuixiuyin
 * @date : 2019/6/23
 */
@RestController
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiscoveryClient discoveryClient;
    @Value("${spring.application.name}")
    private String serviceId;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        // 让处理线程等待几秒钟
        // 由于 Hystrix 默认超时时间为 2000 毫秒， 所以这里采用了 0 至 3000 的随机数以让处理过程有一定概率发生超时来触发断路器。
        int sleepTime = new Random().nextInt(3000);
        logger.info("sleepTime:" + sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServiceInstance instance = instances.get(0);
        logger.info("/hello, host:" + instance.getHost() + ", serviceId:" + instance.getServiceId());
        return "Hello World";
    }
}
