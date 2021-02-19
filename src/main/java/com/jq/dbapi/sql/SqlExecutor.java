package com.jq.dbapi.sql;

import lombok.Data;

import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-02-19 17:49
 **/
@Data
public class SqlExecutor {
    String sql;
    List<Object> jdbcParamValues;

    public SqlExecutor(String sql, List<Object> jdbcParamValues) {
        this.sql = sql;
        this.jdbcParamValues = jdbcParamValues;
    }
}
