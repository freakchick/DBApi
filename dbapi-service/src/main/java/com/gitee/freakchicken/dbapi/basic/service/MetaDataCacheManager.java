package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

@Component
@Slf4j
public class MetaDataCacheManager {

    @Value("${dbapi.cluster.api.name}")
    String apiName;

    @Value("${dbapi.cluster.gateway.name}")
    String gatewayName;

    @Value("${dbapi.mode:cluster}")
    String mode;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    DiscoveryClient discoveryClient;

    public void cleanApiMetaCacheIfCluster(String key) {
        try {
            if (mode.equals("cluster")) {
                RestTemplate restTemplate = new RestTemplate();
                List<ServiceInstance> instances = discoveryClient.getInstances(apiName);

                for (ServiceInstance instance : instances) {
                    String url = String.format("http://%s:%s/metacache/clean/api?key=%s", instance.getHost(), instance.getPort(), key);
                    restTemplate.getForEntity(url, ResponseDto.class);
                    log.info("api meta cache clean to node: {}", instance.getHost());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void cleanDatasourceMetaCacheIfCluster(String id) {
        try {
            if (mode.equals("cluster")) {
                RestTemplate restTemplate = new RestTemplate();
                List<ServiceInstance> instances = discoveryClient.getInstances(apiName);

                for (ServiceInstance instance : instances) {
                    String url = String.format("http://%s:%s/metacache/clean/datasource?id=%s", instance.getHost(), instance.getPort(), id);
                    restTemplate.getForEntity(url, ResponseDto.class);
                    log.info("datasource meta cache clean to node: {}", instance.getHost());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void cleanTokenAuthMetaCacheIfCluster(String tokenId) {
        try {
            if (mode.equals("cluster")) {
                RestTemplate restTemplate = new RestTemplate();
                List<ServiceInstance> instances = discoveryClient.getInstances(apiName);

                for (ServiceInstance instance : instances) {
                    String url = String.format("http://%s:%s/metacache/clean/tokenAuth?id=%s", instance.getHost(), instance.getPort(), tokenId);
                    restTemplate.getForEntity(url, ResponseDto.class);
                    log.info("token auth meta cache clean to node: {}", instance.getHost());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void cleanTokenMetaCacheIfCluster(String tokenId) {
        try {
            if (mode.equals("cluster")) {
                RestTemplate restTemplate = new RestTemplate();
                List<ServiceInstance> instances = discoveryClient.getInstances(apiName);

                for (ServiceInstance instance : instances) {
                    String url = String.format("http://%s:%s/metacache/clean/token?id=%s", instance.getHost(), instance.getPort(), tokenId);
                    restTemplate.getForEntity(url, ResponseDto.class);
                    log.info("token meta cache clean to node: {}", instance.getHost());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void gatewayIPRuleCacheSyncIfCluster() {

        if (mode.equals("cluster")) {
            RestTemplate restTemplate = new RestTemplate();
            List<ServiceInstance> instances = discoveryClient.getInstances(gatewayName);

            for (ServiceInstance instance : instances) {
                String url = String.format("http://%s:%s/metacache/iprule/sync", instance.getHost(), instance.getPort());
                restTemplate.getForEntity(url, ResponseDto.class);
                log.info("sync ip rule cache to gateway node: {}", instance.getHost());
            }
        }

    }

}
