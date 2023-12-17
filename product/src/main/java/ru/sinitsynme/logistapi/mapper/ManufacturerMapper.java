package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManufacturerMapper {

    Manufacturer fromRequestDto(ManufacturerRequestDto requestDto);
    ManufacturerResponseDto toResponseDto(Manufacturer manufacturer);
}
