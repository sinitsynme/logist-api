package ru.sinitsynme.logistapi.config.security;

import exception.service.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.sinitsynme.logistapi.config.RouteValidator;
import ru.sinitsynme.logistapi.config.ServiceNameProperties;

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter> {

    private static final String VALIDATE_TOKEN_PATH = "lb://%s/validate";
    private static final String TOKEN_PARAM = "token";
    private final RouteValidator routeValidator;
    private final WebClient webClient;
    private final ServiceNameProperties serviceNameProperties;

    @Autowired
    public AuthenticationFilter(RouteValidator routeValidator,
                                WebClient webClient,
                                ServiceNameProperties serviceNameProperties) {
        this.routeValidator = routeValidator;
        this.webClient = webClient;
        this.serviceNameProperties = serviceNameProperties;
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter config) {
        return ((exchange, chain) -> {

            String authServiceName = serviceNameProperties.getAuthServiceName();

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedException();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                final String tokenValue = authHeader;

                webClient
                        .get()
                        .uri(
                                uriBuilder -> uriBuilder
                                        .path(String.format(VALIDATE_TOKEN_PATH, authServiceName))
                                        .queryParam(TOKEN_PARAM, tokenValue)
                                        .build()
                        )
                        .retrieve()
                        .bodyToMono(String.class)
                        .onErrorResume(e -> Mono.error(UnauthorizedException::new))
                        .block();
            }

            return chain.filter(exchange);
        });
    }
}
