package com.cloud.demo.hystrix.dashboard.controller;

import com.cloud.demo.hystrix.dashboard.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author : cuixiuyin
 * @date : 2019/6/23
 */
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HelloService helloService;

    @RequestMapping("/ribbon-consumer")
    public String helloConsumer() {
        // 这里访问的是服务名，而不是一个具体的地址（为了实现负载均衡策略），在服务治理框架中，这是一个非常重要的特性。
        ResponseEntity<String> result = restTemplate.getForEntity("http://cloud-eureka-client/hello", String.class);
        return result.getBody();
    }

    @RequestMapping("/hytrix-consumer")
    public String hytrixConsumer() {
        return helloService.helloService();
    }
}
