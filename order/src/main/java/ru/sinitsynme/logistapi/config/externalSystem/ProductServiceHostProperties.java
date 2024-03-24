package ru.sinitsynme.logistapi.config.externalSystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external-system.product")
@Getter
@AllArgsConstructor
public class ProductServiceHostProperties {
    private String url;
    private String serviceName;
}
