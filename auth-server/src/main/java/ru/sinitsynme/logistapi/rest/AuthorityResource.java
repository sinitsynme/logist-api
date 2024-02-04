package ru.sinitsynme.logistapi.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.config.annotations.AdminAccess;
import ru.sinitsynme.logistapi.config.annotations.ClientAccess;

@RestController
@RequestMapping("/authority")
public class AuthorityResource {

    @GetMapping
    @ClientAccess
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Access granted to CLIENT");
    }

    @GetMapping("/admin")
    @AdminAccess
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Access granted to ADMIN");
    }
}
