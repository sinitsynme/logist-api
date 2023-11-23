package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.rest.dto.OrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.OrganizationResponseDto;

@Tag(name="Управление организациями")
@RestController
@RequestMapping("/organization")
public class OrganizationResource {

    @Operation(summary = "Создать предприятие")
    @PostMapping
    public ResponseEntity<OrganizationResponseDto> createOrganization(
            @RequestBody OrganizationRequestDto organizationRequestDto) {
        return ResponseEntity.ok(new OrganizationResponseDto(1L, "test"));
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
        return ResponseEntity.ok(new OrganizationResponseDto(1L, "test"));
    }

    @Operation(summary = "Удалить предприятие")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
