package com.gitee.freakchicken.dbapi.basic.filter;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.service.ApiConfigService;
import com.gitee.freakchicken.dbapi.basic.service.AppService;
import com.gitee.freakchicken.dbapi.basic.service.AppTokenService;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ApiAuthFilter implements Filter {

    @Autowired
    private ApiConfigService apiConfigService;

    @Autowired
    private AppTokenService tokenService;

    @Autowired
    AppService appService;

    @Value("${dbapi.api.context}")
    private String apiContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        log.debug("auth filter execute");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getRequestURI();
        servletPath = servletPath.substring(apiContext.length() + 2);

        // 不使用writer的时候不要提前获取response的writer,否则无法在后续filter中设置编码
        try {
            // 校验接口是否存在
            ApiConfig config = apiConfigService.getConfig(servletPath);
            if (config == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().append(JSON.toJSONString(ResponseDto.fail("Api not exists")));
                return;
            }
            // 如果是私有接口，校验权限
            if (config.getPrevilege() == 0) {
                String tokenStr = request.getHeader("Authorization");
                if (StringUtils.isBlank(tokenStr)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().append(JSON.toJSONString(ResponseDto.fail("No Token!")));
                    return;
                } else {
                    String appId = tokenService.verifyToken(tokenStr);
                    if (appId == null) {
                        log.error("token[{}] matched no appid", tokenStr);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().append(JSON.toJSONString(ResponseDto.fail("Token Invalid!")));
                        return;
                    } else {
                        List<String> authGroups = appService.getAuthGroups(appId);
                        if (!authGroups.contains(config.getGroupId())) {
                            log.error("token[{}] matched appid[{}], but appid not authorized", tokenStr, appId);
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

        } finally {
            if (response.getWriter() != null) {
                response.getWriter().close();
            }
        }

    }

    @Override
    public void destroy() {

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