package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerResponseDto;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    Manufacturer fromRequestDto(ManufacturerRequestDto requestDto);
    ManufacturerResponseDto toResponseDto(Manufacturer manufacturer);
}
