package com.gitee.freakchicken.dbapi.basic.controller;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.service.DataSourceService;
import com.gitee.freakchicken.dbapi.basic.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.basic.util.ThreadContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
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
        dataSource.setCreateUserId(ThreadContainer.getCurrentThreadUserId());
        dataSourceService.add(dataSource);
    }

    @RequestMapping("/getAll")
    public List<DataSource> getAll() {
        return dataSourceService.getAll();
    }

    @RequestMapping("/detail/{id}")
    public DataSource detail(@PathVariable String id) {
        return dataSourceService.detail(id);
    }

    @RequestMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
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
            log.error(e.getMessage(), e);
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

    @RequestMapping("/export")
    public void export(String ids, HttpServletResponse response) {
        List<String> collect = Arrays.asList(ids.split(","));
        List<DataSource> list = dataSourceService.selectBatch(collect);
        String s = JSON.toJSONString(list);
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=datasource.json");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            os.write(s.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequestMapping(value = "/import", produces = "application/json;charset=UTF-8")
    public void importDatasource(@RequestParam("file") MultipartFile file) throws IOException {

        String s = IOUtils.toString(file.getInputStream(), "utf-8");
        List<DataSource> list = JSON.parseArray(s, DataSource.class);
        list.stream().forEach(t -> {
            t.setCreateUserId(ThreadContainer.getCurrentThreadUserId());
            t.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
            t.setUpdateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
        });
        dataSourceService.insertBatch(list);

    }
}
