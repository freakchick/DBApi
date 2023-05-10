package com.gitee.freakchicken.dbapi.basic.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.domain.Group;
import com.gitee.freakchicken.dbapi.basic.service.ApiConfigService;
import com.gitee.freakchicken.dbapi.basic.service.DataSourceService;
import com.gitee.freakchicken.dbapi.basic.service.GroupService;
import com.gitee.freakchicken.dbapi.basic.util.Constants;
import com.gitee.freakchicken.dbapi.basic.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.basic.util.SqlEngineUtil;
import com.gitee.freakchicken.dbapi.basic.util.UUIDUtil;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ApiPluginConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.github.freakchick.orange.SqlMeta;

import lombok.extern.slf4j.Slf4j;

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

    @Value("${dbapi.api.context}")
    String apiContext;

    @RequestMapping("/context")
    public String getContext() {
        return apiContext;
    }

    @RequestMapping("/add")
    public ResponseDto add(@RequestBody JSONObject jo) {
        ApiConfig config = new ApiConfig();
        config.setName(jo.getString("name"));
        config.setPath(jo.getString("path"));
        config.setNote(jo.getString("note"));
        config.setGroupId(jo.getString("groupId"));
        config.setContentType(jo.getString("contentType"));
        config.setJsonParam(jo.getString("jsonParam"));
        config.setParams(jo.getJSONArray("paramsJson").toString(SerializerFeature.WriteMapNullValue));
        config.setAccess(jo.getInteger("access"));
        config.setTask(jo.getJSONArray("taskJson").toString(SerializerFeature.WriteMapNullValue));
        config.setStatus(Constants.API_STATUS_OFFLINE);

        String id = UUIDUtil.id();
        config.setId(id);

        JSONArray array = jo.getJSONArray("alarmPlugins");
        array.add(jo.getJSONObject("cachePlugin"));
        array.add(jo.getJSONObject("globalTransformPlugin"));

        List<ApiPluginConfig> javaList = array.toJavaList(ApiPluginConfig.class);

        List<ApiPluginConfig> collect = javaList.stream().filter(t -> t != null && StringUtils.isNotEmpty(t.getPluginName())).collect(Collectors.toList());
        collect.forEach(t -> t.setApiId(id));

        return apiConfigService.add(config, collect);
    }

    @Deprecated
    @RequestMapping("/parseParam")
    public ResponseDto parseParam(String sql) {
        try {
            Set<String> set = SqlEngineUtil.getEngine().parseParameter(sql);
            // 转化成前端需要的格式
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

    // 给前端使用的数据结构
    @RequestMapping("/getApiTree")
    public JSONArray getAllApiTree() {
        return apiConfigService.getAllApiTree();
    }

    @RequestMapping("/search")
    public List<ApiConfig> search(String name, String note, String path,  String groupId) {
        return apiConfigService.search(name, note, path, groupId);
    }

    @RequestMapping("/detail/{id}")
    public ApiConfig detail(@PathVariable String id) {
        return apiConfigService.detail(id);
    }

    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        apiConfigService.delete(id);
    }

    @RequestMapping("/update")
    public ResponseDto update(@RequestBody JSONObject jo) {
        ApiConfig config = new ApiConfig();
        config.setId(jo.getString("id"));
        config.setName(jo.getString("name"));
        config.setPath(jo.getString("path"));
        config.setNote(jo.getString("note"));
        config.setGroupId(jo.getString("groupId"));
        config.setContentType(jo.getString("contentType"));
        config.setJsonParam(jo.getString("jsonParam"));
        config.setParams(jo.getJSONArray("paramsJson").toString(SerializerFeature.WriteMapNullValue));
        config.setAccess(jo.getInteger("access"));
        config.setTask(jo.getJSONArray("taskJson").toString(SerializerFeature.WriteMapNullValue));
        config.setStatus(Constants.API_STATUS_OFFLINE);

        JSONArray array = jo.getJSONArray("alarmPlugins");
        array.add(jo.getJSONObject("cachePlugin"));
        array.add(jo.getJSONObject("globalTransformPlugin"));

        List<ApiPluginConfig> javaList = array.toJavaList(ApiPluginConfig.class);
        List<ApiPluginConfig> collect = javaList.stream().filter(t -> t !=null && StringUtils.isNotEmpty(t.getPluginName())).collect(Collectors.toList());

        return apiConfigService.update(config, collect);
    }

    @RequestMapping("/online/{id}")
    public void online(@PathVariable String id) {
        apiConfigService.online(id);
    }

    @RequestMapping("/offline/{id}")
    public void offline(@PathVariable String id) {
        apiConfigService.offline(id);
    }

    @RequestMapping("/apiDocs")
    public void apiDocs(String ids, HttpServletResponse response) {
        List<String> collect = Arrays.asList(ids.split(","));
        String docs = apiConfigService.apiDocs(collect);
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=API docs.md");
        OutputStream os = null; // 输出流
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

    /**
     * 导出API 配置
     *
     * @param ids
     * @param response
     */
    @RequestMapping("/downloadConfig")
    public void downloadConfig(String ids, HttpServletResponse response) {
        List<String> collect = Arrays.asList(ids.split(","));
        JSONObject jo = apiConfigService.exportAPI(collect);
        String s = jo.toString(SerializerFeature.WriteMapNullValue);
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
        // response.setHeader("Content-Disposition", "attachment; filename=api配置.json");
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

    /**
     * 导入API配置
     *
     * @param file
     * @throws IOException
     */
    @RequestMapping(value = "/import", produces = "application/json;charset=UTF-8")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String s = IOUtils.toString(file.getInputStream(), "utf-8");
        JSONObject jsonObject = JSON.parseObject(s);
        List<ApiConfig> apis = jsonObject.getJSONArray("api").toJavaList(ApiConfig.class);
        List<ApiPluginConfig> plugins = jsonObject.getJSONArray("plugins").toJavaList(ApiPluginConfig.class);
        apiConfigService.importAPI(apis, plugins);
    }

    @RequestMapping(value = "/importGroup", produces = "application/json;charset=UTF-8")
    public void importGroup(@RequestParam("file") MultipartFile file) throws IOException {
        String s = IOUtils.toString(file.getInputStream(), "utf-8");
        List<Group> configs = JSON.parseArray(s, Group.class);
        groupService.insertBatch(configs);
    }

    @RequestMapping("/sql/execute")
    public ResponseDto executeSql(String datasourceId, String sql, String params) {
        DruidPooledConnection connection = null;
        try {
            DataSource dataSource = dataSourceService.detail(datasourceId);
            connection = PoolManager.getPooledConnection(dataSource);
            Map<String, Object> map = JSON.parseObject(params, Map.class);
            SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(sql, map);
            Object data = JdbcUtil.executeSql(connection, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
            return ResponseDto.successWithData(data);
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
