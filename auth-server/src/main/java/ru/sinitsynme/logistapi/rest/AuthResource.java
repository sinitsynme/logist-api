package ru.sinitsynme.logistapi.rest;

import exception.ExceptionSeverity;
import exception.service.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.rest.dto.UserSignInDto;
import ru.sinitsynme.logistapi.service.AuthService;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.INVALID_AUTH_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.INVALID_AUTH_TEMPLATE;

@RestController
@RequestMapping("/token")
public class AuthResource {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthResource(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<String> getToken(@RequestBody UserSignInDto dto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getEmail(),
                    new String(dto.getPassword())
            ));
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException(
                    String.format(INVALID_AUTH_TEMPLATE, dto.getEmail(), ex.getMessage()),
                    null,
                    UNAUTHORIZED,
                    INVALID_AUTH_CODE,
                    ExceptionSeverity.WARN
            );
        }

        return ResponseEntity.ok(authService.generateToken(dto.getEmail()));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        authService.validateToken(token);
        return ResponseEntity.ok().build();
    }
}
