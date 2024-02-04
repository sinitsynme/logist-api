package ru.sinitsynme.logistapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.config.annotations.AdminAccess;
import ru.sinitsynme.logistapi.rest.dto.EmailRequestDto;
import ru.sinitsynme.logistapi.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    //FIXME - Not working by some reason endpoints - to be fixed!
    @PatchMapping("/block")
    @AdminAccess
    public ResponseEntity<String> lockUserAccount(@RequestBody String email) {
        userService.lockUserAccount(email);
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully blocked", email)
        );
    }

    @PatchMapping("/unblock")
    @AdminAccess
    public ResponseEntity<String> unblockUserAccount(@RequestBody EmailRequestDto dto) {
        userService.unlockUserAccount(dto.getEmail());
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully unblocked", dto.getEmail())
        );
    }

    @PatchMapping("/disable")
    @AdminAccess
    public ResponseEntity<String> disableUserAccount(@RequestBody EmailRequestDto dto) {
        userService.disableUser(dto.getEmail());
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully disabled", dto.getEmail())
        );
    }

    @PatchMapping("/enable")
    @AdminAccess
    public ResponseEntity<String> enableUserAccount(@RequestBody EmailRequestDto dto) {
        userService.enableUser(dto.getEmail());
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully enabled", dto.getEmail())
        );
    }
}
