package ru.sinitsynme.logistapi.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import ru.sinitsynme.logistapi.config.externalSystem.AuthServiceHostProperties;
import ru.sinitsynme.logistapi.config.externalSystem.ExternalSystemHostProvider;
import security.JwtAuthenticationFilter;
import security.token.AuthServiceClient;
import security.token.ServiceCredentials;
import security.token.TokenInterceptor;

import java.time.Clock;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final RestTemplate restTemplate;
    private final ExternalSystemHostProvider externalSystemHostProvider;
    private final AuthServiceHostProperties authServiceHostProperties;
    private final PermittedPaths permittedPaths;
    private final Clock clock;

    @Autowired
    public SecurityConfiguration(
            RestTemplate restTemplate,
            ExternalSystemHostProvider externalSystemHostProvider,
            AuthServiceHostProperties authServiceHostProperties,
            PermittedPaths permittedPaths,
            Clock clock) {
        this.restTemplate = restTemplate;
        this.externalSystemHostProvider = externalSystemHostProvider;
        this.authServiceHostProperties = authServiceHostProperties;
        this.permittedPaths = permittedPaths;
        this.clock = clock;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.GET, permittedPaths.getPaths()).permitAll()
                .anyRequest().authenticated()
        );
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement(mng -> mng.sessionCreationPolicy(STATELESS));

        return http.build();
    }


    @Bean
    @Order(1)
    public AuthServiceClient authServiceClient() {
        return new AuthServiceClient(
                restTemplate,
                externalSystemHostProvider.provideHost(
                        authServiceHostProperties.getServiceName(),
                        authServiceHostProperties.getUrl()
                )
        );
    }

    @Bean
    @Order(2)
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor(
                new ServiceCredentials(
                        authServiceHostProperties.getUsername(),
                        authServiceHostProperties.getPassword()
                ),
                authServiceClient(),
                clock
        );
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }


}
