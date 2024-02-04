package ru.sinitsynme.logistapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.service.UserService;

@RestController("/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/block")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEAD_ADMIN')")
    public ResponseEntity<String> lockUserAccount(@RequestBody String email) {
        userService.lockUserAccount(email);
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully blocked", email)
        );
    }

    @PatchMapping("/unblock")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEAD_ADMIN')")
    public ResponseEntity<String> unblockUserAccount(@RequestBody String email) {
        userService.unlockUserAccount(email);
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully unblocked", email)
        );
    }

    @PatchMapping("/disable")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEAD_ADMIN')")
    public ResponseEntity<String> disableUserAccount(@RequestBody String email) {
        userService.disableUser(email);
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully disabled", email)
        );
    }

    @PatchMapping("/enable")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEAD_ADMIN')")
    public ResponseEntity<String> enableUserAccount(@RequestBody String email) {
        userService.enableUser(email);
        return ResponseEntity.ok(String.format(
                "User with email %s was successfully enabled", email)
        );
    }
}
