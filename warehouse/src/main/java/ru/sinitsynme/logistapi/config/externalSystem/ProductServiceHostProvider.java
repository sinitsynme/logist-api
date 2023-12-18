package ru.sinitsynme.logistapi.config.externalSystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class ProductServiceHostProvider {
    @Value("${external-system.product.service-name}")
    private String productServiceName;
    @Value("${external-system.product.url}")
    private String productServiceUrl;
    private final DiscoveryClient discoveryClient;

    public ProductServiceHostProvider(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String provideHost() {
        Optional<ServiceInstance> instance = discoveryClient.getInstances(productServiceName)
                .stream()
                .findAny();

        if (instance.isPresent()) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(instance.get().getUri().toString());
            return uriComponentsBuilder.toUriString();

        } else return productServiceUrl;
    }
}
