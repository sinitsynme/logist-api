package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.annotations.AdminAccess;
import ru.sinitsynme.logistapi.entity.BaseAuthorities;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.rest.dto.user.SignUpAuthorityDto;
import ru.sinitsynme.logistapi.rest.dto.user.UserSignUpDto;
import ru.sinitsynme.logistapi.service.UserService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Сервис регистрации")
@RestController
@RequestMapping("/rest/api/v1/signup")
public class SignUpResource {
    private final Logger logger = LoggerFactory.getLogger(SignUpResource.class);
    private final UserService userService;

    @Autowired
    public SignUpResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/client")
    @Operation(summary = "Зарегистрировать пользователя в роли клиента")
    public ResponseEntity<UUID> signUpAsClient(@RequestBody @Valid UserSignUpDto signUpDto) {
        User user = userService.saveUser(signUpDto, List.of(BaseAuthorities.ROLE_CLIENT.name()));
        logger.info("User with email {} signed up with role: ROLE_CLIENT",
                signUpDto.getEmail());
        return ResponseEntity.ok(user.getId());
    }

    @PostMapping
    @Operation(summary = "Зарегистрировать пользователя")
    @AdminAccess
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UUID> signUp(@RequestBody @Valid SignUpAuthorityDto dto) {
        User user = userService.saveUser(dto.getSignUpDto(), List.of(dto.getAuthorityName()));
        logger.info("User with email {} signed up with role: {}",
                dto.getSignUpDto().getEmail(),
                dto.getAuthorityName());
        return ResponseEntity.ok(user.getId());
    }
}
