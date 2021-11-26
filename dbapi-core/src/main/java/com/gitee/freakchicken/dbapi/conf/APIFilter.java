package com.gitee.freakchicken.dbapi.conf;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.service.IPService;
import com.gitee.freakchicken.dbapi.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
@WebFilter(filterName = "APIFilter", urlPatterns = "/api/*")
public class APIFilter implements Filter {

    @Autowired
    IPService ipService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String originIp = IPUtil.getOriginIp(request);

        String method = request.getMethod();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization");//这里很重要，要不然js header不能跨域携带  Authorization属性
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

        PrintWriter out = null;
        try {

            //js跨域的预检请求，不经过处理逻辑。开发模式下，前端启动，访问8521的页面进行请求测试会跨域
            if (method.equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            boolean checkIP = checkIP(originIp);
            if (!checkIP) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out = response.getWriter();
                out.append(JSON.toJSONString(ResponseDto.fail("Illegal ip (" + originIp + "), access forbidden")));

            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            if (out != null)
                out.close();
        }

    }

    @Override
    public void destroy() {

    }

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
}