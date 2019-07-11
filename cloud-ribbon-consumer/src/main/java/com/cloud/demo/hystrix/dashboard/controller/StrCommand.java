package com.cloud.demo.hystrix.dashboard.controller;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author : cuixiuyin
 * @date : 2019/7/8
 * HystrixCommand 的实现有一定的局限性，它返回的 Observable 只能发射一次数据
 */

public class StrCommand extends HystrixCommand<String> {

    private static final HystrixCommandKey COMMAND_KEY = HystrixCommandKey.Factory.
            asKey("strCommand");

    private RestTemplate restTemplate;

    public StrCommand(RestTemplate restTemplate) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("strCommand"))
                .andCommandKey(COMMAND_KEY)
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("strThreadPool")));
        this.restTemplate = restTemplate;
    }

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

    // 1. 设置缓存key。开启缓存功能
    // 2. 当不同的外部请求处理逻辑调用了同一个依赖服务时，Hystrix 会根据 getCacheKey 方法返回的值来区分是否是重复的请求，
    //    如果它们的 cacheKey 相同，那么该依赖服务只会在第一个请求到达时
    //    被真实地调用一次，另外，一个请求则是直接从请求缓存中返回结果。
    @Override
    protected String getCacheKey() {
        return "";
    }

    // 清理缓存
    public void flushCache() {
        HystrixRequestCache hystrixRequestCache = HystrixRequestCache.getInstance(COMMAND_KEY, HystrixConcurrencyStrategyDefault.getInstance());
        hystrixRequestCache.clear("");
    }
}
