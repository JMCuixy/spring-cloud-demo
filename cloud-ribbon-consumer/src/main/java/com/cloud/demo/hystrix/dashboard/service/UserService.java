package com.cloud.demo.hystrix.dashboard.service;

import java.util.List;
import java.util.Set;

/**
 * @author : cuixiuyin
 * @date : 2019/7/9
 */
public interface UserService {


    String getUserById(Long id);

    Set<String> getUserByIds(List<Long> ids);
}
