package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.ClientOrganization;
import ru.sinitsynme.logistapi.rest.dto.ClientOrganizationEditRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ClientOrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ClientOrganizationResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AddressMapper.class})
public interface ClientOrganizationMapper {

    @Mapping(source = "addressRequestDto", target = "address")
    ClientOrganization fromRequestDto(ClientOrganizationRequestDto requestDto);

    @Mapping(source = "addressRequestDto", target = "address")
    ClientOrganization fromEditRequestDto(ClientOrganizationEditRequestDto requestDto);

    @Mapping(source = "address", target = "addressResponseDto")
    ClientOrganizationResponseDto toResponseDto(ClientOrganization clientOrganization);
}
