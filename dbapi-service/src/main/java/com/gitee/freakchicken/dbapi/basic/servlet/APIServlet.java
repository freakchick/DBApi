package com.gitee.freakchicken.dbapi.basic.servlet;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.service.*;

import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ApiSql;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.domain.Token;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;

import com.gitee.freakchicken.dbapi.basic.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.basic.util.SqlEngineUtil;
import com.github.freakchick.orange.SqlMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class APIServlet extends HttpServlet {

    @Autowired
    ApiConfigService apiConfigService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    ApiService apiService;
    @Autowired
    TokenService tokenService;
    @Autowired
    IPService ipService;

    @Value("${dbapi.api.context}")
    String apiContext;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("servlet execute");
        String servletPath = request.getRequestURI();
        servletPath = servletPath.substring(apiContext.length() + 2);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            ResponseDto responseDto = process(servletPath, request, response);
            out.append(JSON.toJSONString(responseDto));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.append(JSON.toJSONString(ResponseDto.fail(e.toString())));
            log.error(e.toString(), e);
        } finally {
            if (out != null)
                out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    public ResponseDto process(String path, HttpServletRequest request, HttpServletResponse response) throws SQLException {

//            // 校验接口是否存在
        ApiConfig config = apiConfigService.getConfig(path);
        if (config == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseDto.fail("Api not exists");
        }

        DataSource datasource = dataSourceService.detail(config.getDatasourceId());
        if (datasource == null) {
            response.setStatus(500);
            return ResponseDto.fail("Datasource not exists!");
        }

        Map<String, Object> sqlParam = apiService.getSqlParam(request, config);

        //从缓存获取数据
        if (StringUtils.isNoneBlank(config.getCachePlugin())) {
            CachePlugin cachePlugin = PluginManager.getCachePlugin(config.getCachePlugin());
            Object o = cachePlugin.get(config, sqlParam);
            if (o != null) {
                return ResponseDto.apiSuccess(o); //如果缓存有数据直接返回
            }
        }

        List<ApiSql> sqlList = config.getSqlList();
        DruidPooledConnection connection = PoolManager.getPooledConnection(datasource);
        //执行sql
        List<Object> dataList = executeSql(connection, sqlList, sqlParam);

        //执行数据转换
        for (int i = 0; i < sqlList.size(); i++) {
            ApiSql apiSql = sqlList.get(i);
            Object data = dataList.get(i);
            //如果此单条sql是查询类sql，并且配置了数据转换插件
            if (data instanceof Iterable && StringUtils.isNotBlank(apiSql.getTransformPlugin())) {
                log.info("transform plugin execute");
                List<JSONObject> sourceData = (List<JSONObject>) (data); //查询类sql的返回结果才可以这样强制转换，只有查询类sql才可以配置转换插件
                TransformPlugin transformPlugin = PluginManager.getTransformPlugin(apiSql.getTransformPlugin());
                data = transformPlugin.transform(sourceData, apiSql.getTransformPluginParams());
            }
        }
        Object res = dataList;
        //如果只有单条sql,返回结果不是数组格式
        if (dataList.size() == 1) {
            res = dataList.get(0);
        }
        ResponseDto dto = ResponseDto.apiSuccess(res);
        //设置缓存
        if (StringUtils.isNoneBlank(config.getCachePlugin())) {
            CachePlugin cachePlugin = PluginManager.getCachePlugin(config.getCachePlugin());
            cachePlugin.set(config, sqlParam, dto.getData());
        }
        return dto;
    }

    public List<Object> executeSql(Connection connection, List<ApiSql> sqlList, Map<String, Object> sqlParam) {
        List<Object> dataList = new ArrayList<>();
        try {
            connection.setAutoCommit(false);

            for (ApiSql apiSql : sqlList) {
                SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(apiSql.getSqlText(), sqlParam);
                Object data = JdbcUtil.executeSql(connection, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                dataList.add(data);
            }
            connection.commit();
            return dataList;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*    private JSONObject getJsonInfo(HttpServletRequest request) {
        try {
            InputStreamReader in = new InputStreamReader(request.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(in);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject jsonObject = JSON.parseObject(sb.toString());
            return jsonObject;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {

        }
        return null;
    }*/

}
