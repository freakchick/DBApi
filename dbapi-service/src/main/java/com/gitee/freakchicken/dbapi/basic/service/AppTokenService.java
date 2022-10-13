package com.gitee.freakchicken.dbapi.basic.service;


import com.gitee.freakchicken.dbapi.basic.dao.AppInfoMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AppInfo;
import com.gitee.freakchicken.dbapi.basic.domain.AppToken;
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
public class AppTokenService {
    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    CacheManager cacheManager;

    @Cacheable(value = "app", key = "#appId", unless = "#result == null")
    public AppInfo getAppinfo(String appId) {
        AppInfo appInfo = appInfoMapper.selectById(appId);
        return appInfo;
    }

    @Transactional
    public AppToken generateToken(String appId, String secret) {

        //判断APP是否存在
        AppInfo appInfo = getAppinfo(appId);


        if (appInfo == null) {
            return null;
        } else {
            //密钥不对，返回
            if (!secret.equals(appInfo.getSecret())) {
                return null;
            }
            String token = RandomStringUtils.random(32, true, true);
//            appInfo.setToken(token);

            AppToken appToken = new AppToken();

            //单次失效
            if (appInfo.getExpireDuration() == 0) {
                appToken.setExpireAt(0l);
            }
            // 永久有效
            else if (appInfo.getExpireDuration() == -1) {
                appToken.setExpireAt(-1l);
            }
            // 设置了有效的失效时间
            else if (appInfo.getExpireDuration() > 0) {
                long expireAt = System.currentTimeMillis() + appInfo.getExpireDuration() * 1000;
                appToken.setExpireAt(expireAt);
//                appInfo.setExpireAt(expireAt);
            }
            appToken.setToken(token);
            appToken.setAppId(appId);


            //最新token存入缓存
            cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).putIfAbsent(token, appToken);

            //clean old token
            String oldToken = cacheManager.getCache(Constants.EHCACHE_APP_TOKEN).get(appId, String.class);
            if (oldToken != null)
                cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).evict(oldToken);

            //appid和最新token关系记录下来
            cacheManager.getCache(Constants.EHCACHE_APP_TOKEN).putIfAbsent(appId, token);

            // token和失效时间存入app表
//            appInfoMapper.updateById(appInfo);

            return appToken;
        }
    }

    /**
     * 检查token是否有效，有效就返回token对应的appId
     *
     * @param token
     * @return
     */
    public String verifyToken(String token) {
        AppToken appToken = getAppToken(token);
        if (appToken == null) {
            return null;
        } else {
            Long expireTime = appToken.getExpireAt();
            // 单次失效
            if (expireTime == 0) {
                cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).evict(token);
                return appToken.getAppId();
            }
            // 永久有效
            else if (expireTime == -1) {
                return appToken.getAppId();
            }
            // 设置了有效的失效时间
            else if (expireTime > 0) {
                if (expireTime > System.currentTimeMillis()) {
                    return appToken.getAppId();
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

    /**
     * 先查缓存，查不到再查数据库，如果数据库有存入缓存
     *
     * @param token
     * @return
     */
    public AppToken getAppToken(String token) {
        AppToken appToken = cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).get(token, AppToken.class);
        if (appToken == null) {
            AppInfo appInfo = appInfoMapper.selectByToken(token);
            if (appInfo != null) {
                AppToken appToken1 = new AppToken();
                appToken1.setAppId(appInfo.getId());
                appToken1.setToken(appInfo.getToken());
                appToken1.setExpireAt(appInfo.getExpireAt());
                cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).putIfAbsent(token, appToken1);
                return appToken1;
            } else {
                return null;
            }
        } else {
            return appToken;
        }

    }
}