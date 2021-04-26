package com.jq.dbapi.controller;

import com.jq.dbapi.dao.TokenMapper;
import com.jq.dbapi.domain.Token;
import com.jq.dbapi.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    TokenService tokenService;

    @RequestMapping("/add")
    public void add(Token token) {
        token.setCreateTime(System.currentTimeMillis());
        tokenMapper.insert(token);
    }

    @RequestMapping("/getAll")
    public List<Token> getAll() {
        List<Token> list = tokenMapper.selectList(null);
        return list;
    }

    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        tokenMapper.deleteById(id);
    }

    @RequestMapping("/generate")
    public String generate() {
        String token = DigestUtils.md5Hex(UUID.randomUUID().toString());
        return token;
    }

    @RequestMapping("/auth")
    public void auth(Integer tokenId, String groupIds) {
        tokenService.auth(tokenId, groupIds);
    }

    @RequestMapping("/getAuthGroups/{tokenId}")
    public List<Integer> getAuthGroups(@PathVariable("tokenId") Integer tokenId) {
        List<Integer> list = tokenService.getAuthGroups(tokenId);
        return list;
    }
}
