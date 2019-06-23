package com.cloud.demo.controller;

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
        ServiceInstance instance = instances.get(0);
        logger.info("/hello, host:" + instance.getHost() + ", serviceId:" + instance.getServiceId());
        return "Hello World";
    }
}
