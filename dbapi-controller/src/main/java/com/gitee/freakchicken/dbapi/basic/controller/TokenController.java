package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.dao.TokenMapper;
import com.gitee.freakchicken.dbapi.basic.domain.Token;
import com.gitee.freakchicken.dbapi.basic.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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
        tokenService.insert(token);

    }

    @RequestMapping("/getAll")
    public List<Token> getAll() {
        List<Token> list = tokenMapper.selectList(null);
        return list;
    }

    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        tokenService.deleteById(id);

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
    public List<String> getAuthGroups(@PathVariable("tokenId") Integer tokenId) {
        List<String> list = tokenService.getAuthGroups(tokenId);
        return list;
    }
}
