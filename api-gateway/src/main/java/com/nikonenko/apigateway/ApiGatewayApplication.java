package com.nikonenko.apigateway;

import com.nikonenko.apigateway.configs.AuthenticationFilter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/book-service/v3/api-docs")
                        .and().method(HttpMethod.GET).uri("lb://book-service"))
                .route(r -> r.path("/library-service/v3/api-docs")
                        .and().method(HttpMethod.GET).uri("lb://library-service"))
                .route(r -> r.path("/auth-service/v3/api-docs")
                        .and().method(HttpMethod.GET).uri("lb://authentication-service"))
                .route("book-service", r -> r.path("/api/book")
                        .filters(f -> f.filter(filter))
                        .uri("lb://book-service"))
                .route("library-service", r -> r.path("/api/note")
                        .filters(f -> f.filter(filter))
                        .uri("lb://library-service"))
                .route("discovery-server", r -> r.path("/eureka/web")
                        .filters(f -> f.setPath("/"))
                        .uri("http://localhost:8761/"))
                .route("discovery-server-resources", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761/"))
                .route("authentication-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://authentication-service"))
                .build();
    }
}
