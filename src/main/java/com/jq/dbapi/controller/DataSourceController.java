package com.jq.dbapi.controller;

import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.service.DataSourceService;
import com.jq.dbapi.util.JdbcUtil;
import com.jq.dbapi.util.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 10:43
 **/
@Slf4j
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping("/add")
    public void add(DataSource dataSource) {
        dataSourceService.add(dataSource);
    }

    @RequestMapping("/getAll")
    public List<DataSource> getAll() {
        return dataSourceService.getAll();
    }

    @RequestMapping("/detail/{id}")
    public DataSource detail(@PathVariable Integer id) {
        return dataSourceService.detail(id);
    }

    @RequestMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable Integer id) {
        return dataSourceService.delete(id);
    }

    @RequestMapping("/update")
    public DataSource update(DataSource dataSource) {
        dataSourceService.update(dataSource);
        return null;
    }

    @RequestMapping("/connect")
    public ResponseDto connect(DataSource dataSource) {
        Connection connection = null;
        try {
            connection = JdbcUtil.getConnection(dataSource);
            return ResponseDto.apiSuccess(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
