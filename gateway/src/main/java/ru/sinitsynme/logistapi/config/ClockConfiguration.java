package ru.sinitsynme.logistapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {
    private final AppProperties appProperties;

    public ClockConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(appProperties.getClockZoneId()));
    }

}
