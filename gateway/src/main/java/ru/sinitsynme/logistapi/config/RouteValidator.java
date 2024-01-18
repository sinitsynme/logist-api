package ru.sinitsynme.logistapi.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/auth/signup",
            "/auth/token",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> OPEN_API_ENDPOINTS
            .stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
