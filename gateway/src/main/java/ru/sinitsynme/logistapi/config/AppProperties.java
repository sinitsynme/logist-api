package ru.sinitsynme.logistapi.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logist-api")
public class AppProperties {
    private String clockZoneId;

    public String getClockZoneId() {
        return clockZoneId;
    }

    public void setClockZoneId(String clockZoneId) {
        this.clockZoneId = clockZoneId;
    }
}
