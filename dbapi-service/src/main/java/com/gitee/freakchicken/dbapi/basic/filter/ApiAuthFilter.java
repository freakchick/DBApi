package com.gitee.freakchicken.dbapi.basic.filter;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import com.gitee.freakchicken.dbapi.basic.log.AccessLogWriter;
import com.gitee.freakchicken.dbapi.basic.service.ApiConfigService;
import com.gitee.freakchicken.dbapi.basic.service.ClientService;
import com.gitee.freakchicken.dbapi.basic.util.Constants;
import com.gitee.freakchicken.dbapi.basic.util.IPUtil;
import com.gitee.freakchicken.dbapi.basic.util.ThreadUtils;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ApiAuthFilter implements Filter {

    private static Logger accessLogger = LoggerFactory.getLogger("accessLogger");

    @Autowired
    private ApiConfigService apiConfigService;

    @Autowired
    private ClientService clientService;

    @Autowired
    ClientService appService;

    @Autowired
    AccessLogWriter accessLogWriter;

    @Value("${dbapi.api.context}")
    private String apiContext;

    @Value("${access.log.writer}")
    private String logWriter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {

        long now = System.currentTimeMillis();
        AccessLog accessLog = new AccessLog();
        accessLog.setTimestamp(now / 1000);

        log.debug("auth filter execute");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        String servletPath = uri.substring(apiContext.length() + 2);

        // 不使用writer的时候不要提前获取response的writer,否则无法在后续filter中设置编码
        try {
            // 校验接口是否存在
            ApiConfig config = apiConfigService.getConfig(servletPath);
            if (config == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().append(JSON.toJSONString(ResponseDto.fail("Api not exists")));
                return;
            }
            accessLog.setApiId(config.getId());

            String tokenStr = request.getHeader("Authorization");
            String clientId = clientService.verifyToken(tokenStr);
            accessLog.setClientId(clientId);

            // 如果是私有接口，校验权限
            if (config.getAccess() == Constants.API_ACCESS_PRIVATE) {
                
                if (StringUtils.isBlank(tokenStr)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().append(JSON.toJSONString(ResponseDto.fail("No Token!")));
                    return;
                } else {
                    if (clientId == null) {
                        log.error("token[{}] matched no clientId", tokenStr);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().append(JSON.toJSONString(ResponseDto.fail("Token Invalid!")));
                        return;
                    } else {
                        List<String> authGroups = appService.getAuthGroups(clientId);
                        if (!authGroups.contains(config.getGroupId())) {
                            log.error("token[{}] matched clientId[{}], but clientId not authorized", tokenStr, clientId);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().append(JSON.toJSONString(ResponseDto.fail("Token Invalid!")));
                            return;
                        }
                    }

                }
            }

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().append(JSON.toJSONString(ResponseDto.fail(e.toString())));
            log.error(e.getMessage(), e);
            accessLog.setError(e.getMessage());
        } finally {
            if (response.getWriter() != null) {
                response.getWriter().close();
            }

            accessLog.setDuration(System.currentTimeMillis() - now);
            accessLog.setIp(IPUtil.getOriginIp(request));
            accessLog.setStatus(response.getStatus());
            accessLog.setUrl(uri);
            accessLog.setId(UUID.randomUUID().toString());
            accessLogger.info(JSON.toJSONString(accessLog));
            if (!logWriter.equals("null")) {
                ThreadUtils.submitAlarmTask(new Runnable() {
                    @Override
                    public void run() {
                        accessLogWriter.write(accessLog);
                    }
                });
            }

        }

    }

    @Override
    public void destroy() {

    }

}