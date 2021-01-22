package com.jq.dbapi.controller;

import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.service.DataSourceService;
import com.jq.dbapi.util.JdbcUtil;
import com.jq.dbapi.util.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 10:43
 **/
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
    public DataSource delete(@PathVariable Integer id) {
        dataSourceService.delete(id);
        return null;
    }

    @RequestMapping("/update")
    public DataSource update(DataSource dataSource) {
        dataSourceService.update(dataSource);
        return null;
    }

    @RequestMapping("/connect")
    public ResponseDto connect(DataSource dataSource) {
        try {
            JdbcUtil.getConnection(dataSource);
            return ResponseDto.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.fail(e.getMessage());
        }
    }
}
