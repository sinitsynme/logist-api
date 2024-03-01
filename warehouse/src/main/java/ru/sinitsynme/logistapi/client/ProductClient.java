package ru.sinitsynme.logistapi.client;

import dto.business.ProductResponseDto;
import exception.service.BadRequestException;
import exception.service.ServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.sinitsynme.logistapi.config.externalSystem.ExternalSystemHostProvider;
import ru.sinitsynme.logistapi.config.externalSystem.ProductServiceHostProperties;

import java.util.UUID;

import static exception.ExceptionSeverity.ERROR;
import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.EXTERNAL_RESOURCE_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.SERVER_ERROR_CODE;

@Component
public class ProductClient {
    private static final String GET_PRODUCT_PATH = "/rest/api/v1/product/{productId}";

    private final RestTemplate restTemplate;
    private final ProductServiceHostProperties productServiceHostProperties;
    private final ExternalSystemHostProvider hostProvider;
    private final Logger logger = LoggerFactory.getLogger(ProductClient.class);

    public ProductClient(
            RestTemplate restTemplate,
            ProductServiceHostProperties productServiceHostProperties,
            ExternalSystemHostProvider hostProvider
    ) {
        this.restTemplate = restTemplate;
        this.productServiceHostProperties = productServiceHostProperties;
        this.hostProvider = hostProvider;
    }

    public ProductResponseDto getProduct(UUID productId) throws BadRequestException {
        String host = getProductHost();

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
                    SERVER_ERROR_CODE,
                    ERROR
            );
        }

    }

    private String getProductHost() {
        return hostProvider.provideHost(
                productServiceHostProperties.getProductServiceName(),
                productServiceHostProperties.getProductServiceUrl()
        );
    }
}
