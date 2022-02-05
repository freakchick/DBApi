package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.ApiAuthMapper;
import com.gitee.freakchicken.dbapi.basic.dao.TokenMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAuth;
import com.gitee.freakchicken.dbapi.basic.domain.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TokenService {

    @Autowired
    ApiAuthMapper apiAuthMapper;

    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    MetaDataCacheManager metaDataCacheManager;
    @Autowired
    CacheManager cacheManager;

    @Transactional
    @CacheEvict(value = "token_AuthGroups", key = "#tokenId")
    public void auth(Integer tokenId, String groupIds) {
        apiAuthMapper.deleteByTokenId(tokenId);
        if (StringUtils.isNoneBlank(groupIds)) {
            String[] split = groupIds.split(",");
            Arrays.stream(split).forEach(t -> {
                ApiAuth auth = new ApiAuth();
                auth.setTokenId(tokenId);
                auth.setGroupId(t);
                apiAuthMapper.insert(auth);
            });
        }
        metaDataCacheManager.cleanTokenAuthMetaCacheIfCluster(tokenId.toString());
    }

    @Cacheable(value = "token_AuthGroups", key = "#tokenId", unless = "#result == null")
    public List<String> getAuthGroups(Integer tokenId) {
        List<String> list = apiAuthMapper.selectByTokenId(tokenId);
        return list;
    }

    /**
     * 带本地缓存的查询
     * @param tokenStr
     * @return
     */
    @Cacheable(value = "token", key = "#tokenStr", unless = "#result == null")
    public Token getToken(String tokenStr) {
        Token token = tokenMapper.selectByToken(tokenStr);
        return token;
    }

    @Transactional
    public void insert(Token token) {
        tokenMapper.insert(token);
    }

    @Transactional
//    @CacheEvict(value = "token_AuthGroups", key = "#tokenId")
    public void deleteById(Integer tokenId) {
        Token old = tokenMapper.selectById(tokenId);

        tokenMapper.deleteById(tokenId);
        apiAuthMapper.deleteByTokenId(tokenId);

        cacheManager.getCache("token").evictIfPresent(old.getToken());
        cacheManager.getCache("token_AuthGroups").evictIfPresent(tokenId);

        metaDataCacheManager.cleanTokenAuthMetaCacheIfCluster(tokenId.toString());
        metaDataCacheManager.cleanTokenMetaCacheIfCluster(tokenId.toString());

    }
}
