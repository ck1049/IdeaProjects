package cn.itcast.zuul.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (token == null || token.isEmpty()){

            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            HttpHeaders headers = response.getHeaders();
            headers.set(HttpHeaders.LOCATION, "https://www.baidu.com");
            headers.add("Content-type", "text/plain;charset=UTF-8");
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
