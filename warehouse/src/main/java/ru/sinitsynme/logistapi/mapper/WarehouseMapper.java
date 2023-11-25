package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.Warehouse;
import ru.sinitsynme.logistapi.rest.dto.WarehouseRequestDto;
import ru.sinitsynme.logistapi.rest.dto.WarehouseResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AddressMapper.class, OrganizationMapper.class})
public interface WarehouseMapper {

    Warehouse requestDtoToWarehouse(WarehouseRequestDto requestDto);
    WarehouseResponseDto warehouseToResponseDto(Warehouse warehouse);
}