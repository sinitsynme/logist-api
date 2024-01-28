package ru.sinitsynme.logistapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.repository.UserRepository;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;
import ru.sinitsynme.logistapi.service.UserService;

@RestController
@RequestMapping("/signup")
public class SignUpResource {
    private final Logger logger = LoggerFactory.getLogger(SignUpResource.class);
    private final UserService userService;

    @Autowired
    public SignUpResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> signUpAsClient(@RequestBody UserSignUpDto signUpDto) {
        userService.saveUserAsClient(signUpDto);
        logger.info("User with email {} signed up with ROLE_CLIENT", signUpDto.getEmail());
        return ResponseEntity.ok().build();
    }
}
