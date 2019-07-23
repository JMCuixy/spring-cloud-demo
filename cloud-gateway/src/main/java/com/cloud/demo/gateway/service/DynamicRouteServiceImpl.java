package com.cloud.demo.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author : cuixiuyin
 * @date : 2019/7/23
 */
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    private Logger log = LoggerFactory.getLogger(DynamicRouteServiceImpl.class);

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 增加路由
     *
     * @param definition
     * @return
     */
    public Boolean add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return Boolean.TRUE;
    }


    /**
     * 更新路由
     *
     * @param definition
     * @return
     */
    public Boolean update(RouteDefinition definition) {
        try {
            routeDefinitionWriter.delete(Mono.just(definition.getId()));
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("update fail,not find route  routeId: {}", definition.getId());
        }
        return Boolean.FALSE;
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    public Boolean delete(String id) {
        try {
            routeDefinitionWriter.delete(Mono.just(id));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("delete fail,not find route  routeId: {}", id);
        }
        return Boolean.FALSE;
    }
}
