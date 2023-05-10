package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.ClientMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ClientToken;
import com.gitee.freakchicken.dbapi.basic.util.Constants;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoadPluginOnSpringReady {
    @Autowired
    CacheManager cacheManager;

    @Autowired
    ClientMapper clientMapper;

    @EventListener
    public void load(ApplicationReadyEvent event) {
        PluginManager.loadPlugins();
        loadToken();
    }

    public void loadToken() {
        List<ClientToken> allToken = clientMapper.getAllToken();
        for (ClientToken clientToken : allToken) {
            cacheManager.getCache(Constants.EHCACHE_TOKEN_CLIENT).putIfAbsent(clientToken.getToken(), clientToken);
            // clientId和最新token关系记录下来,便于下次可以找到旧token可以删除，否则缓存中token越来越多
            cacheManager.getCache(Constants.EHCACHE_CLIENT_TOKEN).put(clientToken.getClientId(), clientToken.getToken());
        }
    }

}