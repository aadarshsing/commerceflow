package com.gatewayServer;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CustomRouteLocator {

    @Bean
    public RouteLocator commerceFlowRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route(predicateSpec -> predicateSpec.path("/commerceflow/customer/**")
                    .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/customer/(?<segment>.*)","/${segment}"))
                    .uri("lb://CUSTOMER"))
                .route(predicateSpec -> predicateSpec.path("/commerceflow/payment/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/payment/(?<segment>.*)","/${segment}"))
                        .uri("lb://PAYMENT"))
                .route(predicateSpec -> predicateSpec.path("/commerceflow/order/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/order/(?<segment>.*)","/${segment}"))
                        .uri("lb://ORDER"))
                .route(predicateSpec -> predicateSpec.path("/commerceflow/notification/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/notification/(?<segment>.*)","/${segment}"))
                        .uri("lb://NOTIFICATION"))
                .route(predicateSpec -> predicateSpec.path("/commerceflow/catalog/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/catalog/(?<segment>.*)","/${segment}"))
                        .uri("lb://CATALOG"))
                .route(predicateSpec -> predicateSpec.path("/commerceflow/inventory/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/inventory/(?<segment>.*)","/${segment}"))
                        .uri("lb://INVENTORY"))
                .route(predicateSpec -> predicateSpec.path("/commerceflow/shipment/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/shipment/(?<segment>.*)","/${segment}"))
                        .uri("lb://SHIPMENT"))
                .route(predicateSpec -> predicateSpec.path("/commerceflow/cart/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/commerceflow/cart/(?<segment>.*)","/${segment}"))
                        .uri("lb://CART"))

                .build();
    }
}
