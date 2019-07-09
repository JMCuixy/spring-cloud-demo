package com.cloud.demo.eureka.client.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author : cuixiuyin
 * @date : 2019/7/9
 */
@RestController
public class UserController {

    /**
     * 通过 id 获取用户接口
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}")
    public String getById(@PathVariable Long id){
        return String.valueOf(id);
    }

    /**
     * 通过 ids 批量获取用户信息接口
     * @param ids 以逗号分割的用户 id
     * @return
     */
    @RequestMapping(value = "/user/ids")
    public Set<String> getByIds(@RequestParam String ids){
        return StringUtils.commaDelimitedListToSet(ids);
    }
}
