package ru.sinitsynme.logistapi.config.externalSystem;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ProductServiceHostProperties {
    @Value("${external-system.product.service-name}")
    private String productServiceName;
    @Value("${external-system.product.url}")
    private String productServiceUrl;
}
