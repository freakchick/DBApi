package com.gitee.freakchicken.dbapi.basic.log;

import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "access.log.writer",havingValue = "null")
public class AccessLogNullWriter implements AccessLogWriter{
    @Override
    public void write(AccessLog log) {

    }
}
