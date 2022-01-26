package com.gitee.freakchicken.dbapi.basic.service;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;


@Slf4j
@Component
public class NacosService {

//    @Value("${dbapi.mode:standalone}")
//    String mode;

    @Value("${dbapi.cluster.gateway.name}")
    String gatewayName;

    @Autowired
    DiscoveryClient discoveryClient;

//    @Autowired
//    private NacosServiceManager nacosServiceManager;
//    @Autowired
//    private NacosDiscoveryProperties nacosDiscoveryProperties;

    public String getGatewayAddress() {
        String res = null;

//            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
//            Instance instance = namingService.selectOneHealthyInstance(gatewayName);

        List<ServiceInstance> instances = discoveryClient.getInstances(gatewayName);
        res = instances.get(0).getHost() + ":" + instances.get(0).getPort();
        return res;

    }


}
