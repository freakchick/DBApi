package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import com.gitee.freakchicken.dbapi.basic.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public List<Map<String,Object>> countByDay(String start, String end) {
        return accessLogService.countByDay(start, end);
    }
}
