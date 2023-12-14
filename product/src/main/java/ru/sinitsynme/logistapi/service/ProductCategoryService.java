package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.entity.ProductCategory;
import ru.sinitsynme.logistapi.mapper.ProductCategoryMapper;
import ru.sinitsynme.logistapi.repository.ProductCategoryRepository;
import ru.sinitsynme.logistapi.rest.dto.ProductCategoryDto;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.PRODUCT_CATEGORY_EXISTS_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.PRODUCT_CATEGORY_NOT_FOUND_CODE;

@Service
public class ProductCategoryService {

    private final ProductCategoryMapper productCategoryMapper;
    private final ProductCategoryRepository productCategoryRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductCategoryService.class);

    @Autowired
    public ProductCategoryService(ProductCategoryMapper productCategoryMapper,
                                  ProductCategoryRepository productCategoryRepository) {
        this.productCategoryMapper = productCategoryMapper;
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductCategory saveProductCategory(ProductCategoryDto categoryDto) {
        checkIfCategoryIsPresentByCode(categoryDto.getCategoryCode());
        checkIfCategoryIsPresentByName(categoryDto.getCategoryName());
        ProductCategory productCategory = productCategoryMapper.fromDto(categoryDto);
        return productCategoryRepository.save(productCategory);
    }

    public ProductCategory editProductCategory(ProductCategoryDto categoryDto, String conditionCode) {
        ProductCategory productCategoryFromDb = getProductCategoryByCategoryCode(conditionCode);

        String dtoCategoryCode = categoryDto.getCategoryCode();
        String dtoCategoryName = categoryDto.getCategoryName();

        if (!conditionCode.equals(dtoCategoryCode)) {

            checkIfCategoryIsPresentByCode(dtoCategoryCode);
            logger.info("Changing product category code {} into {}",
                    productCategoryFromDb.getCategoryCode(),
                    dtoCategoryCode);

        }

        if (!productCategoryFromDb.getCategoryName().equals(dtoCategoryName)) {

            checkIfCategoryIsPresentByName(dtoCategoryName);
            logger.info("Changing product category name {} into {}",
                    productCategoryFromDb.getCategoryName(),
                    dtoCategoryName);
        }

        productCategoryFromDb.setCategoryCode(dtoCategoryCode);
        productCategoryFromDb.setCategoryName(dtoCategoryName);

        return productCategoryRepository.save(productCategoryFromDb);
    }

    public void deleteProductCategory(String categoryCode) {
        if (!productCategoryRepository.existsById(categoryCode)) {
            throw notFoundException(categoryCode);
        }
        productCategoryRepository.deleteById(categoryCode);
    }

    public List<ProductCategory> getPageOfProductCategories(int size, int page) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("categoryCode"));
        Page<ProductCategory> productCategoryPage = productCategoryRepository.findAll(pageRequest);
        return productCategoryPage.getContent();
    }

    public ProductCategory getProductCategoryByCategoryCode(String categoryCode) {
        return productCategoryRepository
                .findByCategoryCode(categoryCode)
                .orElseThrow(() -> notFoundException(categoryCode));
    }

    public ProductCategory getProductCategoryByCategoryName(String categoryName) {
        return productCategoryRepository
                .findByCategoryName(categoryName)
                .orElseThrow(() -> notFoundException(categoryName));
    }

    private void checkIfCategoryIsPresentByCode(String categoryCode) {
        if (productCategoryRepository.existsById(categoryCode)) {
            logger.warn("Product category with code {} exists", categoryCode);
            throw new BadRequestException(
                    String.format("Product category with code %s exists", categoryCode),
                    null,
                    HttpStatus.BAD_REQUEST,
                    PRODUCT_CATEGORY_EXISTS_CODE,
                    ExceptionSeverity.ERROR);

        }
    }

    private void checkIfCategoryIsPresentByName(String categoryName) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository
                .findByCategoryName(categoryName);

        if (optionalProductCategory.isPresent()) {
            ProductCategory productCategory = optionalProductCategory.get();


            logger.warn("Product category {} exists, the code is {}",
                    productCategory.getCategoryName(),
                    productCategory.getCategoryCode()
            );

            throw new BadRequestException(
                    String.format("Product category %s exists, the code is %s",
                            productCategory.getCategoryName(),
                            productCategory.getCategoryCode()),
                    null,
                    HttpStatus.BAD_REQUEST,
                    PRODUCT_CATEGORY_EXISTS_CODE,
                    ExceptionSeverity.ERROR);

        }
    }

    private NotFoundException notFoundException(String categoryString) {
        logger.warn("Product category {} not found", categoryString);
        return new NotFoundException(
                String.format("Product category %s not found", categoryString),
                null,
                NOT_FOUND,
                PRODUCT_CATEGORY_NOT_FOUND_CODE,
                ExceptionSeverity.WARN);
    }
}
