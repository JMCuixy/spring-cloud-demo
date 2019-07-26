package com.cloud.demo.gateway.controller;

import com.cloud.demo.gateway.model.GatewayFilterDefinition;
import com.cloud.demo.gateway.model.GatewayPredicateDefinition;
import com.cloud.demo.gateway.model.GatewayRouteDefinition;
import com.cloud.demo.gateway.service.DynamicRouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * gateway 默认内置了创建动态路由的接口：
 * 要创建一个路由，发送POST请求 /gateway/routes/{id_route_to_create}，参数为JSON结构，具体参数数据结构：
 * {
 *   "id": "first_route",
 *   "predicates": [{
 *     "name": "Path",
 *     "args": {"_genkey_0":"/first"}
 *   }],
 *   "filters": [],
 *   "uri": "http://www.uri-destination.org",
 *   "order": 0
 * }]
 * 要删除一个路由，发送 DELETE请求 /gateway/routes/{id_route_to_delete}
 *
 * 动态路由管理
 */
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    /**
     * 增加路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/add")
    public Boolean add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        try {
            RouteDefinition definition = assembleRouteDefinition(gatewayRouteDefinition);
            return dynamicRouteService.add(definition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 删除路由
     *
     * @param id 路由 id
     * @return
     */
    @GetMapping("/delete/{id}")
    public Boolean delete(@PathVariable String id) {
        return dynamicRouteService.delete(id);
    }

    /**
     * 更新路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/update")
    public Boolean update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition definition = assembleRouteDefinition(gatewayRouteDefinition);
        return dynamicRouteService.update(definition);
    }


    private RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        List<PredicateDefinition> pdList = new ArrayList<>();
        definition.setId(gwdefinition.getId());
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gwdefinition.getPredicates();
        for (GatewayPredicateDefinition gpDefinition : gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }

        List<GatewayFilterDefinition> gatewayFilterDefinitions = gwdefinition.getFilters();
        List<FilterDefinition> filterList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(gatewayFilterDefinitions)) {
            for (GatewayFilterDefinition gatewayFilterDefinition : gatewayFilterDefinitions) {
                FilterDefinition filterDefinition = new FilterDefinition();
                filterDefinition.setName(gatewayFilterDefinition.getName());
                filterDefinition.setArgs(gatewayFilterDefinition.getArgs());
                filterList.add(filterDefinition);
            }
        }
        definition.setPredicates(pdList);
        definition.setFilters(filterList);
        URI uri = UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri()).build().toUri();
        definition.setUri(uri);
        return definition;
    }
}
