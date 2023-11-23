package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.Organization;
import ru.sinitsynme.logistapi.mapper.OrganizationMapper;
import ru.sinitsynme.logistapi.rest.dto.OrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.OrganizationResponseDto;
import ru.sinitsynme.logistapi.service.OrganizationService;

import java.util.List;

@Tag(name="Управление организациями")
@RestController
@RequestMapping("/organization")
public class OrganizationResource {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    public OrganizationResource(OrganizationService organizationService, OrganizationMapper organizationMapper) {
        this.organizationService = organizationService;
        this.organizationMapper = organizationMapper;
    }

    @Operation(summary = "Создать предприятие")
    @PostMapping
    public ResponseEntity<OrganizationResponseDto> createOrganization(
            @RequestBody OrganizationRequestDto organizationRequestDto) {
        Organization organization = organizationService.addOrganization(organizationRequestDto);
        return ResponseEntity.ok(organizationMapper
                .organizationToResponseDto(organization)
        );
    }

    @Operation(summary = "Редактировать предприятие")
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> editOrganization(@RequestBody OrganizationRequestDto organizationRequestDto,
                                                                    @PathVariable Long id) {
        return ResponseEntity.ok(new OrganizationResponseDto(1L, "test-edited"));
    }

    @Operation(summary = "Получить предприятие")
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> getOrganization(@PathVariable Long id) {
        Organization organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(organizationMapper
                .organizationToResponseDto(organization));
    }

    @Operation(summary = "Получить список предприятий")
    @GetMapping
    public ResponseEntity<List<OrganizationResponseDto>> getOrganizationPaged(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(List.of(
                new OrganizationResponseDto(1L, "test"),
                new OrganizationResponseDto(2L, "test")
        ));
    }

    @Operation(summary = "Удалить предприятие")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
