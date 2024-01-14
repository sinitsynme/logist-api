package ru.sinitsynme.logistapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "logist-api")
public class AppProperties {
    private String clockZoneId;
    private int initialWarehouseCodeCounter;
}
