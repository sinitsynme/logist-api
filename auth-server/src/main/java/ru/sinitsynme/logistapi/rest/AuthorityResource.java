package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.config.annotations.SupportAccess;
import ru.sinitsynme.logistapi.config.annotations.ClientAccess;

@Tag(name = "Управление ролями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/authority")
public class AuthorityResource {

    @GetMapping
    @ClientAccess
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Access granted to CLIENT");
    }

    @GetMapping("/admin")
    @SupportAccess
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Access granted to ADMIN");
    }
}
