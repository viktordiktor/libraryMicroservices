package com.nikonenko.apigateway.configs;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {

    public static final List<String> openEndpoints = List.of(
            "/api/auth/register",
            "/api/v1/auth/",
            "v3/api-docs/",
            "v3/api-docs.yaml",
            "/swagger-ui/",
            "/api/auth/refresh-token",
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
