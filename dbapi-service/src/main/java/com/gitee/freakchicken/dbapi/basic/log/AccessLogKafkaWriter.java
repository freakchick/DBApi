package com.gitee.freakchicken.dbapi.basic.log;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "access.log.writer",havingValue = "kafka")
public class AccessLogKafkaWriter implements AccessLogWriter{
    @Override
    public void write(AccessLog log) {
        System.out.println(JSON.toJSONString(log));
    }
}
