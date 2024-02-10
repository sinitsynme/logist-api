package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.config.annotations.SupportAccess;
import ru.sinitsynme.logistapi.rest.dto.EmailRequestDto;
import ru.sinitsynme.logistapi.service.UserService;

@Tag(name = "Управление пользователями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/block")
    @SupportAccess
    public ResponseEntity<String> lockUserAccount(@RequestBody String email) {
        userService.lockUserAccount(email);
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully blocked", email)
        );
    }

    @PatchMapping("/unblock")
    @SupportAccess
    public ResponseEntity<String> unblockUserAccount(@RequestBody EmailRequestDto dto) {
        userService.unlockUserAccount(dto.getEmail());
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully unblocked", dto.getEmail())
        );
    }

    @PatchMapping("/disable")
    @SupportAccess
    public ResponseEntity<String> disableUserAccount(@RequestBody EmailRequestDto dto) {
        userService.disableUser(dto.getEmail());
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully disabled", dto.getEmail())
        );
    }

    @PatchMapping("/enable")
    @SupportAccess
    public ResponseEntity<String> enableUserAccount(@RequestBody EmailRequestDto dto) {
        userService.enableUser(dto.getEmail());
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully enabled", dto.getEmail())
        );
    }
}
