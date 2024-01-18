package ru.sinitsynme.logistapi.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authority")
public class AuthorityResource {

    //TODO

    @GetMapping
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().build();
    }
}
