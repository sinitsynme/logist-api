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
import ru.sinitsynme.logistapi.rest.dto.token.JwtTokenPair;
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
    public ResponseEntity<JwtTokenPair> getToken(@RequestBody UserSignInDto dto) {
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

        return ResponseEntity.ok(jwtService.generateTokenPair(dto.getEmail()));
    }

    @GetMapping("/validate")
    @Operation(summary = "Валидировать access-токен")
    public ResponseEntity<?> validateAccessToken(@RequestParam String accessToken) {
        jwtService.validateAccessToken(accessToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновить токен через refresh-токен")
    public ResponseEntity<JwtTokenPair> refreshTokenPair(@RequestParam String refreshToken) {
        jwtService.validateRefreshToken(refreshToken);
        return ResponseEntity.ok(jwtService.generateTokenPairByRefreshToken(refreshToken));
    }
}
