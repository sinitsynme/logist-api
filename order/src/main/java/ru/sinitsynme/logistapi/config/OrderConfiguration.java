package ru.sinitsynme.logistapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sinitsynme.logistapi.config.externalSystem.AuthServiceHostProperties;
import ru.sinitsynme.logistapi.config.externalSystem.ProductServiceHostProperties;
import ru.sinitsynme.logistapi.config.externalSystem.WarehouseServiceHostProperties;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
@EnableConfigurationProperties({
        AppProperties.class,
        AuthServiceHostProperties.class,
        ProductServiceHostProperties.class,
        WarehouseServiceHostProperties.class
})
public class OrderConfiguration {

    private final AppProperties appProperties;

    @Autowired
    public OrderConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(appProperties.getClockZoneId()));
    }
}
