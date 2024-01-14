package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.mapper.StoredProductMapper;
import ru.sinitsynme.logistapi.rest.dto.StoredProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.StoredProductResponseDto;
import ru.sinitsynme.logistapi.service.StoredProductService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Управление товарами на складах")
@RestController
@RequestMapping("/rest/api/v1/warehouse/product")
public class StoredProductResource {

    private final Logger logger = LoggerFactory.getLogger(StoredProductResource.class);
    private final StoredProductService service;
    private final StoredProductMapper storedProductMapper;

    public StoredProductResource(StoredProductService service, StoredProductMapper storedProductMapper) {
        this.service = service;
        this.storedProductMapper = storedProductMapper;
    }

    @PostMapping
    @Operation(summary = "Добавить товары на склад")
    public ResponseEntity<StoredProductResponseDto> addProductToWarehouse(@RequestBody @Valid StoredProductRequestDto requestDto) {
        StoredProduct storedProduct = service.addStoredProductToWarehouse(requestDto);
        logger.info("Product {} added to warehouse {}, now quantity {}",
                storedProduct.getId().getProductId(),
                storedProduct.getId().getWarehouse().getId(),
                storedProduct.getQuantity()
        );
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @GetMapping("/id")
    @Operation(summary = "Получить товар со склада по ID")
    public ResponseEntity<StoredProductResponseDto> getProductFromWarehouse(
            @RequestParam UUID productId,
            @RequestParam @Valid @Min(1) Long warehouseId) {
        StoredProduct storedProduct = service.getStoredProduct(productId, warehouseId);
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @PutMapping("/reserve")
    @Operation(summary = "Зарезервировать товар на складе")
    public ResponseEntity<StoredProductResponseDto> reserveProductInWarehouse(@RequestBody @Valid StoredProductRequestDto requestDto) {
        StoredProduct storedProduct = service.reserveProductInWarehouse(requestDto);
        logger.info("Products with ID = {} reserved at warehouse with ID = {} in quantity {}",
                requestDto.getProductId(),
                requestDto.getWarehouseId(),
                requestDto.getQuantity()
        );
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @PutMapping("/remove")
    @Operation(summary = "Удалить зарезервированный товар со склада")
    public ResponseEntity<StoredProductResponseDto> removeProductFromWarehouse(@RequestBody @Valid StoredProductRequestDto requestDto) {
        StoredProduct storedProduct = service.removeReservedProductFromWarehouse(requestDto);
        logger.info("Reserved products with ID = {} removed from warehouse with ID = {} in quantity {}",
                requestDto.getProductId(),
                requestDto.getWarehouseId(),
                requestDto.getQuantity()
        );
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }


    @GetMapping
    @Operation(summary = "Получить товары на складе")
    public ResponseEntity<List<StoredProductResponseDto>> getProductsFromWarehouse(
            @Valid @Min(0) int page, @Valid @Min(1) int size, @Valid @Min(1) Long warehouseId
    ) {
        List<StoredProductResponseDto> responseDtoList = service
                .getListOfStoredProductsAtWarehouse(page, size, warehouseId)
                .stream()
                .map(storedProductMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(responseDtoList);
    }
}
