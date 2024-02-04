package security;

import exception.ExceptionSeverity;
import exception.service.UnauthorizedException;
import io.jsonwebtoken.*;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JwtClaimsExtractor {
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String SUBJECT_CLAIM = "sub";

    public List<String> getAuthorities(final String token) {
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

    public String getSubject(final String token) {
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
}
