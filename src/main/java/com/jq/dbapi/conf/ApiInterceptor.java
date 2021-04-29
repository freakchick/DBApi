package com.jq.dbapi.conf;

import com.alibaba.fastjson.JSON;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.domain.Token;
import com.jq.dbapi.service.ApiConfigService;
import com.jq.dbapi.service.ApiService;
import com.jq.dbapi.service.DataSourceService;
import com.jq.dbapi.service.TokenService;
import com.jq.dbapi.util.ResponseDto;
import com.jq.dbapi.util.SqlEngineUtil;
import com.jq.orange.SqlMeta;
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
 * 日志拦截器
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
        String method = request.getMethod();
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(5);
        log.info(servletPath);
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

            ResponseDto responseDto = process(servletPath, request, response);

            log.info(JSON.toJSONString(responseDto));
            out = response.getWriter();
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

    public ResponseDto process(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 校验接口是否存在
            ApiConfig config = apiConfigService.getConfig(path);
            if (config == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseDto.fail("该接口不存在！！");
            }
            // 如果是私有接口，校验权限
            if (config.getPrevilege() == 0) {
                String tokenStr = request.getHeader("Authorization");
                if (StringUtils.isBlank(tokenStr)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return ResponseDto.fail("header中token缺失，私有接口禁止访问！！");
                } else {
                    Token token = tokenService.getToken(tokenStr);
                    if (token == null) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return ResponseDto.fail("token无效，私有接口禁止访问！！");
                    } else {
                        if (token.getExpire() != null && token.getExpire() < System.currentTimeMillis()) {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            return ResponseDto.fail("token过期，私有接口禁止访问！！");
                        } else {
                            // log.info("token存在且有效");
                            List<Integer> authGroups = tokenService.getAuthGroups(token.getId());
                            if (checkAuth(authGroups, config.getGroupId())) {

                            } else {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                return ResponseDto.fail("该token无权访问此接口");
                            }
                        }
                    }

                }
            }

            DataSource datasource = dataSourceService.detail(config.getDatasourceId());
            if (datasource == null) {
                response.setStatus(500);
                return ResponseDto.fail("数据源不存在！！");
            }

            Map<String, Object> sqlParam = apiService.getSqlParam(request, config);
            String sql = config.getSql();
            SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(sql, sqlParam);
            log.info(sqlMeta.getSql());
            ResponseDto responseDto = apiService.executeSql(config.getIsSelect(), datasource, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
            if (!responseDto.isSuccess())
                response.setStatus(500);
            return responseDto;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseDto.fail(e.getMessage());
        }
    }

    public boolean checkAuth(List<Integer> authGroups, Integer group) {
        for (Integer authGroup : authGroups) {
            if (authGroup == group) {
                return true;
            }
        }
        return false;
    }

}