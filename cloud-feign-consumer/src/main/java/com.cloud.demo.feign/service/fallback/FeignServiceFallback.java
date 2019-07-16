package com.cloud.demo.feign.service.fallback;

import com.cloud.demo.feign.service.FeignService;
import org.springframework.stereotype.Component;

/**
 * @author : cuixiuyin
 * @date : 2019/7/16
 */
@Component
public class FeignServiceFallback implements FeignService {

    @Override
    public String hello() {
        return "error";
    }
}
