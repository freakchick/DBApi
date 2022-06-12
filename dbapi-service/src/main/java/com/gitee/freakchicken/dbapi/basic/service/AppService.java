package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.ApiAuthMapper;
import com.gitee.freakchicken.dbapi.basic.dao.AppInfoMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAuth;
import com.gitee.freakchicken.dbapi.basic.domain.AppInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class AppService {

    @Autowired
    private AppInfoMapper appInfoMapper;
    @Autowired
    private ApiAuthMapper apiAuthMapper;

    @Autowired
    private MetaDataCacheManager metaDataCacheManager;

    @Transactional
    public AppInfo add(AppInfo app) {
        app.setId(RandomStringUtils.random(16, true, true));
        app.setSecret(RandomStringUtils.random(32, true, true));
        if (app.getExpireDesc().equals("5min")) {
            app.setExpireTime(5 * 60);
        } else if (app.getExpireDesc().equals("1hour")) {
            app.setExpireTime(60 * 60);
        } else if (app.getExpireDesc().equals("1day")) {
            app.setExpireTime(60 * 60 * 24);
        } else if (app.getExpireDesc().equals("30day")) {
            app.setExpireTime(60 * 60 * 24 * 30);
        } else if (app.getExpireDesc().equals("单次有效")) {
            app.setExpireTime(0);
        } else if (app.getExpireDesc().equals("永久有效")) {
            app.setExpireTime(-1);
        }

        appInfoMapper.insert(app);
        return app;
    }

    public List<AppInfo> getAll() {
        return appInfoMapper.selectByMap(null);
    }

    @Transactional
    public void delete(String appid) {
        appInfoMapper.deleteById(appid);
    }

    @Transactional
    @CacheEvict(value = "app_AuthGroups", key = "#appId")
    public void auth(String appId, String groupIds) {
        apiAuthMapper.deleteByAppId(appId);
        if (StringUtils.isNoneBlank(groupIds)) {
            String[] split = groupIds.split(",");
            Arrays.stream(split).forEach(t -> {
                ApiAuth auth = new ApiAuth();
                auth.setAppId(appId);
                auth.setGroupId(t);
                apiAuthMapper.insert(auth);
            });
        }
        metaDataCacheManager.cleanTokenAuthMetaCacheIfCluster(appId);
    }

    @Cacheable(value = "app_AuthGroups", key = "#appId", unless = "#result == null")
    public List<String> getAuthGroups(String appId) {
        List<String> list = apiAuthMapper.selectByAppId(appId);
        return list;
    }
}