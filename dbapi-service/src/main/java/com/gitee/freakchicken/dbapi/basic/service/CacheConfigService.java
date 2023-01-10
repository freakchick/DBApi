package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.CacheConfigMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiCacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheConfigService {

    @Autowired
    CacheConfigMapper cacheMapper;

    @Cacheable(value = "api_cache_config", key = "#apiId", unless = "#result == null")
    public ApiCacheConfig getByApiId(String apiId){
        return cacheMapper.selectByApiId(apiId);
    }

}
