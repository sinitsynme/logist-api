package ru.sinitsynme.logistapi.config.externalSystem;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class ExternalSystemHostProvider {

    private final DiscoveryClient discoveryClient;

    public ExternalSystemHostProvider(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String provideHost(String serviceName, String defaultUrl) {
        Optional<ServiceInstance> instance = discoveryClient.getInstances(serviceName)
                .stream()
                .findAny();

        if (instance.isPresent()) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(instance.get().getUri().toString());
            return uriComponentsBuilder.toUriString();

        } else return defaultUrl;
    }
}
