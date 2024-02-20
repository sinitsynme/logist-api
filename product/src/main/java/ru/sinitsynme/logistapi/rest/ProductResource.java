package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.mapper.ProductMapper;
import ru.sinitsynme.logistapi.rest.dto.ProductImageLinkResponseDto;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductResponseDto;
import ru.sinitsynme.logistapi.service.ProductService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Управление товарами")
@RestController
@RequestMapping("/rest/api/v1/product")
public class ProductResource {
    private final ProductMapper productMapper;
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductResource.class);

    public ProductResource(ProductMapper productMapper, ProductService productService) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Получить список товаров")
    public ResponseEntity<Page<ProductResponseDto>> getPageOfProducts(
            @RequestParam @Valid @Min(0) int page,
            @RequestParam @Valid @Min(1) int size,
            @RequestParam(required = false) List<String> sortByFields,
            @RequestParam(required = false) List<String> categoryCodes) {
        Page<ProductResponseDto> responseDtos = productService
                .getProductsPage(page, size, categoryCodes, sortByFields)
                .map(productMapper::toResponseDto);

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/search")
    @Operation(summary = "Получить список товаров по поисковому запросу")
    public ResponseEntity<Page<ProductResponseDto>> getPageOfProductsWithNameContaining(
            @RequestParam @Valid @Min(0) int page,
            @RequestParam @Valid @Min(1) int size,
            @RequestParam @Valid @Size(min = 3, max = 100) String query) {
        Page<ProductResponseDto> responseDtos = productService
                .getProductsPageWithNameContaining(page, size, query)
                .map(productMapper::toResponseDto);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") UUID productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(productMapper.toResponseDto(product));
    }

    @PostMapping
    @Operation(summary = "Сохранить товар")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestBody @Valid ProductRequestDto productRequestDto) {
        Product product = productService.saveProduct(productRequestDto);
        logger.info("Saved product: {}", product);
        return ResponseEntity.ok(productMapper.toResponseDto(product));
    }

    @GetMapping("/{id}/image")
    @Operation(summary = "Получить ссылку на изображение товара")
    @ResponseBody
    public ResponseEntity<ProductImageLinkResponseDto> getProductImage(@PathVariable("id") UUID productId) {
        String link = productService.getLinkToProductImage(productId);
        ProductImageLinkResponseDto responseDto = new ProductImageLinkResponseDto(link);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавить изображение к товару")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> addImageToProduct(
            @RequestPart MultipartFile productImageFile,
            @PathVariable("id") UUID productId) {
        productService.uploadImageToProduct(productId, productImageFile);
        logger.info("Added image to product with ID = {}", productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать товар")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProductResponseDto> editProduct(@RequestBody ProductRequestDto productRequestDto,
                                                          @PathVariable("id") UUID productId) {
        Product product = productService.editProduct(productId, productRequestDto);
        logger.info("Edited product with ID = {}", productId);
        return ResponseEntity.ok(productMapper.toResponseDto(product));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") UUID productId) {
        productService.deleteProduct(productId);
        logger.info("Deleted product with ID = {}", productId);
        return ResponseEntity.ok().build();
    }
}
