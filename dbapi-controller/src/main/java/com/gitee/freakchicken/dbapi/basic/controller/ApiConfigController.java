package com.gitee.freakchicken.dbapi.basic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.domain.Group;
import com.gitee.freakchicken.dbapi.basic.service.ApiConfigService;
import com.gitee.freakchicken.dbapi.basic.service.DataSourceService;
import com.gitee.freakchicken.dbapi.basic.service.GroupService;
import com.gitee.freakchicken.dbapi.basic.service.NacosService;
import com.gitee.freakchicken.dbapi.basic.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.basic.util.SqlEngineUtil;
import com.github.freakchick.orange.SqlMeta;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${dbapi.mode}")
    String mode;

    @Autowired
    ApiConfigService apiConfigService;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    GroupService groupService;

    @Autowired
    NacosService nacosService;

    @RequestMapping("/add")
    public ResponseDto add(@RequestBody ApiConfig apiConfig) {
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
    public ApiConfig detail(@PathVariable String id) {
        return apiConfigService.detail(id);
    }

    @RequestMapping("/delete/{id}")
    public ApiConfig delete(@PathVariable String id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.delete(id, path);
        return null;
    }

    @RequestMapping("/update")
    public ResponseDto update(@RequestBody ApiConfig apiConfig) {
        return apiConfigService.update(apiConfig);
    }

    @RequestMapping("/online/{id}")
    public ApiConfig online(@PathVariable String id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.online(id, path);
        return null;
    }

    @RequestMapping("/offline/{id}")
    public ApiConfig offline(@PathVariable String id) {
        String path = apiConfigService.getPath(id);
        apiConfigService.offline(id, path);
        return null;
    }

    @RequestMapping("/getIPPort")
    public String getIPPort(HttpServletRequest request) {

        if ("standalone".equals(mode)) {
            return request.getServerName() + ":" + request.getServerPort();
        } else if ("cluster".equals(mode)) {
            return nacosService.getGatewayAddress();
        } else {
            return null;
        }
    }

    @RequestMapping("/apiDocs")
    public void apiDocs(String ids, HttpServletResponse response) {
        List<String> collect = Arrays.asList(ids.split(","));
        String docs = apiConfigService.apiDocs(collect);
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=API docs.md");
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

    @RequestMapping("/downloadConfig")
    public void downloadConfig(String ids, HttpServletResponse response) {
        List<String> collect = Arrays.asList(ids.split(","));
        List<ApiConfig> apiConfigs = apiConfigService.selectBatch(collect);
        String s = JSON.toJSONString(apiConfigs);
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=api_config.json");
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

    @RequestMapping("/downloadGroupConfig")
    public void downloadGroupConfig(String ids, HttpServletResponse response) {
        List<String> collect = Arrays.asList(ids.split(","));
        List<Group> list = groupService.selectBatch(collect);
        String s = JSON.toJSONString(list);
        response.setContentType("application/x-msdownload;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment; filename=api配置.json");
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
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        String s = IOUtils.toString(file.getInputStream(), "utf-8");
        List<ApiConfig> configs = JSON.parseArray(s, ApiConfig.class);
        configs.stream().forEach(t -> t.setStatus(0));
        apiConfigService.insertBatch(configs);

    }

    @RequestMapping(value = "/importGroup", produces = "application/json;charset=UTF-8")
    public void importGroup(@RequestParam("file") MultipartFile file) throws IOException {

        String s = IOUtils.toString(file.getInputStream(), "utf-8");
        List<Group> configs = JSON.parseArray(s, Group.class);
        groupService.insertBatch(configs);

    }

    @RequestMapping("/sql/execute")
    public ResponseDto executeSql(String datasourceId, String sql, String params) {
        try {
            DataSource dataSource = dataSourceService.detail(datasourceId);
            Map<String, Object> map = JSON.parseObject(params, Map.class);
            SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(sql, map);
            Object data = JdbcUtil.executeSql(dataSource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
            return ResponseDto.successWithData(data);
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
