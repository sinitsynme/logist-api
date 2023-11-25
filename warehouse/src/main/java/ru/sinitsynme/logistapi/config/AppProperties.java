package ru.sinitsynme.logistapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "logist-api")
@ConstructorBinding
public class AppProperties {
    private String clockZoneId;
}
