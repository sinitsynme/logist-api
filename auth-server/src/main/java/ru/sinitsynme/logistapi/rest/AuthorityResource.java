package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.config.annotations.AdminAccess;
import ru.sinitsynme.logistapi.config.annotations.SupportAccess;
import ru.sinitsynme.logistapi.entity.Authority;
import ru.sinitsynme.logistapi.rest.dto.authority.AuthorityRequestDto;
import ru.sinitsynme.logistapi.rest.dto.authority.AuthorityUpdateRequestDto;
import ru.sinitsynme.logistapi.service.AuthorityService;

import java.util.List;

@Tag(name = "Управление ролями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("rest/api/v1/authority")
public class AuthorityResource {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Operation(summary = "Получить список всех ролей")
    @GetMapping
    @SupportAccess
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        return ResponseEntity.ok(authorityService.getAll());
    }

    @Operation(summary = "Получить роль по названию")
    @GetMapping("/{authorityName}")
    @SupportAccess
    public ResponseEntity<Authority> getAuthority(@PathVariable String authorityName) {
        return ResponseEntity.ok(authorityService.getByName(authorityName));
    }

    @Operation(summary = "Создать роль")
    @PostMapping
    @AdminAccess
    public ResponseEntity<Authority> saveAuthority(@RequestBody AuthorityRequestDto requestDto) {
        Authority authority = authorityService.save(requestDto);
        return ResponseEntity.ok(authority);
    }

    @Operation(summary = "Обновить роль")
    @PatchMapping("/{authorityName}")
    @AdminAccess
    public ResponseEntity<Authority> updateAuthority(@PathVariable String authorityName, @RequestBody AuthorityUpdateRequestDto requestDto) {
        Authority authority = authorityService.update(authorityName, requestDto);
        return ResponseEntity.ok(authority);
    }

    @Operation(summary = "Удалить роль")
    @DeleteMapping("/{authorityName}")
    public ResponseEntity<?> deleteAuthority(@PathVariable String authorityName) {
        authorityService.delete(authorityName);
        return ResponseEntity.ok().build();
    }
}
