package com.gitee.freakchicken.dbapi.basic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ApiPluginConfigMapper;
import com.gitee.freakchicken.dbapi.common.ApiPluginConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@Slf4j
@Service
@DS("meta-db")
public class ApiPluginConfigService {

    @Autowired
    ApiPluginConfigMapper pluginConfigMapper;

    @Cacheable(value = "api_alarm_config", key = "#apiId", unless = "#result == null")
    public List<ApiPluginConfig> getAlarmPlugins(String apiId){
        return pluginConfigMapper.selectAlarmPlugins(apiId);
    }

    @Cacheable(value = "api_cache_config", key = "#apiId", unless = "#result == null")
    public ApiPluginConfig getCachePlugin(String apiId){
        return pluginConfigMapper.selectCachePlugin(apiId);
    }

    @Cacheable(value = "api_conversion_config", key = "#apiId", unless = "#result == null")
    public ApiPluginConfig getConversionPlugin(String apiId){
        return pluginConfigMapper.selectConversionPlugin(apiId);
    }

}