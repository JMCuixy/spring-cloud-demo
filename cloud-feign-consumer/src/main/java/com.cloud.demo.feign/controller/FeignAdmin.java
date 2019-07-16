package com.cloud.demo.feign.controller;

import com.cloud.demo.feign.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cuixiuyin
 * @date : 2019/7/16
 */
@RestController
public class FeignAdmin {

    @Autowired
    private FeignService feignService;

    @RequestMapping(value = "/feign-consumer", method = RequestMethod.GET)
    public String feignHello() {
        return feignService.hello();
    }
}
