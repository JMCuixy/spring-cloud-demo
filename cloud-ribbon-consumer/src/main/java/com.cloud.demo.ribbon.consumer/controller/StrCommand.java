package com.cloud.demo.ribbon.consumer.controller;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author : cuixiuyin
 * @date : 2019/7/8
 * HystrixCommand 的实现有一定的局限性，它返回的 Observable 只能发射一次数据
 */

public class StrCommand extends HystrixCommand<String> {

    private RestTemplate restTemplate;

    public StrCommand(Setter setter, RestTemplate restTemplate) {
        super(setter);
        this.restTemplate = restTemplate;
    }


    @Override
    protected String run() throws Exception {
        ResponseEntity<String> result = restTemplate.getForEntity("http://cloud-eureka-client/hello", String.class);
        return result.getBody();
    }

    // 重写 fallback 用来实现服务的降级处理逻辑
    @Override
    protected String getFallback() {
        return "fallback return";
    }
}
