package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.UnauthorizedException;
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
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.INVALID_JWT_CODE;

@Service
public class JwtService {
    private static final String AUTHORITIES_CLAIM = "authorities";
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final Clock clock;

    @Autowired
    public JwtService(UserService userService, JwtProperties jwtProperties, Clock clock) {
        this.userService = userService;
        this.jwtProperties = jwtProperties;
        this.clock = clock;
    }

    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        } catch (JwtException ex) {
            throw new UnauthorizedException(
                    ex.getMessage(),
                    null,
                    UNAUTHORIZED,
                    INVALID_JWT_CODE,
                    ExceptionSeverity.WARN
            );
        }
    }

    public String generateToken(String email) {
        Collection<String> authorities = userService.getUserAuthoritiesNames(email);
        Map<String, Object> claims = Map.of(AUTHORITIES_CLAIM, authorities);

        return createToken(email, claims);
    }

    private String createToken(String email, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(Date.from(clock.instant()))
                .setExpiration(Date.from(clock.instant().plus(jwtProperties.getExpirationMinutes(), ChronoUnit.MINUTES)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
