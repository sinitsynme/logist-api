package ru.sinitsynme.logistapi.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PermittedPaths {

    private final String[] paths = new String [] {

        // SWAGGER PATHS
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/v3/api-docs/**",

        // SERVICE PATHS
        "/token/**",
        "/signup/client"
    };
}
