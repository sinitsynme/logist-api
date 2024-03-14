package ru.sinitsynme.logistapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ExceptionHandlerConfiguration {

    private final ObjectMapper objectMapper;
    private final Clock clock;

    public ExceptionHandlerConfiguration(ObjectMapper objectMapper, Clock clock) {
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @Bean
    public GlobalExceptionHandler exceptionHandler() {
        return new GlobalExceptionHandler(objectMapper, clock);
    }
}
