package com.gitee.freakchicken.dbapi.basic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.AccessLogMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@DS("access-log-db")
public class AccessLogService {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Transactional
    public void insert(AccessLog log) {
        accessLogMapper.insert(log);
    }

    public List<AccessLog> getAll(){
        return accessLogMapper.selectList(null);
    }

    public List<AccessLog> search(String path, String apiId, String appId, long startTime, long endTime, int status) {
        return null;
    }
}
