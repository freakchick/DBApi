package com.jq.dbapi.conf;

import com.alibaba.fastjson.JSON;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.service.ApiConfigService;
import com.jq.dbapi.service.ApiService;
import com.jq.dbapi.service.DataSourceService;
import com.jq.dbapi.util.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

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
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(5);
        log.info(servletPath);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            ResponseDto responseDto = process(servletPath, request);
            out = response.getWriter();
            out.append(JSON.toJSONString(responseDto));
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
            return false;
        } finally {
            out.close();
        }

    }

    @Autowired
    ApiConfigService apiConfigService;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    ApiService apiService;

    public ResponseDto process(String path, HttpServletRequest request) {
        try {
            ApiConfig config = apiConfigService.getConfig(path);
            if (config == null) {
                return ResponseDto.fail("该接口不存在！！");
            }

            DataSource datasource = dataSourceService.detail(config.getDatasourceId());
            if (datasource == null) {
                return ResponseDto.fail("数据源不存在！！");
            }

            List<Object> sqlParam = apiService.getSqlParam(request, config);

            return apiService.executeSql(config, datasource, sqlParam);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseDto.fail(e.getMessage());
        }
    }

}