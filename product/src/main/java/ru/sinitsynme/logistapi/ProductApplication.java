package ru.sinitsynme.logistapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.sinitsynme.logistapi.config.AppProperties;
import ru.sinitsynme.logistapi.config.FileProperties;
import ru.sinitsynme.logistapi.config.ObjectStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class, FileProperties.class, ObjectStorageProperties.class})
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}