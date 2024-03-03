package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.rest.dto.ProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ProductResponseDto;

@Mapper(componentModel = "spring",
        uses = {ManufacturerMapper.class, ProductCategoryMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product fromRequestDto(ProductRequestDto requestDto);

    @Mapping(source = "pathToImage", target = "link")
    ProductResponseDto toResponseDto(Product product);
}
