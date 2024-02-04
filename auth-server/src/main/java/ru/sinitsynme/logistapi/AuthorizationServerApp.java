package ru.sinitsynme.logistapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.sinitsynme.logistapi.config.properties.AppProperties;
import ru.sinitsynme.logistapi.config.properties.BCryptProperties;
import ru.sinitsynme.logistapi.config.properties.JwtProperties;
import ru.sinitsynme.logistapi.config.properties.MasterUserProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        AppProperties.class,
        BCryptProperties.class,
        JwtProperties.class,
        MasterUserProperties.class
})
public class AuthorizationServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApp.class, args);
    }
}