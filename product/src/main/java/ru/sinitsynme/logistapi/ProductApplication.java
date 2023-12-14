package ru.sinitsynme.logistapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.sinitsynme.logistapi.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}