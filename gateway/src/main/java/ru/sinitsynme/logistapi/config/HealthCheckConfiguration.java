package ru.sinitsynme.logistapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.logging.Level.FINE;

@Configuration
public class HealthCheckConfiguration {
    private final Logger log = LoggerFactory.getLogger(HealthCheckConfiguration.class);
    private final WebClient webClient;
    private final ServiceNameProperties serviceNameProperties;

    @Autowired
    public HealthCheckConfiguration(
            WebClient.Builder webClientBuilder,
            ServiceNameProperties serviceNameProperties) {
        this.webClient = webClientBuilder.build();
        this.serviceNameProperties = serviceNameProperties;
    }

    @Bean
    ReactiveHealthContributor healthcheckMicroservices() {

        final Map<String, ReactiveHealthIndicator> registry = new LinkedHashMap<>();

        serviceNameProperties.getAllServiceNames()
                .forEach(serviceName -> putServiceInHealthRegistry(registry, serviceName));

        return CompositeReactiveHealthContributor.fromMap(registry);
    }

    private void putServiceInHealthRegistry(Map<String, ReactiveHealthIndicator> registry, String serviceName) {
        registry.put(serviceName,
                () -> getHealth(String.format("http://%s", serviceName))
        );
    }

    private Mono<Health> getHealth(String baseUrl) {
        String url = baseUrl + "/actuator/health";
        log.debug("Setting up a call to the Health API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log(log.getName(), FINE);
    }
}
