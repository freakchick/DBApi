package com.jq.dbapi.service;

import com.jq.dbapi.dao.ApiAuthMapper;
import com.jq.dbapi.dao.TokenMapper;
import com.jq.dbapi.domain.ApiAuth;
import com.jq.dbapi.domain.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                auth.setGroupId(Integer.valueOf(t));
                apiAuthMapper.insert(auth);
            });
        }

    }

    @Cacheable(value = "token_AuthGroups", key = "#tokenId", unless = "#result == null")
    public List<Integer> getAuthGroups(Integer tokenId) {
        List<Integer> list = apiAuthMapper.selectByTokenId(tokenId);
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
    @CacheEvict(value = "token_AuthGroups", key = "#id")
    public void deleteById(Integer id) {
        tokenMapper.deleteById(id);
    }
}
