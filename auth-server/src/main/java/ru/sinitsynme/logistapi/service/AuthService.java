package ru.sinitsynme.logistapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService jwtService;

    @Autowired
    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
