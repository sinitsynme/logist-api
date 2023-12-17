package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.rest.dto.StoredProductRequestDto;
import ru.sinitsynme.logistapi.rest.dto.StoredProductResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoredProductMapper {

    StoredProduct toEntity(StoredProductRequestDto requestDto);

    @Mapping(
            expression = "java(storedProduct.getQuantity() - storedProduct.getReservedQuantity())",
            target = "availableQuantity"
    )
    @Mapping(expression = "java(storedProduct.getId().getWarehouse().getId())", target = "warehouseId")
    @Mapping(expression = "java(storedProduct.getId().getProductId())", target = "productId")
    StoredProductResponseDto toResponseDto(StoredProduct storedProduct);
}
