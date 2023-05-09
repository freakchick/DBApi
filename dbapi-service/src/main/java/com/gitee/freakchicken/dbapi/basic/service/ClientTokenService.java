package com.gitee.freakchicken.dbapi.basic.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ClientMapper;
import com.gitee.freakchicken.dbapi.basic.domain.Client;
import com.gitee.freakchicken.dbapi.basic.domain.ClientToken;
import com.gitee.freakchicken.dbapi.basic.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@DS("meta-db")
public class ClientTokenService {
    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    CacheManager cacheManager;

    @Cacheable(value = "app", key = "#clientId", unless = "#result == null")
    public Client getClientInfo(String clientId) {
        Client client = clientMapper.selectById(clientId);
        return client;
    }

    @Transactional
    public ClientToken generateToken(String clientId, String secret) {

        //判断client是否存在
        Client client = getClientInfo(clientId);

        if (client == null) {
            return null;
        } else {
            //密钥不对，返回
            if (!secret.equals(client.getSecret())) {
                return null;
            }
            String token = RandomStringUtils.random(32, true, true);

            ClientToken clientToken = new ClientToken();

            //单次失效
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
            clientToken.setToken(token);
            clientToken.setClientId(clientId);

            //最新token存入缓存
            cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).putIfAbsent(token, clientToken);

            //clean old token
            String oldToken = cacheManager.getCache(Constants.EHCACHE_APP_TOKEN).get(clientId, String.class);
            if (oldToken != null)
                cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).evict(oldToken);

            //clientId和最新token关系记录下来,便于下次可以找到旧token可以删除，否则缓存中token越来越多
            cacheManager.getCache(Constants.EHCACHE_APP_TOKEN).put(clientId, token);

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
        ClientToken clientToken = cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).get(token, ClientToken.class);
        if (clientToken == null) {
            return null;
        } else {
            Long expireTime = clientToken.getExpireAt();
            // 单次失效
            if (expireTime == 0) {
                cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).evict(token);
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
                    cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).evict(token);
                    log.error("token [{}] expired!", token);
                    throw new RuntimeException("token expired!");
                }
            } else {
                return null;
            }

        }
    }

}