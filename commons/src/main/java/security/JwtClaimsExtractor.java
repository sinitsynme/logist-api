package security;

import exception.ExceptionSeverity;
import exception.service.UnauthorizedException;
import io.jsonwebtoken.*;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JwtClaimsExtractor {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String SUBJECT_CLAIM = "sub";
    private static final String USER_ID_CLAIM = "user_id";

    public static List<String> getAuthorities(final String token) {
        try {
            return (List<String>) Jwts.parserBuilder().build()
                    .parseClaimsJwt(token)
                    .getBody()
                    .get(AUTHORITIES_CLAIM, List.class);
        } catch (JwtException ex) {
            throw new UnauthorizedException(
                    ex.getMessage(),
                    null,
                    UNAUTHORIZED,
                    "Invalid JWT",
                    ExceptionSeverity.WARN
            );
        }
    }

    public static String getSubject(final String token) {
        try {
            return Jwts.parserBuilder().build()
                    .parseClaimsJwt(token)
                    .getBody()
                    .get(SUBJECT_CLAIM, String.class);
        } catch (JwtException ex) {
            throw new UnauthorizedException(
                    ex.getMessage(),
                    null,
                    UNAUTHORIZED,
                    "Invalid JWT",
                    ExceptionSeverity.WARN
            );
        }
    }

    public static String getUserId(final String token) {
        try {
            return Jwts.parserBuilder().build()
                    .parseClaimsJwt(token)
                    .getBody()
                    .get(USER_ID_CLAIM, String.class);
        } catch (JwtException ex) {
            throw new UnauthorizedException(
                    ex.getMessage(),
                    null,
                    UNAUTHORIZED,
                    "Invalid JWT",
                    ExceptionSeverity.WARN
            );
        }
    }

    public static String extractTokenWithoutSignature(String header) {
        String token = header.replace(BEARER_PREFIX, "");

        int signatureDelimiter = token.lastIndexOf('.');
        return token.substring(0, signatureDelimiter + 1);
    }
}
