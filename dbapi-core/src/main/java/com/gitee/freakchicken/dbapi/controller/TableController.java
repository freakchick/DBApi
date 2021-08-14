package com.gitee.freakchicken.dbapi.controller;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.domain.DataSource;
import com.gitee.freakchicken.dbapi.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.util.PoolManager;
import com.gitee.freakchicken.dbapi.dao.DataSourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
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
        List<String> tables = JdbcUtil.getAllTables(connection, dataSource.getType());
        List<JSONObject> list = tables.stream().map(t -> {
            JSONObject jo = new JSONObject();
            jo.put("label", t);
            jo.put("columns", new ArrayList<Object>());
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
