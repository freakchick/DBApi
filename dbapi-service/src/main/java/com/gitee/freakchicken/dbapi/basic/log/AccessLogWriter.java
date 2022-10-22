package com.gitee.freakchicken.dbapi.basic.log;

import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;

public interface AccessLogWriter {

    void write(AccessLog log);
}
