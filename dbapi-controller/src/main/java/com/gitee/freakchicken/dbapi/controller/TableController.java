package com.gitee.freakchicken.dbapi.controller;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.basic.dao.DataSourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-04-01 15:11
 **/
@Slf4j
@RestController
@RequestMapping("/table")
public class TableController {

    @Autowired
    DataSourceMapper dataSourceMapper;

    @RequestMapping("/getAllTables")
    public List<JSONObject> getAllTables(String sourceId) throws SQLException {
        DataSource dataSource = dataSourceMapper.selectById(sourceId);
        DruidPooledConnection connection = PoolManager.getPooledConnection(dataSource);
        List<String> tables = JdbcUtil.getAllTables(connection, dataSource.getTableSql());
        List<JSONObject> list = tables.stream().map(t -> {
            JSONObject jo = new JSONObject();
            jo.put("label", t);
            try {
                DruidPooledConnection conn = PoolManager.getPooledConnection(dataSource);
                jo.put("columns", JdbcUtil.getRDBMSColumnProperties(conn, dataSource.getType(), t));
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            jo.put("columns",);
            jo.put("showColumns", false);
            return jo;
        }).collect(Collectors.toList());
        return list;
    }

    @RequestMapping("/getAllColumns")
    public List<JSONObject> getAllTables(String sourceId, String table) throws SQLException {
        DataSource dataSource = dataSourceMapper.selectById(sourceId);
        DruidPooledConnection connection = PoolManager.getPooledConnection(dataSource);
        List<JSONObject> columns = JdbcUtil.getRDBMSColumnProperties(connection, dataSource.getType(), table);
        return columns;
    }
}
