package com.gitee.freakchicken.dbapi.basic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ClientAuthMapper;
import com.gitee.freakchicken.dbapi.basic.dao.ClientMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ClientAuth;
import com.gitee.freakchicken.dbapi.basic.domain.Client;
import com.gitee.freakchicken.dbapi.basic.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@DS("meta-db")
public class ClientService {

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ClientAuthMapper clientAuthMapper;

    @Autowired
    CacheManager cacheManager;

    @Transactional
    public Client add(Client client) {
        client.setId(RandomStringUtils.random(16, true, true));
        client.setSecret(RandomStringUtils.random(32, true, true));
        if (client.getExpireDesc().equals("5min")) {
            client.setExpireDuration(5 * 60);
        } else if (client.getExpireDesc().equals("1hour")) {
            client.setExpireDuration(60 * 60);
        } else if (client.getExpireDesc().equals("1day")) {
            client.setExpireDuration(60 * 60 * 24);
        } else if (client.getExpireDesc().equals("30day")) {
            client.setExpireDuration(60 * 60 * 24 * 30);
        } else if (client.getExpireDesc().equals("once")) {
            client.setExpireDuration(0);
        } else if (client.getExpireDesc().equals("forever")) {
            client.setExpireDuration(-1);
        }

        clientMapper.insert(client);
        return client;
    }

    public List<Client> getAll() {
        return clientMapper.selectByMap(null);
    }

    @Transactional
    public void delete(String clientId) {
        clientMapper.deleteById(clientId);
        String oldToken = cacheManager.getCache(Constants.EHCACHE_APP_TOKEN).get(clientId, String.class);
        if (oldToken != null)
            cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).evict(oldToken);
        cacheManager.getCache(Constants.EHCACHE_APP_TOKEN).evict(clientId);
        cacheManager.getCache(Constants.EHCACHE_APP_AUTH_GROUPS).evict(clientId);
    }

    @Transactional
    public void auth(String clientId, String groupIds) {
        cacheManager.getCache(Constants.EHCACHE_APP_AUTH_GROUPS).evictIfPresent(clientId);
        clientAuthMapper.deleteByClientId(clientId);
        if (StringUtils.isNoneBlank(groupIds)) {
            String[] split = groupIds.split(",");
            Arrays.stream(split).forEach(t -> {
                ClientAuth auth = new ClientAuth();
                auth.setClientId(clientId);
                auth.setGroupId(t);
                clientAuthMapper.insert(auth);
            });
        }
    }

    @Cacheable(value = "app_AuthGroups", key = "#clientId", unless = "#result == null")
    public List<String> getAuthGroups(String clientId) {
        List<String> list = clientAuthMapper.selectByClientId(clientId);
        return list;
    }
}