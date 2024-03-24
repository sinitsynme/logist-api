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
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.mapper.UserMapper;
import ru.sinitsynme.logistapi.rest.dto.user.ChangePasswordRequestDto;
import ru.sinitsynme.logistapi.rest.dto.user.UserDataDto;
import ru.sinitsynme.logistapi.rest.dto.user.UserRoleRequestDto;
import ru.sinitsynme.logistapi.rest.dto.user.UserUpdateDto;
import ru.sinitsynme.logistapi.service.PrincipalService;
import ru.sinitsynme.logistapi.service.UserService;
import security.annotations.AdminAccess;
import security.annotations.SupportAccess;

import java.util.Set;
import java.util.UUID;

@Tag(name = "Управление пользователями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("rest/api/v1/user")
public class UserResource {

    private final UserService userService;
    private final PrincipalService principalService;
    private final UserMapper userMapper;

    @Autowired
    public UserResource(
            UserService userService,
            PrincipalService principalService,
            UserMapper userMapper) {
        this.userService = userService;
        this.principalService = principalService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Получить страницу пользователей")
    @GetMapping
    @SupportAccess
    public ResponseEntity<Page<UserDataDto>> getUserDataPage(@Valid PageRequestDto pageRequestDto) {
        pageRequestDto.updatePageRequestDtoIfSortIsEmpty("email");
        Page<UserDataDto> userDataDtoPage = userService
                .getUsers(pageRequestDto.toPageable())
                .map(userMapper::toDataDto);
        return ResponseEntity.ok(userDataDtoPage);
    }

    @Operation(summary = "[OWNER] Получить данные пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDataDto> getUserDataById(@PathVariable UUID id) {
        principalService.assertPrincipalAction(id);
        UserDataDto userDataDto = userMapper.toDataDto(userService.getUserById(id));
        return ResponseEntity.ok(userDataDto);
    }

    @Operation(summary = "[SUPPORT+] Получить данные пользователя по ID")
    @GetMapping("/admin-id/{id}")
    @SupportAccess
    public ResponseEntity<UserDataDto> getUserDataByIdAdmin(@PathVariable UUID id) {
        UserDataDto userDataDto = userMapper.toDataDto(userService.getUserById(id));
        return ResponseEntity.ok(userDataDto);
    }

    @Operation(summary = "[ADMIN] Получить данные пользователя по E-MAIL")
    @GetMapping("/admin-email/{email}")
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

    @Operation(summary = "Присвоить роль существующему пользователю")
    @PatchMapping("/{id}/roles/grant")
    @AdminAccess
    public ResponseEntity<?> grantRoleToUser(@PathVariable UUID id, @RequestBody UserRoleRequestDto requestDto) {
        userService.grantAuthorityToUser(id, requestDto.getAuthorityName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отобрать роль у существующего пользователя")
    @PatchMapping("/{id}/roles/revoke")
    @AdminAccess
    public ResponseEntity<?> revokeRoleFromUser(@PathVariable UUID id, @RequestBody UserRoleRequestDto requestDto) {
        userService.revokeAuthorityFromUser(id, requestDto.getAuthorityName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[OWNER] Обновить данные пользователя")
    @PatchMapping("/{id}/data")
    public ResponseEntity<UserDataDto> updateUserData(@PathVariable UUID id, @RequestBody @Valid UserUpdateDto updateDto) {
        principalService.assertPrincipalAction(id);
        User user = userService.updateUserData(id, updateDto);
        return ResponseEntity.ok(userMapper.toDataDto(user));
    }

    @Operation(summary = "[ADMIN] Обновить данные пользователя")
    @PatchMapping("/admin/{id}/data")
    @AdminAccess
    public ResponseEntity<UserDataDto> updateUserDataAdmin(@PathVariable UUID id, @RequestBody @Valid UserUpdateDto updateDto) {
        User user = userService.updateUserData(id, updateDto);
        return ResponseEntity.ok(userMapper.toDataDto(user));
    }

    @Operation(summary = "[OWNER] Обновить пароль пользователя")
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updateUserPassword(
            @PathVariable UUID id,
            @RequestBody @Valid ChangePasswordRequestDto passwordRequestDto) {

        principalService.assertPrincipalAction(id);
        userService.updateUserPassword(id, passwordRequestDto.getPassword());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[ADMIN] Обновить пароль пользователя")
    @PatchMapping("/admin/{id}/password")
    @AdminAccess
    public ResponseEntity<UserDataDto> updateUserPasswordAdmin(
            @PathVariable UUID id,
            @RequestBody @Valid ChangePasswordRequestDto passwordRequestDto) {

        userService.updateUserPassword(id, passwordRequestDto.getPassword());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Заблокировать пользователя")
    @PatchMapping("/{id}/block")
    @SupportAccess
    public ResponseEntity<?> lockUserAccount(@PathVariable UUID id) {
        userService.lockUserAccount(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully blocked", id)
        );
    }

    @Operation(summary = "Разблокировать пользователя")
    @PatchMapping("/{id}/unblock")
    @SupportAccess
    public ResponseEntity<?> unblockUserAccount(@PathVariable UUID id) {
        userService.unlockUserAccount(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully unblocked", id)
        );
    }

    @Operation(summary = "Деактивировать пользователя")
    @PatchMapping("/{id}/disable")
    @SupportAccess
    public ResponseEntity<?> disableUserAccount(@PathVariable UUID id) {
        userService.disableUser(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully disabled", id)
        );
    }

    @Operation(summary = "Активировать пользователя")
    @PatchMapping("/{id}/enable")
    @SupportAccess
    public ResponseEntity<?> enableUserAccount(@PathVariable UUID id) {
        userService.enableUser(id);
        return ResponseEntity.ok(String.format(
                "User with id %s was successfully enabled", id)
        );
    }
}
