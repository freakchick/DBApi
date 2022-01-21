package com.gitee.freakchicken.dbapi.basic.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.service.*;

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
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("servlet execute");
        String servletPath = request.getRequestURI();
        servletPath = servletPath.substring(5);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            ResponseDto responseDto = process(servletPath, request, response);
            out.append(JSON.toJSONString(responseDto));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
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

    public ResponseDto process(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 校验接口是否存在
            ApiConfig config = apiConfigService.getConfig(path);
            if (config == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseDto.fail("Api not exists");
            }
            // 如果是私有接口，校验权限
            if (config.getPrevilege() == 0) {
                String tokenStr = request.getHeader("Authorization");
                log.debug(tokenStr);
                if (StringUtils.isBlank(tokenStr)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return ResponseDto.fail("No Token!");
                } else {
                    Token token = tokenService.getToken(tokenStr);
                    if (token == null) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return ResponseDto.fail("Invalid Token!");
                    } else {
                        if (token.getExpire() != null && token.getExpire() < System.currentTimeMillis()) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return ResponseDto.fail("Token Expired!");
                        } else {
                            // log.info("token存在且有效");
                            List<String> authGroups = tokenService.getAuthGroups(token.getId());
                            if (checkAuth(authGroups, config.getGroupId())) {

                            } else {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                return ResponseDto.fail("Invalid Token!");
                            }
                        }
                    }

                }
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
            List<Object> dataList = new ArrayList<>();
            // sql 执行并转换
            for (ApiSql apiSql : sqlList) {
                SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(apiSql.getSqlText(), sqlParam);
                Object data = JdbcUtil.executeSql(datasource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                //如果此单条sql配置了数据转换插件
                if (StringUtils.isNoneBlank(apiSql.getTransformPlugin())) {

                    List<JSONObject> sourceData = (List<JSONObject>) (data); //查询类sql的返回结果才可以这样强制转换，只有查询类sql才可以配置转换插件
                    TransformPlugin transformPlugin = PluginManager.getTransformPlugin(apiSql.getTransformPlugin());
                    data = transformPlugin.transform(sourceData, apiSql.getTransformPluginParams());
                }

                dataList.add(data);
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ResponseDto fail = ResponseDto.fail(e.getMessage() == null ? e.toString() : e.getMessage());
            return fail;
        }
    }

    public boolean checkAuth(List<String> authGroups, String group) {
        for (String authGroup : authGroups) {
            if (authGroup.equals(group)) {
                return true;
            }
        }
        return false;
    }

}
