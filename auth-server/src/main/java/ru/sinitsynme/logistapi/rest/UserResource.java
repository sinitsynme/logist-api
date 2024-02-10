package ru.sinitsynme.logistapi.rest;

import dto.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.config.annotations.AdminAccess;
import ru.sinitsynme.logistapi.config.annotations.SupportAccess;
import ru.sinitsynme.logistapi.mapper.UserMapper;
import ru.sinitsynme.logistapi.rest.dto.user.UserDataDto;
import ru.sinitsynme.logistapi.service.UserService;

import java.util.Set;
import java.util.UUID;

@Tag(name = "Управление пользователями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("rest/api/v1/user")
public class UserResource {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserResource(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Получить страницу пользователей")
    @GetMapping
    @SupportAccess
    public ResponseEntity<Page<UserDataDto>> getUserDataPage(@Valid PageRequestDto pageRequestDto) {
        Page<UserDataDto> userDataDtoPage = userService
                .getUsers(pageRequestDto.toPageable())
                .map(userMapper::toDataDto);
        return ResponseEntity.ok(userDataDtoPage);
    }

    @Operation(summary = "Получить данные пользователя по ID")
    @GetMapping("/{id}")
    @SupportAccess
    public ResponseEntity<UserDataDto> getUserData(@PathVariable UUID id) {
        UserDataDto userDataDto = userMapper.toDataDto(userService.getUserById(id));
        return ResponseEntity.ok(userDataDto);
    }

    @Operation(summary = "Получить данные пользователя по E-MAIL")
    @GetMapping("/getByEmail/{email}")
    @AdminAccess
    public ResponseEntity<UserDataDto> getUserDataByEmail(@PathVariable String email) {
        UserDataDto userDataDto = userMapper.toDataDto(userService.getUserByEmail(email));
        return ResponseEntity.ok(userDataDto);
    }

    @Operation(summary = "Получить роли пользователя")
    @GetMapping("/{id}/roles")
    @SupportAccess
    public ResponseEntity<Set<String>> getUserAuthorities(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserAuthoritiesNames(id));
    }

    @Operation(summary = "Заблокировать пользователя")
    @PatchMapping("/{id}/block")
    @SupportAccess
    public ResponseEntity<String> lockUserAccount(@PathVariable UUID id) {
        userService.lockUserAccount(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully blocked", id)
        );
    }

    @Operation(summary = "Разблокировать пользователя")
    @PatchMapping("/{id}/unblock")
    @SupportAccess
    public ResponseEntity<String> unblockUserAccount(@PathVariable UUID id) {
        userService.unlockUserAccount(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully unblocked", id)
        );
    }

    @Operation(summary = "Деактивировать пользователя")
    @PatchMapping("/{id}/disable")
    @SupportAccess
    public ResponseEntity<String> disableUserAccount(@PathVariable UUID id) {
        userService.disableUser(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully disabled", id)
        );
    }

    @Operation(summary = "Активировать пользователя")
    @PatchMapping("/{id}/enable")
    @SupportAccess
    public ResponseEntity<String> enableUserAccount(@PathVariable UUID id) {
        userService.enableUser(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully enabled", id)
        );
    }
}
