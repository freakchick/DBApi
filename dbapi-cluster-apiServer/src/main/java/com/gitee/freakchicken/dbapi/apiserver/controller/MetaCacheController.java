package com.gitee.freakchicken.dbapi.apiserver.controller;

import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 集群模式的ehcache多节点同步
 */
@RestController
@Slf4j
public class MetaCacheController {

    @Autowired
    CacheManager cacheManager;

    @RequestMapping("/cache/clean/api")
    public ResponseDto cleanCache(String key) {
        log.info("clean api cache when cluster");
        cacheManager.getCache("api").evictIfPresent(key);
        return ResponseDto.successWithData(null);
    }

    @RequestMapping("/cache/clean/datasource")
    public ResponseDto cleanDatasourceCache(String id) {
        log.info("clean datasource cache when cluster");
        cacheManager.getCache("datasource").evictIfPresent(id);
        PoolManager.removeJdbcConnectionPool(id);
        return ResponseDto.successWithData(null);
    }
}
