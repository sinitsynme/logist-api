package ru.sinitsynme.logistapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "bcrypt")
public class BCryptProperties {

    private int encryptionRounds;
}
