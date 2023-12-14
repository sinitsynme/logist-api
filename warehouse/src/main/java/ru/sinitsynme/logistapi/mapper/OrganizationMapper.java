package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.Organization;
import ru.sinitsynme.logistapi.rest.dto.OrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.OrganizationResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationMapper {

    Organization requestDtoToOrganization(OrganizationRequestDto requestDto);

    OrganizationResponseDto organizationToResponseDto(Organization organization);

}
