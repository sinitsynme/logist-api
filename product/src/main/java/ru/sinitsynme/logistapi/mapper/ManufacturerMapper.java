package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerResponseDto;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productList", ignore = true)
    Manufacturer fromRequestDto(ManufacturerRequestDto requestDto);
    ManufacturerResponseDto toResponseDto(Manufacturer manufacturer);
}
