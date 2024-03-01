package ru.sinitsynme.logistapi.rest;

import dto.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.Organization;
import ru.sinitsynme.logistapi.mapper.OrganizationMapper;
import ru.sinitsynme.logistapi.rest.dto.OrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.OrganizationResponseDto;
import ru.sinitsynme.logistapi.service.OrganizationService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name="Управление предприятиями")
@RestController
@RequestMapping("/rest/api/v1/organization")
public class OrganizationResource {

    private final Logger logger = LoggerFactory.getLogger(OrganizationResource.class);
    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    public OrganizationResource(OrganizationService organizationService, OrganizationMapper organizationMapper) {
        this.organizationService = organizationService;
        this.organizationMapper = organizationMapper;
    }

    @Operation(summary = "Создать предприятие")
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<OrganizationResponseDto> createOrganization(
            @RequestBody @Valid OrganizationRequestDto organizationRequestDto) {
        Organization organization = organizationService.addOrganization(organizationRequestDto);

        logger.info("Created organization {}", organization);
        return ResponseEntity.ok(organizationMapper
                .organizationToResponseDto(organization)
        );
    }

    @Operation(summary = "Редактировать предприятие")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<OrganizationResponseDto> editOrganization(@RequestBody @Valid OrganizationRequestDto organizationRequestDto,
                                                                    @PathVariable Long id) {
        Organization editedOrganization = organizationService.editOrganization(organizationRequestDto, id);
        logger.info("Edited organization {}", editedOrganization);

        OrganizationResponseDto responseDto = organizationMapper.organizationToResponseDto(editedOrganization);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Получить предприятие")
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> getOrganization(@PathVariable Long id) {
        Organization organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(organizationMapper
                .organizationToResponseDto(organization));
    }

    @Operation(summary = "Получить страницу предприятий")
    @GetMapping
    public ResponseEntity<Page<OrganizationResponseDto>> getOrganizationPaged(
            @Valid PageRequestDto pageRequestDto
    ) {
        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);

        Page<OrganizationResponseDto> organizationList = organizationService
                .getPageOfOrganizations(pageRequestDto.toPageable())
                .map(organizationMapper::organizationToResponseDto);

        return ResponseEntity.ok(organizationList);
    }

    @Operation(summary = "Удалить предприятие")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        logger.info("Deleted organization and all warehouses with ID {}", id);

        return ResponseEntity.ok().build();
    }

    private void updatePageRequestDtoIfSortIsEmpty(PageRequestDto pageRequestDto) {
        if (pageRequestDto.getSortByFields() == null || pageRequestDto.getSortByFields().length == 0) {
            pageRequestDto.setSortByFields(new String[]{"name"});
        }
    }
}
