package ru.sinitsynme.logistapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.entity.BaseAuthorities;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;
import ru.sinitsynme.logistapi.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        return signUp(signUpDto, List.of(BaseAuthorities.ROLE_CLIENT.name()));
    }

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDto signUpDto, List<String> authorities) {
        userService.saveUser(signUpDto, authorities);
        logger.info("User with email {} signed up with roles: {}",
                signUpDto.getEmail(),
                authorities);
        return ResponseEntity.ok().build();
    }
}
