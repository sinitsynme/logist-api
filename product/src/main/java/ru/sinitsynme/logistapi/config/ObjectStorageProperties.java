package ru.sinitsynme.logistapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "yandex.object-storage")
public class ObjectStorageProperties {

    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String awsRegion;
    private String endpoint;
}
