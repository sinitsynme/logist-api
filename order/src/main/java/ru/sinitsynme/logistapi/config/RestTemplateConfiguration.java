package ru.sinitsynme.logistapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import security.token.TokenInterceptor;

import java.util.List;

@Configuration
public class RestTemplateConfiguration {

    private final TokenInterceptor tokenInterceptor;

    @Autowired
    public RestTemplateConfiguration(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(tokenInterceptor));
        return restTemplate;
    }
}
