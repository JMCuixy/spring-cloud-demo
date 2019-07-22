package com.cloud.demo.zuul.dynamic.filter;

import com.cloud.demo.zuul.dynamic.filter.config.FilterConfiguration;
import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;


// 开启 Zuul 的Api 网关服务功能
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties
public class DynamicFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicFilterApplication.class, args);
    }

    /**
     * 根据上面的定义，API 网关应用会每隔 5 秒，从 API 网关服务所在位置的 filter/pre 和 filter/post 目录下获取 Groovy 定义的过滤器，
     * 并对其进行编译和动态加载使用。
     *
     * @param filterConfiguration
     * @return
     */
    @Bean
    public FilterLoader filterLoader(FilterConfiguration filterConfiguration) {
        FilterLoader filterLoader = FilterLoader.getInstance();
        filterLoader.setCompiler(new GroovyCompiler());
        try {
            FilterFileManager.setFilenameFilter(new GroovyFileFilter());
            FilterFileManager.init(
                    filterConfiguration.getInterval(),
                    filterConfiguration.getRoot() + "/pre",
                    filterConfiguration.getRoot() + "/post");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return filterLoader;
    }
}
