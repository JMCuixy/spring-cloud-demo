package com.cloud.demo.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务降级
 *
 * @author : cuixiuyin
 * @date : 2019/7/18
 */
@RestController
public class FallbackController {


    @GetMapping("/fallback")
    public String fallback() throws JsonProcessingException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", "100");
        resultMap.put("msg", "服务不可用，请稍后再试");
        return new ObjectMapper().writeValueAsString(resultMap);
    }
}
