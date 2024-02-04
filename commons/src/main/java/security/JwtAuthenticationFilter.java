package security;

import exception.service.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final JwtClaimsExtractor JWT_CLAIMS_EXTRACTOR = new JwtClaimsExtractor();
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !isHeaderBearer(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = extractTokenWithoutSignature(authHeader);

        try {
            String subject = JWT_CLAIMS_EXTRACTOR.getSubject(jwtToken);
            List<String> authorityNames = JWT_CLAIMS_EXTRACTOR.getAuthorities(jwtToken);
            List<SimpleGrantedAuthority> authorities = authorityNames
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException ex) {
            logger.warn(ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isHeaderBearer(String header) {
        return header.startsWith("Bearer ");
    }

    private String extractTokenWithoutSignature(String header) {
        String token = header.replace(BEARER_PREFIX, "");

        int signatureDelimiter = token.lastIndexOf('.');
        return token.substring(0, signatureDelimiter + 1);
    }
}
