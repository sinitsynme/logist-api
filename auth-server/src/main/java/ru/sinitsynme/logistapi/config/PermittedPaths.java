package ru.sinitsynme.logistapi.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
