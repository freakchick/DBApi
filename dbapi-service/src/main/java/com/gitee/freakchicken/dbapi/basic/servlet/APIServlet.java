package com.gitee.freakchicken.dbapi.basic.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.executor.SQLExecutor;
import com.gitee.freakchicken.dbapi.basic.executor.ESExecutor;
import com.gitee.freakchicken.dbapi.basic.service.ApiConfigService;
import com.gitee.freakchicken.dbapi.basic.service.ApiService;
import com.gitee.freakchicken.dbapi.basic.service.DataSourceService;
import com.gitee.freakchicken.dbapi.basic.service.IPService;

import com.gitee.freakchicken.dbapi.common.ApiConfig;

import com.gitee.freakchicken.dbapi.common.ResponseDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
    IPService ipService;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
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
            DataSource datasource = dataSourceService.detail(config.getDatasourceId());
            if (datasource == null) {
                response.setStatus(500);
                return ResponseDto.fail("Datasource not exists!");
            }

            Map<String, Object> sqlParam = getParams(request, config);

            ResponseDto res = null;
            if ("es".equals(datasource.getType())) {
                res = ESExecutor.execute(config, datasource, sqlParam, request);
            } else {
                res = SQLExecutor.execute(config, datasource, sqlParam, request);
            }

            return res;

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    private Map<String, Object> getParams(HttpServletRequest request, ApiConfig apiConfig) {
        /**
         * Content-Type格式说明:
         * {@see <a href="https://www.w3.org/Protocols/rfc1341/4_Content-Type.html">Content-Type</a>}
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
                throw new RuntimeException("this API only support content-type: " + apiConfig.getContentType() + ", but you use: " + contentType);
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
