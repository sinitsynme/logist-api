package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.Address;
import ru.sinitsynme.logistapi.rest.dto.address.AddressRequestDto;
import ru.sinitsynme.logistapi.rest.dto.address.AddressResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    Address requestDtoToAddress(AddressRequestDto requestDto);

    AddressResponseDto addressToResponseDto(Address address);

}
