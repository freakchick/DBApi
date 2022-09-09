package com.gitee.freakchicken.dbapi.basic.service;


import com.gitee.freakchicken.dbapi.basic.dao.AppInfoMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AppInfo;
import com.gitee.freakchicken.dbapi.basic.domain.AppToken;
import com.gitee.freakchicken.dbapi.basic.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppTokenService {
    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    CacheManager cacheManager;

    public AppToken generateToken(String appId, String secret) {

        //判断APP是否存在
        AppInfo appInfo = appInfoMapper.selectByIdSecret(appId, secret);
        if (appId == null) {
            return null;
        } else {
            String token = RandomStringUtils.random(32, true, true);

            AppToken appToken = new AppToken();

            //单次失效
            if (appInfo.getExpireTime() == 0) {
                appToken.setExpireTime(0l);
            }
            // 永久有效
            else if (appInfo.getExpireTime() == -1) {
                appToken.setExpireTime(-1l);
            }
            // 设置了有效的失效时间
            else if (appInfo.getExpireTime() > 0) {
                appToken.setExpireTime(System.currentTimeMillis() + appInfo.getExpireTime() * 1000);
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
        AppToken appToken = cacheManager.getCache(Constants.EHCACHE_TOKEN_APP).get(token, AppToken.class);
        if (appToken == null) {
            return null;
        } else {
            Long expireTime = appToken.getExpireTime();
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
}