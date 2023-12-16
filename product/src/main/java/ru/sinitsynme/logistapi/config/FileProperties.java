package ru.sinitsynme.logistapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "file-config")
@ConstructorBinding
public class FileProperties {
    private String baseDirectory;
    private int maxImageSizeMb;

    @Value("#{'${file-config.allowed-image-extensions-list}'.split(',')}")
    private List<String> allowedImageExtensions;
}
