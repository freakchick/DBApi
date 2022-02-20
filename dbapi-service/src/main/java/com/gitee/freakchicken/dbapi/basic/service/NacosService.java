package com.gitee.freakchicken.dbapi.basic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class NacosService {

    @Value("${dbapi.cluster.gateway.name}")
    String gatewayName;

    @Autowired
    DiscoveryClient discoveryClient;

    public String getGatewayAddress() {
        String res = null;
        List<ServiceInstance> instances = discoveryClient.getInstances(gatewayName);
        res = instances.get(0).getHost() + ":" + instances.get(0).getPort();
        return res;

    }


}
