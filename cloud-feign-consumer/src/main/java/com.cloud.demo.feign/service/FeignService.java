package com.cloud.demo.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : cuixiuyin
 * @date : 2019/7/16
 */
@FeignClient(value = "cloud-eureka-client")
public interface FeignService {


    @RequestMapping("/hello")
    String hello();
}
