package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.ProductCategory;
import ru.sinitsynme.logistapi.mapper.ProductCategoryMapper;
import ru.sinitsynme.logistapi.rest.dto.ProductCategoryDto;
import ru.sinitsynme.logistapi.service.ProductCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Управление категориями товаров")
@RestController
@RequestMapping("/rest/api/v1/product/category")
public class ProductCategoryResource {

    private final ProductCategoryMapper productCategoryMapper;
    private final ProductCategoryService productCategoryService;
    private final Logger logger = LoggerFactory.getLogger(ManufacturerResource.class);


    public ProductCategoryResource(ProductCategoryMapper productCategoryMapper, ProductCategoryService productCategoryService) {
        this.productCategoryMapper = productCategoryMapper;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{categoryCode}")
    @Operation(summary = "Найти категорию по коду")
    public ResponseEntity<ProductCategoryDto> findProductCategoryByCategoryCode(
            @PathVariable String categoryCode) {
        ProductCategory productCategory = productCategoryService.getProductCategoryByCategoryCode(categoryCode);
        return ResponseEntity.ok(productCategoryMapper.toDto(productCategory));
    }

    @GetMapping("/name/{categoryName}")
    @Operation(summary = "Найти категорию по имени")
    public ResponseEntity<ProductCategoryDto> findProductCategoryByCategoryName(
            @PathVariable String categoryName) {
        ProductCategory productCategory = productCategoryService.getProductCategoryByCategoryName(categoryName);
        return ResponseEntity.ok(productCategoryMapper.toDto(productCategory));
    }

    @GetMapping
    @Operation(summary = "Получить список категорий")
    public ResponseEntity<List<ProductCategoryDto>> findPageOfProductCategory(
            @RequestParam @Valid @Min(1) int size,
            @RequestParam @Valid @Min(0) int page) {
        List<ProductCategory> productCategories = productCategoryService.getPageOfProductCategories(size, page);
        return ResponseEntity.ok(
                productCategories
                        .stream()
                        .map(productCategoryMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    @Operation(summary = "Создать категорию")
    public ResponseEntity<ProductCategoryDto> createProductCategory(
            @RequestBody @Valid ProductCategoryDto requestDto) {
        ProductCategory productCategory = productCategoryService.saveProductCategory(requestDto);
        logger.info("Product category {} created", productCategory);
        return ResponseEntity.ok(productCategoryMapper.toDto(productCategory));
    }

    @PutMapping("/{categoryCode}")
    @Operation(summary = "Редактировать категорию по коду")
    public ResponseEntity<ProductCategoryDto> editProductCategoryByCode(
            @RequestBody @Valid ProductCategoryDto requestDto,
            @PathVariable String categoryCode) {
        ProductCategory productCategory = productCategoryService.editProductCategory(requestDto, categoryCode);
        logger.info("Product category {} edited", productCategory);
        return ResponseEntity.ok(productCategoryMapper.toDto(productCategory));
    }

    @DeleteMapping("/{categoryCode}")
    @Operation(summary = "Удалить категорию по коду")
    public ResponseEntity<?> deleteManufacturerById(
            @PathVariable String categoryCode
    ) {
        productCategoryService.deleteProductCategory(categoryCode);
        logger.info("Product category with code {} deleted", categoryCode);
        return ResponseEntity.ok().build();
    }
}
