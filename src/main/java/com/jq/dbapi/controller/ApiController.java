package com.jq.dbapi.controller;

import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.service.ApiConfigService;
import com.jq.dbapi.service.ApiService;
import com.jq.dbapi.service.DataSourceService;
import com.jq.dbapi.util.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@Slf4j
@RequestMapping("/api")
public class ApiController {

    @Autowired
    ApiConfigService apiConfigService;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    ApiService apiService;

    @RequestMapping("{path}")
    public ResponseDto hello(@PathVariable("path") String path, HttpServletRequest request) {
        try {
            ApiConfig config = apiConfigService.getConfig(path);
            if (config == null) {
                return ResponseDto.fail("该接口不存在！！");
            }

            DataSource datasource = dataSourceService.detail(config.getDatasourceId());
            if (datasource == null) {
                return ResponseDto.fail("数据源不存在！！");
            }

            //生成真实的sql
            String sql = apiService.buildSql(request, config);
//            log.info("执行sql: {}", sql);
            return apiService.executeSql(sql, datasource);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseDto.fail(e.getMessage());
        }
    }

}
