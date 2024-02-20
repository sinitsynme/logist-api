package ru.sinitsynme.logistapi.config.security;

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

            "/rest/api/v1/product/**",
            "/rest/api/v1/product",
            "/rest/api/v1/product/category",
            "/rest/api/v1/product/category/**",
            "/rest/api/v1/manufacturer",
            "/rest/api/v1/manufacturer/**"
    };
}
