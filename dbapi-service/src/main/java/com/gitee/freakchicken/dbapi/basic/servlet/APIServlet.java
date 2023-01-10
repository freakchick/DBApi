package com.gitee.freakchicken.dbapi.basic.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAlarmConfig;
import com.gitee.freakchicken.dbapi.basic.domain.ApiCacheConfig;
import com.gitee.freakchicken.dbapi.basic.executor.ESExecutor;
import com.gitee.freakchicken.dbapi.basic.executor.SQLExecutor;
import com.gitee.freakchicken.dbapi.basic.service.AlarmConfigService;
import com.gitee.freakchicken.dbapi.basic.service.ApiConfigService;
import com.gitee.freakchicken.dbapi.basic.service.ApiService;
import com.gitee.freakchicken.dbapi.basic.service.CacheConfigService;
import com.gitee.freakchicken.dbapi.basic.service.DataSourceService;
import com.gitee.freakchicken.dbapi.basic.util.ThreadUtils;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;

import lombok.extern.slf4j.Slf4j;

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
    AlarmConfigService alarmService;
    @Autowired
    CacheConfigService cacheService;

    @Value("${dbapi.api.context}")
    String apiContext;

    @Autowired
    SQLExecutor SQLExecutor;

    @Autowired
    ESExecutor ESExecutor;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public ResponseDto process(String path, HttpServletRequest request, HttpServletResponse response) {

        // // 校验接口是否存在
        ApiConfig config = apiConfigService.getConfig(path);
        if (config == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseDto.fail("Api not exists");
        }

        try {

            Map<String, Object> requestParam = getParams(request, config);

            ApiCacheConfig cache = cacheService.getByApiId(config.getId());

            // ger data from cache
            if (cache != null) {
                CachePlugin cachePlugin = PluginManager.getCachePlugin(cache.getPluginName());
                Object o = cachePlugin.get(config, requestParam);
                if (o != null) {
                    return ResponseDto.apiSuccess(o); // 如果缓存有数据直接返回
                }
            }

            Object res = null;
            if ("es".equals(config.getTaskType())) {
                res = ESExecutor.execute(config, requestParam);
            } else if("sql".equals(config.getTaskType())) {
                res = SQLExecutor.execute(config, requestParam);
            }

            // set data to cache
            if (cache != null) {
                CachePlugin cachePlugin = PluginManager.getCachePlugin(cache.getPluginName());
                cachePlugin.set(config, requestParam, res);
            }

            return ResponseDto.apiSuccess(res);

        } catch (Exception e) {
            // alarm if error
            List<ApiAlarmConfig> alarms = alarmService.selectByApiId(config.getId());
            for (ApiAlarmConfig alarm : alarms) {
                try {
                    String param = alarm.getPluginParam();
                    AlarmPlugin alarmPlugin = PluginManager.getAlarmPlugin(alarm.getPluginName());
                    ThreadUtils.submitAlarmTask(new Runnable() {
                        @Override
                        public void run() {
                            alarmPlugin.alarm(e, config, request, param);
                        }
                    });
                } catch (Exception error) {
                    log.error(alarm.getPluginName() + " error!", error);
                }
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    private Map<String, Object> getParams(HttpServletRequest request, ApiConfig apiConfig) {
        /**
         * Content-Type格式说明:
         * {@see <a href=
         * "https://www.w3.org/Protocols/rfc1341/4_Content-Type.html">Content-Type</a>}
         * type/subtype(;parameter)? type
         */
        String unParseContentType = request.getContentType();

        // 如果是浏览器get请求过来，取出来的contentType是null
        if (unParseContentType == null) {
            unParseContentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE;
        }
        // issues/I57ZG2
        // 解析contentType 格式: appliation/json;charset=utf-8
        String[] contentTypeArr = unParseContentType.split(";");
        String contentType = contentTypeArr[0];

        Map<String, Object> params = null;
        // 如果是application/json请求，不管接口规定的content-type是什么，接口都可以访问，且请求参数都以json body 为准
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            JSONObject jo = getHttpJsonBody(request);
            params = JSONObject.parseObject(jo.toJSONString(), new TypeReference<Map<String, Object>>() {
            });
        }
        // 如果是application/x-www-form-urlencoded请求，先判断接口规定的content-type是不是确实是application/x-www-form-urlencoded
        else if (contentType.equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(apiConfig.getContentType())) {
                params = apiService.getSqlParam(request, apiConfig);
            } else {
                throw new RuntimeException("This API only supports content-type: " + apiConfig.getContentType()
                        + ", but you use: " + contentType);
            }
        } else {
            throw new RuntimeException("content-type not supported: " + contentType);
        }

        return params;
    }

    private JSONObject getHttpJsonBody(HttpServletRequest request) {
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
    }

}
