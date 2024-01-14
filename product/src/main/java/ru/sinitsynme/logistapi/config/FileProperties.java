package ru.sinitsynme.logistapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "file-config")
public class FileProperties {
    private int maxImageSizeMb;

    @Value("#{'${file-config.allowed-image-extensions-list}'.split(',')}")
    private List<String> allowedImageExtensions;
}
