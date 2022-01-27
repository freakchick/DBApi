package com.gitee.freakchicken.dbapi.apiserver.controller;

import com.gitee.freakchicken.dbapi.common.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

    @Autowired
    CacheManager cacheManager;

    @RequestMapping("/cache/clean")
    public ResponseDto cleanCache(String cache, String key) {
        cacheManager.getCache(cache).evictIfPresent(key);
        return ResponseDto.successWithData(null);
    }
}
