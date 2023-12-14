package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sinitsynme.logistapi.entity.ProductCategory;
import ru.sinitsynme.logistapi.rest.dto.ProductCategoryDto;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    @Mapping(target = "productList", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductCategory fromDto(ProductCategoryDto productCategoryDto);

    ProductCategoryDto toDto(ProductCategory productCategory);
}
