package ru.sinitsynme.logistapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.entity.BaseAuthorities;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;
import ru.sinitsynme.logistapi.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/signup")
public class SignUpResource {
    private final Logger logger = LoggerFactory.getLogger(SignUpResource.class);
    private final UserService userService;

    @Autowired
    public SignUpResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/client")
    public ResponseEntity<?> signUpAsClient(@RequestBody UserSignUpDto signUpDto) {
        return signUp(signUpDto, BaseAuthorities.ROLE_CLIENT.name());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_HEAD_ADMIN'))")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDto signUpDto, String authority) {
        userService.saveUser(signUpDto, List.of(authority));
        logger.info("User with email {} signed up with role: {}",
                signUpDto.getEmail(),
                authority);
        return ResponseEntity.ok().build();
    }
}
