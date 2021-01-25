package com.jq.dbapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.service.ApiConfigService;
import com.jq.dbapi.util.HttpUtil;
import com.jq.dbapi.util.IPUtil;
import com.jq.dbapi.util.ResponseDto;
import com.jq.dbapi.util.SqlParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
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
    public ResponseDto add(ApiConfig apiConfig) {
        return apiConfigService.add(apiConfig);
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

    @RequestMapping("/getAll")
    public List<ApiConfig> getAll() {
        return apiConfigService.getAll();
    }

    @RequestMapping("/detail/{id}")
    public ApiConfig detail(@PathVariable Integer id) {
        return apiConfigService.detail(id);
    }

    @RequestMapping("/delete/{id}")
    public ApiConfig delete(@PathVariable Integer id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.delete(id, path);
        return null;
    }

    @RequestMapping("/update")
    public ResponseDto update(ApiConfig apiConfig) {
        return apiConfigService.update(apiConfig);
    }

    @RequestMapping("/online/{id}")
    public ApiConfig online(@PathVariable Integer id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.online(id, path);
        return null;
    }

    @RequestMapping("/offline/{id}")
    public ApiConfig offline(@PathVariable Integer id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.offline(id, path);
        return null;
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/getIPPort")
    public String getIPPort() {
        String ip = IPUtil.getIpAddress();
        return ip + ":" + port;
    }

    @RequestMapping("/request")
    public JSONObject request(String url, String params) {
        Map<String, String> map = JSON.parseObject(params, Map.class);
        JSONObject post = HttpUtil.post(url, map);
        return post;
    }

}
