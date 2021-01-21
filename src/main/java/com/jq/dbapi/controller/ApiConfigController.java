package com.jq.dbapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.service.ApiConfigService;
import com.jq.dbapi.util.SqlParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@RestController
@Slf4j
@RequestMapping("/apiConfig")
public class ApiConfigController {

    @Autowired
    ApiConfigService apiConfigService;

    @RequestMapping("/add")
    public void add(ApiConfig apiConfig) {
        apiConfigService.add(apiConfig);
    }

    @RequestMapping("/parseParam")
    public List<JSONObject> parseParam(String sql) {
        List<String> requestParam = SqlParser.getRequestParam(sql);
        List<JSONObject> collect = requestParam.stream().map(t -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", t);
            jsonObject.put("type", "string");
            return jsonObject;
        }).collect(Collectors.toList());
        return collect;
    }

}
