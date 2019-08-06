package com.cloud.demo.eureka.client.config;

import brave.sampler.Sampler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义 sleuth 抽样收集策略
 *
 * @author : cuixiuyin
 * @date : 2019/8/5
 */
public class SleuthSamplerConfig extends Sampler {

    private AtomicInteger atomicInteger = new AtomicInteger(0);


    @Override
    public boolean isSampled(long traceId) {
        int increment = atomicInteger.getAndIncrement();
        return increment % 10 == 0;
    }
}
