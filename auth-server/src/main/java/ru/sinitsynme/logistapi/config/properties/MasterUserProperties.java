package ru.sinitsynme.logistapi.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "master-user")
public class MasterUserProperties {
    private String email;
    private String password;
}
