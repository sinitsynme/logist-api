package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.ProductCategory;
import ru.sinitsynme.logistapi.rest.dto.ProductCategoryDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductCategoryMapper {

    ProductCategory fromDto(ProductCategoryDto productCategoryDto);

    ProductCategoryDto toDto(ProductCategory productCategory);
}
