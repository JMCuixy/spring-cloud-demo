package com.cloud.demo.feign.service;

import com.cloud.demo.feign.config.DisableHystrixConfig;
import com.cloud.demo.feign.service.fallback.FeignServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : cuixiuyin
 * @date : 2019/7/16
 */
@FeignClient(value = "cloud-eureka-client", configuration = DisableHystrixConfig.class, fallback = FeignServiceFallback.class)
public interface FeignService {


    @RequestMapping("/hello")
    String hello();
}
