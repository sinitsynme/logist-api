package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductResponseDto;

@Mapper(componentModel = "spring", uses = {ManufacturerMapper.class, ProductCategoryMapper.class})
public interface ProductMapper {

    Product fromRequestDto(ProductRequestDto requestDto);
    ProductResponseDto toResponseDto(Product product);
}
