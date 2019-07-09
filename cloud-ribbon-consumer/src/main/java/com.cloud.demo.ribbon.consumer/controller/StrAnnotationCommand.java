package com.cloud.demo.ribbon.consumer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * @author : cuixiuyin
 * @date : 2019/7/8
 */
@Controller
public class StrAnnotationCommand {

    @Autowired
    private RestTemplate restTemplate;

    // 1. @HystrixCommand 注解相当于新建的 StrCommand 类
    // 2. groupKey 默认是类名，commandKey 默认是方法名 ，threadPoolKey 默认和 groupKey 一致
    @HystrixCommand(fallbackMethod = "fallbackMethod", groupKey = "strGroupCommand", commandKey = "strCommand", threadPoolKey = "strThreadPool")
    public String strConsumer() {
        ResponseEntity<String> result = restTemplate.getForEntity("http://cloud-eureka-client/hello", String.class);
        return result.getBody();
    }

    // ignoreExceptions 表示抛出该异常时不走降级回调逻辑
    @HystrixCommand(fallbackMethod = "fallbackMethod", ignoreExceptions = {IllegalAccessException.class})
    public Future<String> asyncStrConsumer() {

        Future<String> asyncResult = new AsyncResult<String>() {
            @Override
            public String invoke() {
                ResponseEntity<String> result = restTemplate.getForEntity("http://cloud-eureka-client/hello", String.class);
                return result.getBody();
            }
        };
        return asyncResult;
    }

    // 1. 必须定义在一个类中
    // 2. fallbackMethod 的值必须与实现 fallback 方法的名字相同。
    // 3. 对于 fllback 的访问修饰符没有特定的要求， 定义为 private、 protected、 public 均可
    private Future<String> fallbackMethod(Throwable e) {
        Assert.state(e.getMessage().equals("Exception Info"), "tips");
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return "fallback return";
            }
        };
    }
}
