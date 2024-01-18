package ru.sinitsynme.logistapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.sinitsynme.logistapi.config.AppProperties;
import ru.sinitsynme.logistapi.config.BCryptProperties;
import ru.sinitsynme.logistapi.config.JwtProperties;
import ru.sinitsynme.logistapi.toggle.Toggles;

@SpringBootApplication
@EnableConfigurationProperties({
        AppProperties.class,
        BCryptProperties.class,
        Toggles.class,
        JwtProperties.class
})
public class AuthorizationServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApp.class, args);
    }
}