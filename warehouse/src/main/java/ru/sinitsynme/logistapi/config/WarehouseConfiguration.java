package ru.sinitsynme.logistapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.sinitsynme.logistapi.config.externalSystem.AuthServiceHostProperties;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
@EnableConfigurationProperties({AppProperties.class, AuthServiceHostProperties.class})
public class WarehouseConfiguration {

    private final AppProperties appProperties;

    @Autowired
    public WarehouseConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(appProperties.getClockZoneId()));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
