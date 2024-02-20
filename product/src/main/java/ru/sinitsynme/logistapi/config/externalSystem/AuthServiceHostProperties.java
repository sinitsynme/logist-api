package ru.sinitsynme.logistapi.config.externalSystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external-system.auth")
@Getter
@AllArgsConstructor
public class AuthServiceHostProperties {
    private String serviceName;
    private String url;
    private String username;
    private String password;
}
