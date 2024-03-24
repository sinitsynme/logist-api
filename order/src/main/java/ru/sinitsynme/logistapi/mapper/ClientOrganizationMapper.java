package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.ClientOrganization;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationEditRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AddressMapper.class})
public interface ClientOrganizationMapper {

    ClientOrganization fromRequestDto(ClientOrganizationRequestDto requestDto);

    ClientOrganization fromEditRequestDto(ClientOrganizationEditRequestDto requestDto);

    @Mapping(source = "addressList", target = "addressResponseDto")
    ClientOrganizationResponseDto toResponseDto(ClientOrganization clientOrganization);
}
