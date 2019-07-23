package com.cloud.demo.gateway.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.cloud.demo.gateway.config.NacosGatewayProperties;
import com.cloud.demo.gateway.model.GatewayRouteDefinition;
import com.cloud.demo.gateway.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author : cuixiuyin
 * @date : 2019/7/23
 */
@Controller
public class DynamicRouteNacos implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(DynamicRouteNacos.class);

    @Autowired
    private RouteController routeController;

    @Autowired
    private NacosGatewayProperties nacosGatewayProperties;

    @Override
    public void run(String... args) throws Exception {
        dynamicRouteByNacosListener();
    }

    private void dynamicRouteByNacosListener() {
        try {
            ConfigService configService = NacosFactory.createConfigService(nacosGatewayProperties.getAddress());
            String content = configService.getConfig(nacosGatewayProperties.getDataId(), nacosGatewayProperties.getGroupId(),
                    nacosGatewayProperties.getTimeout());
            log.info("dataId:{}, groupId:{}, content:{}", nacosGatewayProperties.getDataId(),
                    nacosGatewayProperties.getGroupId(), content);
            configService.addListener(nacosGatewayProperties.getDataId(), nacosGatewayProperties.getGroupId(), new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    try {
                        List<GatewayRouteDefinition> list = JsonUtils.readListValue(configInfo, GatewayRouteDefinition.class);
                        list.forEach(definition -> routeController.update(definition));
                    } catch (IOException e) {
                        log.error("JsonUtils readListValue error,content:{}", configInfo, e);
                    }
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error("NacosException, dataId:{}, groupId:{}", nacosGatewayProperties.getDataId(),
                    nacosGatewayProperties.getGroupId(), e);
        }
    }


}
