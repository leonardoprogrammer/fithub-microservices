package com.fithub.gateway_service.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("http://localhost:8081"))
                .route("user-service", r -> r.path("/user/**")
                        .uri("http://localhost:8082"))
                .route("workout-service", r -> r.path("/workout/**")
                        .uri("http://localhost:8083"))
                .route("history-service", r -> r.path("/history/**")
                        .uri("http://localhost:8084"))
                .route("sharing-service", r -> r.path("/sharing/**")
                        .uri("http://localhost:8085"))
                .route("dashboard-service", r -> r.path("/dashboard/**")
                        .uri("http://localhost:8086"))
                .route("notification-service", r -> r.path("/notification/**")
                        .uri("http://localhost:8087"))
                .build();
    }
}
