package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.entity.ProductCategory;
import ru.sinitsynme.logistapi.entity.enums.ProductStatus;
import ru.sinitsynme.logistapi.exception.service.GetFileFromRootException;
import ru.sinitsynme.logistapi.exception.service.IllegalFileUploadException;
import ru.sinitsynme.logistapi.mapper.ProductMapper;
import ru.sinitsynme.logistapi.repository.ProductRepository;
import ru.sinitsynme.logistapi.rest.dto.ChangeProductStatusRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.*;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessage.MANUFACTURER_ID_NOT_SET;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessage.PRODUCT_CATEGORY_CODE_NOT_SET;

@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ManufacturerService manufacturerService;
    private final ProductCategoryService productCategoryService;
    private final ProductEventService productEventService;
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductMapper productMapper,
                          ProductRepository productRepository,
                          ManufacturerService manufacturerService,
                          ProductCategoryService productCategoryService,
                          ProductEventService productEventService,
                          FileService fileService) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.manufacturerService = manufacturerService;
        this.productCategoryService = productCategoryService;
        this.productEventService = productEventService;
        this.fileService = fileService;
    }

    public Product saveProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.fromRequestDto(productRequestDto);
        product.setStatus(ProductStatus.NEW);

        addManufacturerAndCategoryToProduct(
                product,
                productRequestDto.getManufacturerId(),
                productRequestDto.getCategoryCode());

        return productRepository.save(product);
    }

    public Product changeProductStatus(UUID productId,
                                       ChangeProductStatusRequestDto requestDto,
                                       String authHeader) {

        Product productFromDb = getProductById(productId);
        ProductStatus newStatus = parseProductStatusFromString(requestDto.getStatus());
        productFromDb.setStatus(newStatus);

        productRepository.save(productFromDb);
        productEventService.saveProductEvent(productId, newStatus, authHeader);

        logger.info("Status of product with id = {} changed to {}", productId, requestDto.getStatus());

        return productFromDb;
    }


    public Product editProduct(UUID productId, ProductRequestDto productRequestDto) {
        Product productFromDb = getProductById(productId);

        productFromDb.setName(productRequestDto.getName());
        productFromDb.setDescription(productRequestDto.getDescription());
        productFromDb.setWeight(productRequestDto.getWeight());
        productFromDb.setVolume(productRequestDto.getVolume());

        addManufacturerAndCategoryToProduct(productFromDb, productRequestDto.getManufacturerId(), productRequestDto.getCategoryCode());

        return productRepository.save(productFromDb);
    }

    public void deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw notFoundException(productId);
        }

        productRepository.deleteById(productId);
    }

    public void uploadImageToProduct(UUID productId, MultipartFile productImageFile) {
        Product product = getProductById(productId);
        if (productImageFile != null) {
            try {
                String newFileName = fileService.saveImage(productImageFile);
                product.setPathToImage(newFileName);
            } catch (IllegalFileUploadException e) {
                throw new BadRequestException(
                        "Error while uploading file: " + e.getMessage(),
                        e,
                        BAD_REQUEST,
                        ILLEGAL_FILE_UPLOAD_CODE,
                        ExceptionSeverity.WARN);
            }
        }

        productRepository.save(product);
    }

    public String getLinkToProductImage(UUID productId) {
        Product product = getProductById(productId);
        String fileName = product.getPathToImage();
        if (fileName == null || fileName.isEmpty()) {
            throw new NotFoundException(
                    String.format("Product %s doesn't have image", productId),
                    null,
                    NOT_FOUND,
                    PRODUCT_IMAGE_NOT_FOUND,
                    ExceptionSeverity.WARN);
        }
        try {
            return fileService.getLinkToResource(fileName);
        } catch (GetFileFromRootException e) {
            throw new BadRequestException(
                    e.getMessage(),
                    e,
                    BAD_REQUEST,
                    PRODUCT_IMAGE_BROKEN_READ,
                    ExceptionSeverity.WARN);
        }
    }

    public Product getProductById(UUID productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> notFoundException(productId));
    }

    public Page<Product> getProductsPage(ProductStatus productStatus, Pageable pageable, List<String> categoryCodes) {
        Page<Product> productsPage;

        if (categoryCodes != null && !categoryCodes.isEmpty()) {
            List<ProductCategory> listOfCategories = categoryCodes.stream()
                    .map(productCategoryService::getProductCategoryByCategoryCode)
                    .toList();
            productsPage = productRepository.findByStatusAndProductCategoryIn(
                    productStatus,
                    listOfCategories,
                    pageable);

        } else productsPage = productRepository.findByStatus(productStatus, pageable);

        return productsPage;
    }


    public Page<Product> getProductsBySearchQueryInStatus(
            Pageable pageable,
            String query,
            ProductStatus productStatus) {
        List<Product> products = productRepository
                .findByNameContainingIgnoreCase(query, pageable)
                .stream()
                .filter(it -> it.getStatus().equals(productStatus))
                .toList();

        return new PageImpl<>(products);
    }

    public ProductStatus parseProductStatusFromString(String status) {
        try {
            return ProductStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    String.format("Status with name = %s doesn't exist", status),
                    e,
                    BAD_REQUEST,
                    PRODUCT_STATUS_NOT_FOUND_CODE,
                    ExceptionSeverity.WARN);        }
    }

    private void addManufacturerAndCategoryToProduct(Product product, Long manufacturerId, String categoryCode) {
        if (manufacturerId == null) {
            logger.warn("Provide manufacturer_id to product");
            throw new BadRequestException(
                    MANUFACTURER_ID_NOT_SET,
                    null,
                    BAD_REQUEST,
                    MANUFACTURER_ID_NOT_SET_CODE,
                    ExceptionSeverity.WARN);
        }

        Manufacturer manufacturer = manufacturerService.getManufacturerById(manufacturerId);
        product.setManufacturer(manufacturer);

        if (categoryCode == null || categoryCode.isEmpty()) {
            logger.warn("Provide category_code to product");
            throw new BadRequestException(
                    PRODUCT_CATEGORY_CODE_NOT_SET,
                    null,
                    BAD_REQUEST,
                    PRODUCT_CATEGORY_NOT_SET_CODE,
                    ExceptionSeverity.WARN);
        }

        ProductCategory productCategory = productCategoryService
                .getProductCategoryByCategoryCode(categoryCode);
        product.setProductCategory(productCategory);
    }

    private NotFoundException notFoundException(UUID productId) {
        logger.warn("Product with id = {} not found", productId);
        return new NotFoundException(
                String.format("Product with id = %s not found", productId),
                null,
                NOT_FOUND,
                PRODUCT_NOT_FOUND_CODE,
                ExceptionSeverity.WARN);
    }



}
