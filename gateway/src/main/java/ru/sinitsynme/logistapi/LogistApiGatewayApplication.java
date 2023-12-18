package ru.sinitsynme.logistapi;

import ru.sinitsynme.logistapi.config.ServiceNameProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ServiceNameProperties.class})
public class LogistApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogistApiGatewayApplication.class, args);
    }

}
