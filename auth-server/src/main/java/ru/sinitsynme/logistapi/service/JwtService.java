package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.ForbiddenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.config.JwtProperties;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.INVALID_JWT_CODE;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final Clock clock;

    @Autowired
    public JwtService(JwtProperties jwtProperties, Clock clock) {
        this.jwtProperties = jwtProperties;
        this.clock = clock;
    }

    public void validateToken(final String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
        } catch (JwtException ex) {
            throw new ForbiddenException(
                    ex.getMessage(),
                    null,
                    FORBIDDEN,
                    INVALID_JWT_CODE,
                    ExceptionSeverity.WARN
            );
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(username, claims);
    }

    private String createToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(Date.from(clock.instant()))
                .expiration(Date.from(clock.instant().plus(jwtProperties.getExpirationMinutes(), ChronoUnit.MINUTES)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
