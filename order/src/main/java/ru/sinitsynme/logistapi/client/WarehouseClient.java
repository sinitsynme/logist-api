package ru.sinitsynme.logistapi.client;

import dto.business.warehouse.StoredProductResponseDto;
import exception.service.BadRequestException;
import exception.service.ServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.sinitsynme.logistapi.config.externalSystem.ExternalSystemHostProvider;
import ru.sinitsynme.logistapi.config.externalSystem.WarehouseServiceHostProperties;

import java.util.UUID;

import static exception.ExceptionSeverity.ERROR;
import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.EXTERNAL_RESOURCE_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.SERVER_ERROR_CODE;

@Component
public class WarehouseClient {

    private static final String GET_STORED_PRODUCT_PATH = "/rest/api/v1/warehouse/product/id?productId={productId}&warehouseId={warehouseId}";
    private final RestTemplate restTemplate;
    private final WarehouseServiceHostProperties hostProperties;
    private final ExternalSystemHostProvider hostProvider;
    private final Logger logger = LoggerFactory.getLogger(WarehouseClient.class);

    @Autowired
    public WarehouseClient(
            RestTemplate restTemplate,
            WarehouseServiceHostProperties warehouseServiceHostProperties,
            ExternalSystemHostProvider externalSystemHostProvider) {
        this.restTemplate = restTemplate;
        this.hostProperties = warehouseServiceHostProperties;
        this.hostProvider = externalSystemHostProvider;
    }

    public StoredProductResponseDto getStoredProduct(UUID productId, Long warehouseId) {
        String host = getWarehouseHost();

        logger.info("[Warehouse] HTTP Request - GET StoredProduct with productId = {}, warehouseId = {}",
                productId,
                warehouseId);

        try {
            StoredProductResponseDto responseDto = restTemplate.getForObject(
                    host + GET_STORED_PRODUCT_PATH,
                    StoredProductResponseDto.class,
                    productId.toString(),
                    warehouseId.toString()
            );

            logger.info("[Warehouse] HTTP Response - {}", responseDto);

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


    private String getWarehouseHost() {
        return hostProvider.provideHost(
                hostProperties.getServiceName(),
                hostProperties.getUrl()
        );
    }
}
