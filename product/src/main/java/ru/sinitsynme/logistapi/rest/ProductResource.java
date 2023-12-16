package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.mapper.ProductMapper;
import ru.sinitsynme.logistapi.rest.dto.ProductPageRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductResponseDto;
import ru.sinitsynme.logistapi.service.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "Управление товарами")
@RestController
@RequestMapping("/product")
public class ProductResource {

    //TODO pageOfProductsByCategoryCode
    //TODO products starting with name

    private final ProductMapper productMapper;
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductResource.class);

    public ProductResource(ProductMapper productMapper, ProductService productService) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getPageOfProducts(
            @RequestBody @Valid ProductPageRequestDto pageRequestDto) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") UUID productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(productMapper.toResponseDto(product));
    }

    @PostMapping
    @Operation(summary = "Сохранить товар")
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestBody @Valid ProductRequestDto productRequestDto) {
        Product product = productService.saveProduct(productRequestDto);
        logger.info("Saved product: {}", product);
        return ResponseEntity.ok(productMapper.toResponseDto(product));
    }

    @GetMapping(value = "/{id}/image", produces = {
            MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<Resource> getProductImage(@PathVariable("id") UUID productId) {
        return ResponseEntity.ok(productService.getProductImage(productId));
    }

    @PutMapping("/{id}/image")
    @Operation(summary = "Добавить картинку к товару")
    public ResponseEntity<?> addImageToProduct(
            @RequestPart MultipartFile productImageFile,
            @PathVariable("id") UUID productId) {
        productService.uploadImageToProduct(productId, productImageFile);
        logger.info("Added image to product with ID = {}", productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> editProduct(@RequestBody ProductRequestDto productRequestDto,
                                                          @PathVariable("id") UUID productId) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") UUID productId) {
        return null;
    }
}
