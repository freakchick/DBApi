package com.gitee.freakchicken.dbapi.basic.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import com.gitee.freakchicken.dbapi.basic.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/access")
public class AccessLogController {

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping("/all")
    public List<AccessLog> getAll() {
        List<AccessLog> all = accessLogService.getAll();
        return all;
    }

    public List<AccessLog> search(String path, String apiId, String appId, long startTime, long endTime, int status) {
        return accessLogService.search(path, apiId, appId, startTime, endTime, status);
    }

    @RequestMapping("/countByDay")
    public JSONArray countByDay(Long start, Long end) {
        return accessLogService.countByDay(start, end);
    }

    @RequestMapping("/top5api")
    public List<JSONObject> top5api( long start,  long end){
        return accessLogService.top5api(start, end);
    }

    @RequestMapping("/top5app")
    public List<JSONObject> top5app(long start, long end){
        return accessLogService.top5app(start, end);
    }

    @RequestMapping("/top5duration")
    public List<JSONObject> top5duration(long start, long end){
        return accessLogService.top5duration(start, end);
    }

    @RequestMapping("/successRatio")
    public JSONObject successRatio(long start, long end){
        return accessLogService.successRatio(start, end);
    }


}
