package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductResponseDto;

@Mapper(componentModel = "spring", uses = {ManufacturerMapper.class, ProductCategoryMapper.class})
public interface ProductMapper {

    @Mapping(target = "pathToImage", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(expression = "java(requestDto.isPackaged())", target = "isPackaged")
    Product fromRequestDto(ProductRequestDto requestDto);

    ProductResponseDto toResponseDto(Product product);
}
