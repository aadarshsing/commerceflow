package com.gatewayServer.filters;


import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class FilterUtility {

    public static final  String  CORRELATION_ID = "commerceflow-correlation-id";

    public String  getCorrelationId(HttpHeaders requestHeaders){
        return requestHeaders.getFirst(CORRELATION_ID);
    }

    private ServerWebExchange setRequestHeader(ServerWebExchange exchange,String name,String value){
        return exchange.mutate().request(exchange.getRequest().mutate().header(name,value).build()).build();
    }
    public ServerWebExchange setCorrelationId(ServerWebExchange exchange,String correlationId){
        return this.setRequestHeader(exchange, CORRELATION_ID,correlationId);
    }

}
