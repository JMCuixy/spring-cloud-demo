package com.cloud.demo.hystrix.dashboard.service.impl;

import com.cloud.demo.hystrix.dashboard.service.HelloService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author : cuixiuyin
 * @date : 2019/7/3
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "helloFallback")
    public String helloService() {
        // 这里访问的是服务名，而不是一个具体的地址（为了实现负载均衡策略），在服务治理框架中，这是一个非常重要的特性。
        ResponseEntity<String> result = restTemplate.getForEntity("http://cloud-eureka-client/hello", String.class);
        return result.getBody();
    }

    public String helloFallback() {
        return "hytrix breaker";
    }
}
