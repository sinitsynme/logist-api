package ru.sinitsynme.logistapi.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignUpResource {

    @PostMapping
    public ResponseEntity<?> signUp() {
        return null;
    }



}
