package com.gitee.freakchicken.dbapi.basic.filter;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.service.IPService;
import com.gitee.freakchicken.dbapi.basic.util.IPUtil;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class ApiIPFilter implements Filter {

    @Autowired
    private IPService ipService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        log.debug("IP filter execute");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String originIp = IPUtil.getOriginIp(request);

        String method = request.getMethod();
        PrintWriter out = null;
        try {

            //js跨域的预检请求，不经过处理逻辑。开发模式下，前端启动，访问8521的页面进行请求测试会跨域
            if (method.equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            boolean checkIP = ipService.checkIP(originIp);
            if (!checkIP) {
                out = response.getWriter();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.append(JSON.toJSONString(ResponseDto.fail("Illegal ip (" + originIp + "), access forbidden")));

            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.append(JSON.toJSONString(ResponseDto.fail(e.toString())));
            log.error(e.toString());

        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    @Override
    public void destroy() {

    }
}