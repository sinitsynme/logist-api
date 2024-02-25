package ru.sinitsynme.logistapi.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.sinitsynme.logistapi.config.ServiceNameProperties;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String VALIDATE_TOKEN_PATH = "/rest/api/v1/token/validate";
    private static final String TOKEN_PARAM = "accessToken";
    private static final String BEARER_PREFIX = "Bearer ";
    private final WebClient webClient;
    private final ServiceNameProperties serviceNameProperties;
    private final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    public AuthenticationFilter(WebClient.Builder webClientBuilder, ServiceNameProperties serviceNameProperties) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
        this.serviceNameProperties = serviceNameProperties;
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        log.info("Entered AuthenticationFilter");
        return ((exchange, chain) -> {

            String authServiceName = serviceNameProperties.getAuthServiceName();

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                return chain.filter(exchange);
            }

            final String tokenValue = authHeader.substring(7);

            log.info("Call AuthServer to validate token");

            return webClient
                    .get()
                    .uri(
                            uriBuilder -> uriBuilder
                                    .host(authServiceName)
                                    .path(VALIDATE_TOKEN_PATH)
                                    .queryParam(TOKEN_PARAM, tokenValue)
                                    .build()
                    )
                    .exchange()
                    .flatMap(response -> {
                        if (response.statusCode().isError()) {
                            log.warn("Token validation resulted in exception");
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        } else {
                            log.info("Successful validation of token");
                            return chain.filter(exchange);
                        }
                    });
        });
    }

    public static class Config {
    }
}
