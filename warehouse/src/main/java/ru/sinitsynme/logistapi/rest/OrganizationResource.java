package ru.sinitsynme.logistapi.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.rest.dto.OrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.OrganizationResponseDto;

@RestController
@RequestMapping("/organization")
public class OrganizationResource {

    @PostMapping
    public ResponseEntity<OrganizationResponseDto> createOrganization(
            @RequestBody OrganizationRequestDto organizationRequestDto) {
        return ResponseEntity.ok(new OrganizationResponseDto(1L, "test"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> editOrganization(@RequestBody OrganizationRequestDto organizationRequestDto,
                                                                    @PathVariable Long id) {
        return ResponseEntity.ok(new OrganizationResponseDto(1L, "test-edited"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> getOrganization(@PathVariable Long id) {
        return ResponseEntity.ok(new OrganizationResponseDto(1L, "test"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
