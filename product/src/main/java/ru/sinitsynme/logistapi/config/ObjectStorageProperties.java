package ru.sinitsynme.logistapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "yandex.object-storage")
@ConstructorBinding
public class ObjectStorageProperties {

    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String awsRegion;
    private String endpoint;
}
