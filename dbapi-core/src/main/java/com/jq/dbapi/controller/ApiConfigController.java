package com.jq.dbapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jq.dbapi.common.ResponseDto;
import com.jq.dbapi.common.ApiConfig;
import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.service.ApiConfigService;
import com.jq.dbapi.service.DataSourceService;
import com.jq.dbapi.service.GroupService;
import com.jq.dbapi.util.JdbcUtil;
import com.jq.dbapi.util.SqlEngineUtil;
import com.jq.orange.SqlMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@RestController
@Slf4j
@RequestMapping("/apiConfig")
public class ApiConfigController {

    @Autowired
    ApiConfigService apiConfigService;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    GroupService groupService;

    @RequestMapping("/add")
    public ResponseDto add(ApiConfig apiConfig) {
        return apiConfigService.add(apiConfig);
    }

    @RequestMapping("/parseParam")
    public ResponseDto parseParam(String sql) {
        try {
            Set<String> set = SqlEngineUtil.getEngine().parseParameter(sql);
//            转化成前端需要的格式
            List<JSONObject> list = set.stream().map(t -> {
                JSONObject object = new JSONObject();
                object.put("value", t);
                return object;
            }).collect(Collectors.toList());
            return ResponseDto.successWithData(list);
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        }
    }

    @RequestMapping("/getAll")
    public List<ApiConfig> getAll() {
        return apiConfigService.getAll();
    }

    //给前端使用的数据结构
    @RequestMapping("/getApiTree")
    public JSONArray getApiTree() {
        return apiConfigService.getAllDetail();
    }

    @RequestMapping("/search")
    public List<ApiConfig> search(String keyword, String field, String groupId) {
        return apiConfigService.search(keyword, field, groupId);
    }

    @RequestMapping("/detail/{id}")
    public ApiConfig detail(@PathVariable Integer id) {
        return apiConfigService.detail(id);
    }

    @RequestMapping("/delete/{id}")
    public ApiConfig delete(@PathVariable Integer id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.delete(id, path);
        return null;
    }

    @RequestMapping("/update")
    public ResponseDto update(ApiConfig apiConfig) {
        return apiConfigService.update(apiConfig);
    }

    @RequestMapping("/online/{id}")
    public ApiConfig online(@PathVariable Integer id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.online(id, path);
        return null;
    }

    @RequestMapping("/offline/{id}")
    public ApiConfig offline(@PathVariable Integer id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.offline(id, path);
        return null;
    }

    @RequestMapping("/getIPPort")
    public String getIPPort(HttpServletRequest request) {
        return request.getServerName() + ":" + request.getServerPort();
    }

    @RequestMapping("/apiDocs")
    public void apiDocs(String ids, HttpServletResponse response) {
        List<Integer> collect = Arrays.stream(ids.split(",")).map(t -> Integer.valueOf(t)).collect(Collectors.toList());

        String docs = apiConfigService.apiDocs(collect);

        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=接口文档.md");
        OutputStream os = null; //输出流
        try {
            os = response.getOutputStream();
            os.write(docs.getBytes("utf-8"));
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

    @RequestMapping("/sql/execute")
    public ResponseDto executeSql(Integer datasourceId, String sql, String params) {
        try {
            DataSource dataSource = dataSourceService.detail(datasourceId);
            Map<String, Object> map = JSON.parseObject(params, Map.class);
            SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(sql, map);
            ResponseDto responseDto = JdbcUtil.executeSql(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
            return responseDto;
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        }

    }

    @RequestMapping("/parseDynamicSql")
    public ResponseDto parseDynamicSql(String sql, String params) {
        try {
            Map<String, Object> map = JSON.parseObject(params, Map.class);
            SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(sql, map);
            return ResponseDto.successWithData(sqlMeta);
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        }

    }

}
