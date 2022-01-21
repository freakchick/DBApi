package com.gitee.freakchicken.dbapi.basic.service;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;


@Slf4j
@Service
public class NacosService {

    @Value("${dbapi.mode:standalone}")
    String mode;

    @Autowired
    private NacosServiceManager nacosServiceManager;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    public String getGatewayAddress() {
        String res = null;
        try {
            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            Instance instance = namingService.selectOneHealthyInstance("DBApi-cluster-gateway");

            res = instance.getIp() + ":" + instance.getPort();
            return res;
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}
