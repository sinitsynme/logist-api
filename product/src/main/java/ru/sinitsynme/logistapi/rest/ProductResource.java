package ru.sinitsynme.logistapi.rest;

import dto.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.entity.enums.ProductStatus;
import ru.sinitsynme.logistapi.mapper.ProductMapper;
import ru.sinitsynme.logistapi.rest.dto.ChangeProductStatusRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductImageLinkResponseDto;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductResponseDto;
import ru.sinitsynme.logistapi.service.ProductService;
import security.annotations.SupportAccess;

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
    @Operation(summary = "Получить страницу товаров со статусом APPROVED")
    public ResponseEntity<Page<ProductResponseDto>> getPageOfProducts(
            @Valid PageRequestDto pageRequestDto,
            @RequestParam(required = false) List<String> categoryCodes) {

        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);

        Page<ProductResponseDto> responseDtos = productService
                .getProductsPage(
                        ProductStatus.APPROVED,
                        pageRequestDto.toPageable(),
                        categoryCodes)
                .map(productMapper::toResponseDto);

        return ResponseEntity.ok(responseDtos);
    }


    @GetMapping("/admin")
    @SecurityRequirement(name = "Bearer Authentication")
    @SupportAccess
    @Operation(summary = "Получить страницу товаров по статусу [SUPPORT+]")
    public ResponseEntity<Page<ProductResponseDto>> getPageOfProductsAdmin(
            @RequestParam String status,
            @Valid PageRequestDto pageRequestDto,
            @RequestParam(required = false) List<String> categoryCodes) {

        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);
        ProductStatus productStatus = productService.parseProductStatusFromString(status);

        Page<ProductResponseDto> responseDtos = productService
                .getProductsPage(productStatus, pageRequestDto.toPageable(), categoryCodes)
                .map(productMapper::toResponseDto);

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/search")
    @Operation(summary = "Получить страницу APPROVED товаров по поисковому запросу")
    public ResponseEntity<Page<ProductResponseDto>> getPageOfProductsWithNameContaining(
            @Valid PageRequestDto pageRequestDto,
            @RequestParam @Valid @Size(min = 3, max = 100) String query) {

        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);

        Page<ProductResponseDto> responseDtos = productService
                .getProductsBySearchQueryInStatus(pageRequestDto.toPageable(), query, ProductStatus.APPROVED)
                .map(productMapper::toResponseDto);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/search/admin")
    @SupportAccess
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Получить страницу товаров по поисковому запросу [SUPPORT+]")
    public ResponseEntity<Page<ProductResponseDto>> getPageOfProductsWithNameContainingApproved(
            @RequestParam String status,
            @Valid PageRequestDto pageRequestDto,
            @RequestParam @Valid @Size(min = 3, max = 100) String query) {

        ProductStatus productStatus = productService.parseProductStatusFromString(status);
        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);

        Page<ProductResponseDto> responseDtos = productService
                .getProductsBySearchQueryInStatus(pageRequestDto.toPageable(), query, productStatus)
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

    @PatchMapping("/{id}")
    @Operation(summary = "Изменить статус товара")
    @SupportAccess
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProductResponseDto> changeProductStatus(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("id") UUID productId,
            @RequestBody ChangeProductStatusRequestDto requestDto) {
        Product product = productService.changeProductStatus(productId, requestDto, authHeader);
        return ResponseEntity.ok(productMapper.toResponseDto(product));
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

    private void updatePageRequestDtoIfSortIsEmpty(PageRequestDto pageRequestDto) {
        if (pageRequestDto.getSortByFields() == null || pageRequestDto.getSortByFields().length == 0) {
            pageRequestDto.setSortByFields(new String[]{"name"});
        }
    }
}
