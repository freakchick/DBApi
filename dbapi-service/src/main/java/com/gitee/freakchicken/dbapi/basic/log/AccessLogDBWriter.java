package com.gitee.freakchicken.dbapi.basic.log;

import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import com.gitee.freakchicken.dbapi.basic.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "access.log.writer", havingValue = "db")
public class AccessLogDBWriter implements AccessLogWriter {

    @Autowired
    private AccessLogService accessLogService;

    @Override
    public void write(AccessLog log) {
        accessLogService.insert(log);
    }
}
