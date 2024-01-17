package ru.sinitsynme.logistapi.toggle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "toggle")
public class Toggles {
    private boolean otpAuthTurnedOn;
}
