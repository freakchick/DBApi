package com.gitee.freakchicken.dbapi.basic.util;

import com.github.freakchick.orange.engine.DynamicSqlEngine;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-02-24 10:02
 **/
public class SqlEngineUtil {

    static DynamicSqlEngine engine = new DynamicSqlEngine();

    public static DynamicSqlEngine getEngine() {
        return engine;
    }
}
