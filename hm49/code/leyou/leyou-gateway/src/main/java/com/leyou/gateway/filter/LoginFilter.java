package com.leyou.gateway.filter;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter implements Ordered, GlobalFilter {

    @Autowired
    private JwtProperties jwtproperties;
    @Autowired
    private FilterProperties filterProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            String uri = request.getURI().getPath();
            List<String> allowPaths = filterProperties.getAllowPaths();
            for (String allowPath : allowPaths) {
                // 白名单直接放行
                if (uri.contains(allowPath)){
                    return chain.filter(exchange);
                }
            }
            String token = CookieUtils.getCookieValue(request, jwtproperties.getCookieName());
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtproperties.getPublicKey());
            if (userInfo.getId() != null){
                // 已登录放行
                return chain.filter(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        HttpHeaders headers = response.getHeaders();
        headers.set(HttpHeaders.LOCATION, "http://www.leyou.com/login.html");
        headers.add("Content-type", "text/plain;charset=UTF-8");
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
