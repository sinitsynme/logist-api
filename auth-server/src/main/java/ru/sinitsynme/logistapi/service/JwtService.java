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
import ru.sinitsynme.logistapi.config.properties.JwtProperties;
import ru.sinitsynme.logistapi.entity.User;

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
    private static final String USER_ID_CLAIM = "user_id";
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final Clock clock;
    private final SecretKey accessTokenSignKey;
    private final SecretKey refreshTokenSignKey;

    @Autowired

    public JwtService(UserService userService, JwtProperties jwtProperties, Clock clock) {
        this.userService = userService;
        this.jwtProperties = jwtProperties;
        this.clock = clock;

        accessTokenSignKey = getSignKey(jwtProperties.getAccessTokenSecret());
        refreshTokenSignKey = getSignKey(jwtProperties.getRefreshTokenSecret());
    }

    public void validateAccessToken(final String token) {
        validateToken(token, accessTokenSignKey);
    }

    public void validateRefreshToken(final String token) {
        validateToken(token, refreshTokenSignKey);
    }

    public String generateAccessToken(String email) {
        User user = userService.getUserByEmail(email);
        Collection<String> authorities = userService.getUserAuthoritiesNames(user.getId());
        Map<String, Object> claims = Map.of(AUTHORITIES_CLAIM, authorities, USER_ID_CLAIM, user.getId());

        return createToken(
                email,
                claims,
                jwtProperties.getAccessTokenExpirationMinutes(),
                ChronoUnit.MINUTES,
                accessTokenSignKey
        );
    }

    public String generateRefreshToken(String email) {
        User user = userService.getUserByEmail(email);
        Map<String, Object> claims = Map.of(USER_ID_CLAIM, user.getId());

        return createToken(
                email,
                claims,
                jwtProperties.getRefreshTokenExpirationDays(),
                ChronoUnit.DAYS,
                refreshTokenSignKey
        );
    }

    private void validateToken(final String token, final SecretKey signingKey) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
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

    private String createToken(
            String email,
            Map<String, Object> claims,
            Long unitsUntilExpires,
            ChronoUnit chronoUnit,
            SecretKey signKey
    ) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(Date.from(clock.instant()))
                .setExpiration(Date.from(clock.instant().plus(unitsUntilExpires, chronoUnit)))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
