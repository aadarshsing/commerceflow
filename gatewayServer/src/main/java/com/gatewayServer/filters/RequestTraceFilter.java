package com.gatewayServer.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    @Autowired
    FilterUtility filterUtility;

    private final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeader = exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(requestHeader)){
            logger.debug("commerceFlow-correlation-id found in RequestTraceFilter : {}"
                    , filterUtility.getCorrelationId(requestHeader));
        }
        else{
            String correlationID = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange,correlationID);
            logger.debug("commerceFlow-correlation-id genrated in RequestTraceFilter : {}"
                    , correlationID
            );
        }
        return chain.filter(exchange);
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeader) {
        return filterUtility.getCorrelationId(requestHeader) != null;
    }
}
