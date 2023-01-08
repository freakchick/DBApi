package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.CacheMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    CacheMapper cacheMapper;

    public ApiCache getByApiId(String apiId){
        return cacheMapper.selectByApiId(apiId);
    }

}
