package ru.sinitsynme.logistapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sinitsynme.logistapi.config.properties.AppProperties;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {

    private final AppProperties appProperties;

    @Autowired
    public ClockConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(appProperties.getClockZoneId()));
    }
}
