package ru.sinitsynme.logistapi.rest;

import exception.ExceptionSeverity;
import exception.service.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.rest.dto.user.UserSignInDto;
import ru.sinitsynme.logistapi.service.JwtService;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.INVALID_AUTH_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.INVALID_AUTH_TEMPLATE;

@Tag(name = "Сервис токенов")
@RestController
@RequestMapping("/token")
public class TokenResource {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public TokenResource(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    @Operation(summary = "Получить токен")
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

        return ResponseEntity.ok(jwtService.generateAccessToken(dto.getEmail()));
    }

    @GetMapping("/validate")
    @Operation(summary = "Валидировать токен")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        jwtService.validateAccessToken(token);
        return ResponseEntity.ok().build();
    }
}
