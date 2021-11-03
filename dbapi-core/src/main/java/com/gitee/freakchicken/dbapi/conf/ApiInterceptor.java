package com.gitee.freakchicken.dbapi.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.domain.DataSource;
import com.gitee.freakchicken.dbapi.domain.Token;
import com.gitee.freakchicken.dbapi.service.*;
import com.gitee.freakchicken.dbapi.util.IPUtil;
import com.gitee.freakchicken.dbapi.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.util.SqlEngineUtil;
import com.github.freakchick.orange.SqlMeta;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * api拦截器
 *
 * @author jiangqiang
 * @date 2019年3月19日下午4:30:56
 */
@Component
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String originIp = IPUtil.getOriginIp(request);
        log.debug(originIp);

        String method = request.getMethod();
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(5);
        log.debug(servletPath);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization");//这里很重要，要不然js header不能跨域携带  Authorization属性
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

        PrintWriter out = null;
        try {

            //js跨域的预检请求，不经过处理逻辑
            if (method.equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
                return false;
            }
            out = response.getWriter();
            boolean checkIP = checkIP(originIp);
            if (!checkIP) {
                out.append(JSON.toJSONString(ResponseDto.fail("Illegal ip (" + originIp + "), access forbidden")));
                return false;
            }
            ResponseDto responseDto = process(servletPath, request, response);

            log.debug(JSON.toJSONString(responseDto));
            out.append(JSON.toJSONString(responseDto));
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
            return false;
        } finally {
            if (out != null)
                out.close();
        }

    }

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

    public boolean checkIP(String originIp) {
        Map<String, String> map = ipService.detail();
        String status = map.get("status");
        if (status.equals("on")) {
            String mode = map.get("mode");
            if (mode.equals("black")) {
                String blackIP = map.get("blackIP");
                if (!ipService.check(mode, blackIP, originIp)) {
                    log.warn("ip黑名单拦截");
                    return false;
                }
            } else if (mode.equals("white")) {
                String whiteIP = map.get("whiteIP");
                if (!ipService.check(mode, whiteIP, originIp)) {
                    log.warn("ip白名单检查不通过");
                    return false;
                }
            }
        }
        return true;
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
                    return ResponseDto.fail("No token! Private api can not be accessed!");
                } else {
                    Token token = tokenService.getToken(tokenStr);
                    if (token == null) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return ResponseDto.fail("Invalid token! Private api can not be accessed!");
                    } else {
                        if (token.getExpire() != null && token.getExpire() < System.currentTimeMillis()) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return ResponseDto.fail("Token expired! Private api can not be accessed!");
                        } else {
                            // log.info("token存在且有效");
                            List<String> authGroups = tokenService.getAuthGroups(token.getId());
                            if (checkAuth(authGroups, config.getGroupId())) {

                            } else {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                return ResponseDto.fail("Token unauthorized to access this api");
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
                try {
                    CachePlugin cachePlugin = PluginManager.getCachePlugin(config.getCachePlugin());
                    Object o = cachePlugin.get(config, sqlParam);
                    if (o != null) {
                        return ResponseDto.apiSuccess(o); //如果缓存有数据直接返回
                    }
                } catch (Exception e) {
                    log.error("Get from cache failed!", e);
                }
            }

            String sql = config.getSql();
            SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(sql, sqlParam);
            ResponseDto responseDto = JdbcUtil.executeSql(datasource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());

            //数据转换
            if (StringUtils.isNoneBlank(config.getTransformPlugin()) && responseDto.isSuccess()) {
                List<JSONObject> data = (List<JSONObject>) (responseDto.getData());
                TransformPlugin transformPlugin = PluginManager.getTransformPlugin(config.getTransformPlugin());
                Object transform = transformPlugin.transform(data, config);
                responseDto.setData(transform);

            }

            //设置缓存
            if (StringUtils.isNoneBlank(config.getCachePlugin()) && responseDto.isSuccess()) {
                //缓存设置失败不影响主逻辑
                try {
                    CachePlugin cachePlugin = PluginManager.getCachePlugin(config.getCachePlugin());
                    cachePlugin.set(config, sqlParam, responseDto.getData());
                } catch (Exception e) {
                    log.error("Set to cache failed!", e);
                }
            }

            if (!responseDto.isSuccess()) {
                response.setStatus(500);
            }
            return responseDto;
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