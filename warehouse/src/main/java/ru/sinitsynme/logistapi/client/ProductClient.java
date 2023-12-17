package ru.sinitsynme.logistapi.client;

import dto.ProductResponseDto;
import exception.service.BadRequestException;
import exception.service.ServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.sinitsynme.logistapi.config.externalSystem.ProductServiceHostProvider;

import java.util.UUID;

import static exception.ExceptionSeverity.ERROR;
import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.EXTERNAL_RESOURCE_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.SERVER_ERROR;

@Component
public class ProductClient {
    private static final String GET_PRODUCT_PATH = "/product/{productId}";

    private final RestTemplate restTemplate;
    private final ProductServiceHostProvider hostProvider;
    private final Logger logger = LoggerFactory.getLogger(ProductClient.class);

    public ProductClient(RestTemplate restTemplate, ProductServiceHostProvider hostProvider) {
        this.restTemplate = restTemplate;
        this.hostProvider = hostProvider;
    }

    public ProductResponseDto getProduct(UUID productId) throws BadRequestException {
        String host = hostProvider.provideHost();

        logger.info("HTTP Request - GET Product with ID = {}", productId);

        try {
            ProductResponseDto responseDto = restTemplate.getForObject(
                    host + GET_PRODUCT_PATH,
                    ProductResponseDto.class,
                    productId.toString()
            );

            logger.info("HTTP Response - {}", responseDto);

            return responseDto;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new BadRequestException(
                        e.getMessage(),
                        e,
                        BAD_REQUEST,
                        EXTERNAL_RESOURCE_NOT_FOUND_CODE,
                        WARN
                );
            } else throw new ServerErrorException(
                    e.getMessage(),
                    e,
                    INTERNAL_SERVER_ERROR,
                    SERVER_ERROR,
                    ERROR
            );
        }

    }
}
