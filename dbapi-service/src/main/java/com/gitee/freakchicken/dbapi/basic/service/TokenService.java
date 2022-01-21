package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.ApiAuthMapper;
import com.gitee.freakchicken.dbapi.basic.dao.TokenMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAuth;
import com.gitee.freakchicken.dbapi.basic.domain.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    }

    @Cacheable(value = "token_AuthGroups", key = "#tokenId", unless = "#result == null")
    public List<String> getAuthGroups(Integer tokenId) {
        List<String> list = apiAuthMapper.selectByTokenId(tokenId);
        return list;
    }

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
    @CacheEvict(value = "token_AuthGroups", key = "#tokenId")
    public void deleteById(Integer tokenId) {
        tokenMapper.deleteById(tokenId);
    }
}
