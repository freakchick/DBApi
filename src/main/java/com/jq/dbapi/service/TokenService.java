package com.jq.dbapi.service;

import com.jq.dbapi.dao.ApiAuthMapper;
import com.jq.dbapi.dao.TokenMapper;
import com.jq.dbapi.domain.ApiAuth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    ApiAuthMapper apiAuthMapper;

    @Transactional
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

    public List<Integer> getAuthGroups(Integer tokenId) {

        List<Integer> list = apiAuthMapper.selectByTokenId(tokenId);
        return list;
    }
}
