package com.gitee.freakchicken.dbapi.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.service.IPService;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class GatewayIPFilter implements GlobalFilter, Ordered {

    @Autowired
    IPService ipService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 从request对象中获取客户端ip
        String clientIp = request.getRemoteAddress().getHostString();
        if (!checkIP(clientIp)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            String data = JSON.toJSONString(ResponseDto.fail("Illegal ip (" + clientIp + "), access forbidden"));
            DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
            return response.writeWith(Mono.just(wrap));
        }
        // 合法请求，放⾏，执⾏后续的过滤器
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public boolean checkIP(String originIp) {
        Map<String, String> map = ipService.detail();
        String status = map.get("status");
        if (status.equals("on")) {
            log.debug("gateway filter ip check...");
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
