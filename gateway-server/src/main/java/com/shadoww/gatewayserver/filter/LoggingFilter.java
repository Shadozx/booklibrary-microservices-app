package com.shadoww.gatewayserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        String method = exchange.getRequest().getMethod().name();
        logger.info("➡️ {} {}", method, path);

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    HttpStatusCode status = exchange.getResponse().getStatusCode();

                    logger.info("✅ Success! - {} {} {}", status != null ? status.value() : "UNKNOWN", method, path);
                })
                .doOnError(error -> {
                    HttpStatusCode status = exchange.getResponse().getStatusCode();
                    logger.error("❌ Error! - {} {} {} - Error: {}", status != null ? status.value() : "UNKNOWN", method, path, error.getMessage());
                });
    }

    @Override
    public int getOrder() {
        return -1;  // Виконати фільтр перед іншими
    }

}