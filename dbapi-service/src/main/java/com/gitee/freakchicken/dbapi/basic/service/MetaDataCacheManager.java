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

import java.util.List;

@Component
@Slf4j
public class MetaDataCacheManager {

    @Value("${dbapi.cluster.api.name}")
    String apiName;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    DiscoveryClient discoveryClient;

//    @Autowired



    public void cleanMetaDataCache(String cache, String key) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances(apiName);

            for (ServiceInstance instance : instances) {
                String url = String.format("http://%s:%s/cache/clean?cache=%s&key=%s", instance.getHost(), instance.getPort(), cache, key);
                restTemplate.getForEntity(url, ResponseDto.class);
                log.info("meta cache clean "+ instance.getHost());
            }
        } catch (Exception e) {

        }
    }
}
