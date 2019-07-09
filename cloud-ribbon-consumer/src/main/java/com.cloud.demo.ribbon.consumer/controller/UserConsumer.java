package com.cloud.demo.ribbon.consumer.controller;

import com.cloud.demo.ribbon.consumer.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author : cuixiuyin
 * @date : 2019/7/9
 */
@RestController
public class UserConsumer {

    @Autowired
    private UserService userService;

    /**
     * 通过 id 获取用户接口
     * @param id
     * @return
     */
    @HystrixCollapser(batchMethod= "getByIds", collapserProperties= {
            @HystrixProperty(name="timerDelayInMilliseconds", value = "100")
    })
    public String getById(Long id){
        return userService.getUserById(id);
    }

    /**
     * 通过 ids 批量获取用户信息接口
     * @param ids 以逗号分割的用户 id
     * @return
     */
    @HystrixCommand
    public Set<String> getByIds(List<Long> ids){
        return userService.getUserByIds(ids);
    }
}
