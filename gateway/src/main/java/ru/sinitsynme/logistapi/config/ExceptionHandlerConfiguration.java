package ru.sinitsynme.logistapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ExceptionHandlerConfiguration {
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Autowired
    public ExceptionHandlerConfiguration(ObjectMapper objectMapper, Clock clock) {
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public GlobalExceptionHandler exceptionHandler() {
        return new GlobalExceptionHandler(objectMapper, clock);
    }
}
