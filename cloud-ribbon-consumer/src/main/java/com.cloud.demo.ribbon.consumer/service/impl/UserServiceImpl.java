package com.cloud.demo.ribbon.consumer.service.impl;

import com.cloud.demo.ribbon.consumer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : cuixiuyin
 * @date : 2019/7/9
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public String getUserById(Long id) {
        return restTemplate.getForObject("http://cloud-eureka-client/user/{1}", String.class, 1L);
    }

    @Override
    public Set<String> getUserByIds(List<Long> ids) {
        String collect = ids.stream().map(id -> String.valueOf(id)).collect(Collectors.joining(","));
        return restTemplate.getForObject("http://cloud-eureka-client/user?ids={1}", Set.class, collect);

    }
}
