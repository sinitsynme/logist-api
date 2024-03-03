package ru.sinitsynme.logistapi.rest;

import dto.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.mapper.StoredProductMapper;
import ru.sinitsynme.logistapi.rest.dto.*;
import ru.sinitsynme.logistapi.service.StoredProductService;
import ru.sinitsynme.logistapi.service.SupplyService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Управление товарами на складах")
@RestController
@RequestMapping("/rest/api/v1/warehouse/product")
public class StoredProductResource {

    private final Logger logger = LoggerFactory.getLogger(StoredProductResource.class);
    private final StoredProductService storedProductService;
    private final SupplyService supplyService;
    private final StoredProductMapper storedProductMapper;

    public StoredProductResource(StoredProductService service,
                                 SupplyService supplyService,
                                 StoredProductMapper storedProductMapper) {
        this.storedProductService = service;
        this.supplyService = supplyService;
        this.storedProductMapper = storedProductMapper;
    }

    @PostMapping
    @Operation(summary = "Зарегистрировать новый товар на складе")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StoredProductResponseDto> registerProductInWarehouse(
            @RequestBody @Valid StoredProductRequestDto requestDto) {
        StoredProduct storedProduct = storedProductService.registerProductInWarehouse(requestDto);
        logger.info("Product {} registered to warehouse {} with warehouseCode {}, now quantity {}",
                storedProduct.getId().getProductId(),
                storedProduct.getId().getWarehouse().getId(),
                storedProduct.getWarehouseCode(),
                storedProduct.getQuantity()
        );
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @PatchMapping
    @Operation(summary = "Обновить стоимость и квант товара")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StoredProductResponseDto> updatePriceAndQuantumOfProductInWarehouse(
            @RequestBody @Valid StoredProductRequestDto requestDto
    ) {
        StoredProduct storedProduct = storedProductService.updateStoredProductPriceAndQuantum(requestDto);
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @PatchMapping("/add")
    @Operation(summary = "Добавить зарегистрированные товары на склад")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StoredProductResponseDto> addProductToWarehouse(
            @RequestBody @Valid StoredProductActionRequestDto requestDto) {
        StoredProduct storedProduct = storedProductService.addStoredProductToWarehouse(requestDto);
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
        StoredProduct storedProduct = storedProductService.getStoredProduct(productId, warehouseId);
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @GetMapping("/minimalPrice")
    @Operation(summary = "Получить информацию о минимальной стоимости товаров по ID")
    public ResponseEntity<List<AvailableStoredProductMinPricesDto>> getStoredProductsMinPrice(
            @RequestParam List<UUID> productIds
    ) {
        return ResponseEntity.ok(supplyService.getAvailableStoredProductsMinPrices(productIds));
    }

    @GetMapping("/{id}/info")
    @Operation(summary = "Получить информацию о наличии на складах товара по ID")
    public ResponseEntity<StoredProductInfoResponseDto> getFullStoredProductInfo(
            @PathVariable("id") UUID productId
    ) {
        return ResponseEntity.ok(supplyService.getFullStoredProductInfo(productId));
    }

    @PatchMapping("/reserve")
    @Operation(summary = "Зарезервировать товар на складе")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StoredProductResponseDto> reserveProductInWarehouse(
            @RequestBody @Valid StoredProductActionRequestDto requestDto) {
        StoredProduct storedProduct = storedProductService.reserveProductInWarehouse(requestDto);
        logger.info("Products with ID = {} reserved at warehouse with ID = {} in quantity {}",
                requestDto.getProductId(),
                requestDto.getWarehouseId(),
                requestDto.getQuantity()
        );
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @PatchMapping("/remove")
    @Operation(summary = "Удалить зарезервированный товар со склада")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StoredProductResponseDto> removeProductFromWarehouse(
            @RequestBody @Valid StoredProductActionRequestDto requestDto) {
        StoredProduct storedProduct = storedProductService.removeReservedProductFromWarehouse(requestDto);
        logger.info("Reserved products with ID = {} removed from warehouse with ID = {} in quantity {}",
                requestDto.getProductId(),
                requestDto.getWarehouseId(),
                requestDto.getQuantity()
        );
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }

    @PatchMapping("/reserve/cancel")
    @Operation(summary = "Снять резерв с товара со склада")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StoredProductResponseDto> cancelReserveFromProductInWarehouse(
            @RequestBody @Valid StoredProductActionRequestDto requestDto) {
        StoredProduct storedProduct = storedProductService.removeReservedProductFromWarehouse(requestDto);
        logger.info("Reserve for products with ID = {} from warehouse with ID = {} in quantity {} was canceled",
                requestDto.getProductId(),
                requestDto.getWarehouseId(),
                requestDto.getQuantity()
        );
        return ResponseEntity.ok(storedProductMapper.toResponseDto(storedProduct));
    }


    @GetMapping
    @Operation(summary = "Получить страницу товаров на складе")
    public ResponseEntity<Page<StoredProductResponseDto>> getProductsFromWarehouse(
            @Valid PageRequestDto pageRequestDto,
            @Valid @Min(1) Long warehouseId
    ) {
        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);

        Page<StoredProductResponseDto> responseDtoList = storedProductService
                .getListOfStoredProductsAtWarehouse(pageRequestDto.toPageable(), warehouseId)
                .map(storedProductMapper::toResponseDto);
        return ResponseEntity.ok(responseDtoList);
    }

    private void updatePageRequestDtoIfSortIsEmpty(PageRequestDto pageRequestDto) {
        if (pageRequestDto.getSortByFields() == null || pageRequestDto.getSortByFields().length == 0) {
            pageRequestDto.setSortByFields(new String[]{"warehouse_code"});
        }
    }
}
