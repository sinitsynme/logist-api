package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.mapper.StoredProductMapper;
import ru.sinitsynme.logistapi.rest.dto.StoredProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.StoredProductResponseDto;
import ru.sinitsynme.logistapi.service.StoredProductService;

import javax.validation.Valid;

@Tag(name = "Управление товарами на складах")
@RestController
@RequestMapping("/warehouse/product")
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
}
