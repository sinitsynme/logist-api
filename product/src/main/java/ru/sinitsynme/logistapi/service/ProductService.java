package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.entity.ProductCategory;
import ru.sinitsynme.logistapi.exception.service.GetFileFromRootException;
import ru.sinitsynme.logistapi.exception.service.IllegalFileUploadException;
import ru.sinitsynme.logistapi.mapper.ProductMapper;
import ru.sinitsynme.logistapi.repository.ProductRepository;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;

import java.util.UUID;

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
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductMapper productMapper,
                          ProductRepository productRepository,
                          ManufacturerService manufacturerService,
                          ProductCategoryService productCategoryService,
                          FileService fileService) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.manufacturerService = manufacturerService;
        this.productCategoryService = productCategoryService;
        this.fileService = fileService;
    }

    public Product saveProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.fromRequestDto(productRequestDto);

        if (productRequestDto.getManufacturerId() == null) {
            logger.warn("Manufacturer id for product request {} is not set", productRequestDto);
            throw new BadRequestException(
                    MANUFACTURER_ID_NOT_SET,
                    null,
                    BAD_REQUEST,
                    MANUFACTURER_ID_NOT_SET_CODE,
                    ExceptionSeverity.WARN);
        }

        Manufacturer manufacturer = manufacturerService.getManufacturerById(
                productRequestDto.getManufacturerId()
        );
        product.setManufacturer(manufacturer);

        if (productRequestDto.getCategoryCode() == null || productRequestDto.getCategoryCode().isEmpty()) {
            logger.warn("Category code for product request {} is not set", productRequestDto);
            throw new BadRequestException(
                    PRODUCT_CATEGORY_CODE_NOT_SET,
                    null,
                    BAD_REQUEST,
                    PRODUCT_CATEGORY_NOT_SET_CODE,
                    ExceptionSeverity.WARN);
        }

        ProductCategory productCategory = productCategoryService
                .getProductCategoryByCategoryCode(productRequestDto.getCategoryCode());
        product.setProductCategory(productCategory);

        return productRepository.save(product);
    }

    public void uploadImageToProduct(UUID productId, MultipartFile productImageFile) {
        Product product = getProductById(productId);
        if (productImageFile != null) {
            try {
                fileService.saveImage(productImageFile);
            } catch (IllegalFileUploadException e) {
                throw new BadRequestException(
                        "Error while uploading file: " + e.getMessage(),
                        e,
                        BAD_REQUEST,
                        ILLEGAL_FILE_UPLOAD_CODE,
                        ExceptionSeverity.WARN);
            }
            product.setPathToImage(productImageFile.getOriginalFilename());
        }

        productRepository.save(product);
    }

    public Resource getProductImage(UUID productId) {
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
            return fileService.getResource(fileName);
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
