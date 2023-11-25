package ru.sinitsynme.logistapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Organization;
import ru.sinitsynme.logistapi.exception.ExceptionSeverity;
import ru.sinitsynme.logistapi.exception.service.BadRequestException;
import ru.sinitsynme.logistapi.exception.service.NotFoundException;
import ru.sinitsynme.logistapi.mapper.OrganizationMapper;
import ru.sinitsynme.logistapi.repository.OrganizationRepository;
import ru.sinitsynme.logistapi.rest.dto.OrganizationRequestDto;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.ORGANIZATION_EXISTS_CODE;
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
                new NotFoundException(
                        String.format("Organization with id = %d not found", id),
                        null,
                        NOT_FOUND,
                        ORGANIZATION_NOT_FOUND_CODE,
                        ExceptionSeverity.WARN
                )
        );
    }

    public Organization addOrganization(OrganizationRequestDto organizationRequestDto) {
        Organization organization = organizationMapper.requestDtoToOrganization(organizationRequestDto);
        if (organizationRepository.findByName(organization.getName()).isPresent()) {
            throw new BadRequestException(
                    String.format("Organization with name= %s exists", organization.getName()),
                    new Throwable(),
                    BAD_REQUEST,
                    ORGANIZATION_EXISTS_CODE,
                    ExceptionSeverity.ERROR
            );
        }
        return organizationRepository.save(organization);
    }

    public Organization editOrganization(OrganizationRequestDto requestDto, Long organizationId) {
        Organization organizationFromDb = getOrganizationById(organizationId);
        organizationFromDb.setName(requestDto.getName());

        return organizationRepository.save(organizationFromDb);
    }

    public void deleteOrganization(Long organizationId) {
        if (!organizationRepository.existsById(organizationId)) {
            throw new NotFoundException(
                    String.format("Organization with ID= %s not found", organizationId),
                    new Throwable(),
                    NOT_FOUND,
                    ORGANIZATION_NOT_FOUND_CODE,
                    ExceptionSeverity.ERROR
            );
        }

        organizationRepository.deleteById(organizationId);
    }

    public List<Organization> getPageOfOrganizations(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
        Page<Organization> organizationPage = organizationRepository.findAll(pageRequest);
        return organizationPage.getContent();
    }
}
