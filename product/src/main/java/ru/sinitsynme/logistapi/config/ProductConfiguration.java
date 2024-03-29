package ru.sinitsynme.logistapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sinitsynme.logistapi.config.externalSystem.AuthServiceHostProperties;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
@EnableConfigurationProperties({
        AppProperties.class,
        FileProperties.class,
        ObjectStorageProperties.class,
        AuthServiceHostProperties.class
})
public class ProductConfiguration {
    private final AppProperties appProperties;

    @Autowired
    public ProductConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(appProperties.getClockZoneId()));
    }

}
