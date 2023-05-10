package com.gitee.freakchicken.dbapi.basic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ClientAuthMapper;
import com.gitee.freakchicken.dbapi.basic.dao.ClientMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ClientAuth;
import com.gitee.freakchicken.dbapi.basic.domain.ClientToken;
import com.gitee.freakchicken.dbapi.basic.domain.Client;
import com.gitee.freakchicken.dbapi.basic.util.Constants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
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
        String oldToken = cacheManager.getCache(Constants.EHCACHE_CLIENT_TOKEN).get(clientId, String.class);
        if (oldToken != null)
            cacheManager.getCache(Constants.EHCACHE_TOKEN_CLIENT).evict(oldToken);
        cacheManager.getCache(Constants.EHCACHE_CLIENT_TOKEN).evict(clientId);
        cacheManager.getCache(Constants.EHCACHE_CLIENT_AUTH_GROUPS).evict(clientId);
    }

    @Transactional
    public void auth(String clientId, String groupIds) {
        cacheManager.getCache(Constants.EHCACHE_CLIENT_AUTH_GROUPS).evictIfPresent(clientId);
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

    @Cacheable(value = "client_auth_groups", key = "#clientId", unless = "#result == null")
    public List<String> getAuthGroups(String clientId) {
        List<String> list = clientAuthMapper.selectByClientId(clientId);
        return list;
    }

    @Cacheable(value = "app", key = "#clientId", unless = "#result == null")
    public Client getClientInfo(String clientId) {
        Client client = clientMapper.selectById(clientId);
        return client;
    }

    @Transactional
    public ClientToken generateToken(String clientId, String secret) {

        // 判断client是否存在
        Client client = getClientInfo(clientId);
        if (client == null) {
            return null;
        } else {
            // 密钥不对，返回
            if (!secret.equals(client.getSecret())) {
                return null;
            }
            String token = RandomStringUtils.random(32, true, true);

            ClientToken clientToken = new ClientToken();
            clientToken.setToken(token);
            clientToken.setClientId(clientId);

            // 单次失效
            if (client.getExpireDuration() == 0) {
                clientToken.setExpireAt(0l);
            }
            // 永久有效
            else if (client.getExpireDuration() == -1) {
                clientToken.setExpireAt(-1l);

            }
            // 设置了有效的失效时间
            else if (client.getExpireDuration() > 0) {
                long expireAt = System.currentTimeMillis() + client.getExpireDuration() * 1000;
                clientToken.setExpireAt(expireAt);
            }
            
            // 非单次有效的token存入表中，系统重启后可以从表恢复token数据
            if (client.getExpireDuration() != 0) {
                clientMapper.updateTokenById(clientId, clientToken.getToken(), clientToken.getExpireAt());
            }

            // 最新token存入缓存
            cacheManager.getCache(Constants.EHCACHE_TOKEN_CLIENT).putIfAbsent(token, clientToken);

            // clean old token
            String oldToken = cacheManager.getCache(Constants.EHCACHE_CLIENT_TOKEN).get(clientId, String.class);
            if (oldToken != null)
                cacheManager.getCache(Constants.EHCACHE_TOKEN_CLIENT).evict(oldToken);

            // clientId和最新token关系记录下来,便于下次可以找到旧token可以删除，否则缓存中token越来越多
            cacheManager.getCache(Constants.EHCACHE_CLIENT_TOKEN).put(clientId, token);

            return clientToken;
        }
    }

    /**
     * 检查token是否有效，有效就返回token对应的clientId
     *
     * @param token
     * @return
     */
    public String verifyToken(String token) {
        ClientToken clientToken = cacheManager.getCache(Constants.EHCACHE_TOKEN_CLIENT).get(token, ClientToken.class);
        if (clientToken == null) {
            return null;
        } else {
            Long expireTime = clientToken.getExpireAt();
            // 单次失效
            if (expireTime == 0) {
                cacheManager.getCache(Constants.EHCACHE_TOKEN_CLIENT).evict(token);
                return clientToken.getClientId();
            }
            // 永久有效
            else if (expireTime == -1) {
                return clientToken.getClientId();
            }
            // 设置了有效的失效时间
            else if (expireTime > 0) {
                if (expireTime > System.currentTimeMillis()) {
                    return clientToken.getClientId();
                } else {
                    // token已经过期就清除
                    cacheManager.getCache(Constants.EHCACHE_TOKEN_CLIENT).evict(token);
                    log.error("token [{}] expired!", token);
                    throw new RuntimeException("token expired!");
                }
            } else {
                return null;
            }

        }
    }

}