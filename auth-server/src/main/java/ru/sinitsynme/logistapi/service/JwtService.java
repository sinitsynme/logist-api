package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.UnauthorizedException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.config.properties.AppProperties;
import ru.sinitsynme.logistapi.config.properties.JwtProperties;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.entity.UserRefreshToken;
import ru.sinitsynme.logistapi.repository.UserRefreshTokenRepository;
import security.token.JwtTokenPair;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.INVALID_JWT_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.INVALID_JWT_MESSAGE;

@Service
public class JwtService {
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String USER_ID_CLAIM = "user_id";
    private final UserService userService;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtProperties jwtProperties;
    private final AppProperties appProperties;
    private final Clock clock;
    private final SecretKey accessTokenSignKey;
    private final SecretKey refreshTokenSignKey;
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    public JwtService(
            UserService userService,
            UserRefreshTokenRepository userRefreshTokenRepository,
            BCryptPasswordEncoder encoder,
            JwtProperties jwtProperties,
            AppProperties appProperties,
            Clock clock) {
        this.userService = userService;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        this.encoder = encoder;
        this.jwtProperties = jwtProperties;
        this.appProperties = appProperties;
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

    public JwtTokenPair generateTokenPairByRefreshToken(String refreshToken) {
        String userEmail = extractTokenSubject(refreshToken, refreshTokenSignKey);
        User user = userService.getUserByEmail(userEmail);
        Optional<UserRefreshToken> optionalToken = userRefreshTokenRepository.findById(user.getId());

        String sha256RefreshToken = DigestUtils.sha256Hex(refreshToken);
        if (optionalToken.isEmpty() || !encoder.matches(sha256RefreshToken, optionalToken.get().getRefreshToken())) {
            throw new UnauthorizedException(
                    INVALID_JWT_MESSAGE,
                    null,
                    UNAUTHORIZED,
                    INVALID_JWT_CODE,
                    ExceptionSeverity.WARN
            );
        }
        return generateTokenPair(userEmail);
    }

    public JwtTokenPair generateTokenPair(String userEmail) {
        Pair<String, LocalDateTime> accessTokenPair = generateAccessToken(userEmail);
        Pair<String, LocalDateTime> refreshTokenPair = generateRefreshToken(userEmail);

        saveRefreshToken(refreshTokenPair, userEmail);

        return JwtTokenPair.builder()
                .accessToken(accessTokenPair.getFirst())
                .accessTokenExpiresAt(accessTokenPair.getSecond())
                .refreshToken(refreshTokenPair.getFirst())
                .refreshTokenExpiresAt(refreshTokenPair.getSecond())
                .build();
    }

    private Pair<String, LocalDateTime> generateAccessToken(String email) {
        User user = userService.getUserByEmail(email);
        Collection<String> authorities = userService.getUserAuthoritiesNames(user.getId());
        Map<String, Object> claims = Map.of(AUTHORITIES_CLAIM, authorities, USER_ID_CLAIM, user.getId());

        Instant expiresAt = clock.instant().plus(jwtProperties.getAccessTokenExpirationMinutes(), ChronoUnit.MINUTES);
        return Pair.of(
                createToken(
                        email,
                        claims,
                        clock.instant(),
                        expiresAt,
                        accessTokenSignKey
                ),
                LocalDateTime.ofInstant(
                        expiresAt,
                        ZoneId.of(appProperties.getClockZoneId()))
        );
    }

    private Pair<String, LocalDateTime> generateRefreshToken(String email) {
        User user = userService.getUserByEmail(email);
        Map<String, Object> claims = Map.of(USER_ID_CLAIM, user.getId());

        Instant expiresAt = clock.instant().plus(jwtProperties.getRefreshTokenExpirationDays(), ChronoUnit.DAYS);

        return Pair.of(
                createToken(
                        email,
                        claims,
                        clock.instant(),
                        expiresAt,
                        refreshTokenSignKey),
                LocalDateTime.ofInstant(
                        expiresAt,
                        ZoneId.of(appProperties.getClockZoneId()))
        );
    }

    public void deleteRefreshToken(UUID userId) {
        userRefreshTokenRepository
                .findById(userId)
                .ifPresent((it) -> {
                    userRefreshTokenRepository.delete(it);
                    log.info("Refresh token for user with id {} is removed", userId);
                });
    }

    private void saveRefreshToken(Pair<String, LocalDateTime> refreshTokenPair, String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        UserRefreshToken userRefreshToken;

        Optional<UserRefreshToken> userRefreshTokenOptional = userRefreshTokenRepository.findById(user.getId());

        if (userRefreshTokenOptional.isPresent()) {
            userRefreshToken = userRefreshTokenOptional.get();
        } else {
            userRefreshToken = new UserRefreshToken();
            userRefreshToken.setUserId(user.getId());
        }

        String sha256refreshToken = DigestUtils.sha256Hex(refreshTokenPair.getFirst());
        userRefreshToken.setRefreshToken(encoder.encode(sha256refreshToken));

        userRefreshToken.setExpiresAt(refreshTokenPair.getSecond());

        userRefreshTokenRepository.save(userRefreshToken);
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

    private String extractTokenSubject(final String token, final SecretKey signingKey) {
        try {
            return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody().getSubject();
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
            Instant issuedAt,
            Instant expiresAt,
            SecretKey signKey
    ) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiresAt))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}