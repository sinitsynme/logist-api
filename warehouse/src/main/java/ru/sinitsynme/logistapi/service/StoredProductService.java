package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsynme.logistapi.client.ProductClient;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.entity.StoredProductId;
import ru.sinitsynme.logistapi.entity.Warehouse;
import ru.sinitsynme.logistapi.mapper.StoredProductMapper;
import ru.sinitsynme.logistapi.repository.StoredProductRepository;
import ru.sinitsynme.logistapi.rest.dto.StoredProductActionRequestDto;
import ru.sinitsynme.logistapi.rest.dto.StoredProductRequestDto;

import java.util.Optional;
import java.util.UUID;

import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.*;

@Service
public class StoredProductService {

    private final Logger logger = LoggerFactory.getLogger(StoredProductService.class);
    private final StoredProductRepository storedProductRepository;
    private final WarehouseService warehouseService;
    private final StoredProductMapper storedProductMapper;
    private final ProductClient productClient;

    @Autowired
    public StoredProductService(StoredProductRepository storedProductRepository,
                                WarehouseService warehouseService,
                                StoredProductMapper storedProductMapper,
                                ProductClient productClient) {
        this.storedProductRepository = storedProductRepository;
        this.warehouseService = warehouseService;
        this.storedProductMapper = storedProductMapper;
        this.productClient = productClient;
    }

    @Transactional
    public StoredProduct registerProductInWarehouse(StoredProductRequestDto requestDto) {
        Warehouse warehouse = warehouseService.getWarehouse(requestDto.getWarehouseId());

        StoredProductId possiblyExistingStoredProductId = new StoredProductId(
                requestDto.getProductId(),
                warehouse
        );

        Optional<StoredProduct> possiblyExistingStoredProduct = storedProductRepository
                .findById(possiblyExistingStoredProductId);

        if (possiblyExistingStoredProduct.isPresent()) {
            throw new BadRequestException(
                    String.format(
                            "Stored product with id = %s exists in warehouse with id = %s",
                            requestDto.getProductId(),
                            requestDto.getWarehouseId()),
                    null,
                    HttpStatus.BAD_REQUEST,
                    STORED_PRODUCT_EXISTS,
                    ExceptionSeverity.WARN
            );
        }

        productClient.getProduct(requestDto.getProductId());

        StoredProduct storedProduct = storedProductMapper.toEntity(requestDto);
        storedProduct.setQuantity(0);
        storedProduct.setWarehouseCode(warehouse.getStoredProductsCodeCounterString());

        warehouseService.increaseWarehouseProductCodeCounter(warehouse.getId());

        StoredProductId id = new StoredProductId(requestDto.getProductId(), warehouse);
        storedProduct.setId(id);

        return storedProductRepository.save(storedProduct);
    }

    @Transactional
    public StoredProduct addStoredProductToWarehouse(StoredProductActionRequestDto requestDto) {
        StoredProduct storedProduct = getStoredProduct(requestDto.getProductId(), requestDto.getWarehouseId());

        synchronized (this) {
            storedProduct.setQuantity(storedProduct.getQuantity() + requestDto.getQuantity());
            return storedProductRepository.save(storedProduct);
        }
    }

    public StoredProduct reserveProductInWarehouse(StoredProductActionRequestDto requestDto) {
        StoredProduct storedProduct = getStoredProduct(requestDto.getProductId(), requestDto.getWarehouseId());
        int requestedReservedQuantity = requestDto.getQuantity();
        int availableQuantity = storedProduct.getQuantity();
        int reservedQuantity = storedProduct.getReservedQuantity();

        synchronized (this) {
            if (availableQuantity - reservedQuantity < requestedReservedQuantity) {
                throw new BadRequestException
                        (
                                String.format(
                                        "Not enough product with ID = %s to reserve in warehouse with ID = %d. " +
                                                "Available to reserve: %d; Requested: %d",
                                        requestDto.getProductId(),
                                        requestDto.getWarehouseId(),
                                        availableQuantity - reservedQuantity,
                                        requestedReservedQuantity),
                                null,
                                HttpStatus.BAD_REQUEST,
                                NOT_ENOUGH_STORED_PRODUCT_TO_RESERVE,
                                ExceptionSeverity.WARN
                        );
            }
            storedProduct.setReservedQuantity(storedProduct.getReservedQuantity() + requestedReservedQuantity);
            return storedProductRepository.save(storedProduct);
        }
    }

    public StoredProduct removeReservedProductFromWarehouse(StoredProductActionRequestDto requestDto) {
        StoredProduct storedProduct = getStoredProduct(requestDto.getProductId(), requestDto.getWarehouseId());
        int reservedQuantity = storedProduct.getReservedQuantity();
        int requestedToCancelReserveQuantity = requestDto.getQuantity();

        synchronized (this) {
            if (reservedQuantity < requestedToCancelReserveQuantity) {
                throw new BadRequestException
                        (
                                String.format(
                                        "Not enough reserved products with ID = %s to cancel reserve in warehouse with ID = %d. " +
                                                "Available reserved: %d; Requested to cancel: %d",
                                        requestDto.getProductId(),
                                        requestDto.getWarehouseId(),
                                        reservedQuantity,
                                        requestedToCancelReserveQuantity),
                                null,
                                HttpStatus.BAD_REQUEST,
                                NOT_ENOUGH_RESERVED_STORED_PRODUCT_TO_REMOVE,
                                ExceptionSeverity.WARN
                        );
            }
            storedProduct.setReservedQuantity(reservedQuantity - requestedToCancelReserveQuantity);
            return storedProductRepository.save(storedProduct);
        }
    }

    public StoredProduct updateStoredProductPriceAndQuantum(StoredProductRequestDto requestDto) {
        StoredProduct storedProduct = getStoredProduct(requestDto.getProductId(), requestDto.getWarehouseId());

        storedProduct.setPrice(requestDto.getPrice());
        storedProduct.setQuantum(requestDto.getQuantum());

        storedProductRepository.save(storedProduct);

        logger.info("Product with id = {} at warehouse with id = {} was updated: price now is {}, quantum now is {}",
                requestDto.getProductId(),
                requestDto.getWarehouseId(),
                requestDto.getPrice(),
                requestDto.getQuantum()
        );
        return storedProduct;
    }

    public Page<StoredProduct> getListOfStoredProductsAtWarehouse(Pageable pageable, Long warehouseId) {
        return storedProductRepository.findByWarehouseId(warehouseId, pageable);
    }

    public StoredProduct getStoredProduct(UUID productId, Long warehouseId) {
        return storedProductRepository
                .findByProductAndWarehouse(productId, warehouseId)
                .orElseThrow(() -> new BadRequestException
                        (
                                String.format("Product with id = %s wasn't registered in warehouse with id = %d",
                                        productId,
                                        warehouseId),
                                null,
                                HttpStatus.BAD_REQUEST,
                                STORED_PRODUCT_NOT_FOUND_CODE,
                                ExceptionSeverity.WARN
                        )
                );
    }

}
