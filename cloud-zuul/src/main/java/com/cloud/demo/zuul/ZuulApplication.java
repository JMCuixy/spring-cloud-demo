package com.cloud.demo.zuul;

import com.cloud.demo.zuul.config.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 1. 作为系统的统一入口，屏蔽了系统内部各个微服务的细节。
 * 2. 可以与服务治理框架相结合，实现自动化的服务实例维护以及负载均衡的路由转发。
 * 3. 可以实现接口权限校验与微服务业务逻辑的解耦
 *
 * @author : cuixiuyin
 * @date : 2019/6/23
 */

// 开启 Zuul 的Api 网关服务功能
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }

    /**
     * 修改默认的路由规则。默认：以服务名作为前缀配置路由
     * <p>
     * 1. 构造函数的第一个参数是用来匹配服务名称是否符合该自定义规则的正则表达式，而第二个参数则是定义根据服务名中定义的内容转换出
     * 的路径表达式。
     * 2. 只要符合第一个参数定义规则的服务名，都会优先使用该实现构建出的路径表达式，如果没有匹配上的服务则还是会使用默认的路由映射
     * 规则，即采用完整服务名作为前缀的路径表达式。
     *
     * @return
     */
    @Bean
    public PatternServiceRouteMapper serviceRouteMapper() {
        return new PatternServiceRouteMapper(
                "(?<name> A .+)-(?<version>v.+$)",
                "${version}/${name}");
    }

}
