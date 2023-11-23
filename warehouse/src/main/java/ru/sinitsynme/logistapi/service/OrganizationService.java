package ru.sinitsynme.logistapi.service;

import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.OrganizationRepository;
import ru.sinitsynme.logistapi.entity.Organization;
import ru.sinitsynme.logistapi.exception.ExceptionSeverity;
import ru.sinitsynme.logistapi.exception.HttpServiceException;
import ru.sinitsynme.logistapi.exception.OrganizationNotFoundException;
import ru.sinitsynme.logistapi.mapper.OrganizationMapper;
import ru.sinitsynme.logistapi.rest.dto.OrganizationRequestDto;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.ORGANIZATION_CREATION_ERROR;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.ORGANIZATION_NOT_FOUND_CODE;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationService(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id).orElseThrow(() ->
                new OrganizationNotFoundException(
                        String.format("Organization with id = %d not found", id),
                        new Throwable(),
                        NOT_FOUND,
                        ORGANIZATION_NOT_FOUND_CODE,
                        ExceptionSeverity.WARN
                )
        );
    }

    public Organization addOrganization(OrganizationRequestDto organizationRequestDto) {
        try {
            return organizationRepository.save(
                    organizationMapper.requestDtoToOrganization(organizationRequestDto)
            );
        } catch (Exception e) {
            throw new HttpServiceException(
                    String.format("Organization couldn't be created. Cause: %s",
                            e.getMessage()
                    ),
                    e,
                    BAD_REQUEST,
                    ORGANIZATION_CREATION_ERROR,
                    ExceptionSeverity.ERROR
            );
        }
    }
}
