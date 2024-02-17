package security.token;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;

public class TokenInterceptor implements ClientHttpRequestInterceptor {

    private JwtTokenPair jwtTokenPair;
    private final ServiceCredentials serviceCredentials;
    private final AuthServiceClient authServiceClient;
    private final Clock clock;

    public TokenInterceptor(ServiceCredentials serviceCredentials,
                            AuthServiceClient authServiceClient, Clock clock) {
        this.serviceCredentials = serviceCredentials;
        this.authServiceClient = authServiceClient;
        this.clock = clock;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        if (jwtTokenPair == null) {
            jwtTokenPair = authServiceClient.requestToken(serviceCredentials);
        } else {
            refreshTokenIfExpired();
        }

        request.getHeaders().add(
                HttpHeaders.AUTHORIZATION,
                String.format("Bearer %s", jwtTokenPair.getAccessToken())
        );
        return execution.execute(request, body);
    }

    private void refreshTokenIfExpired() {
        LocalDateTime now = LocalDateTime.now(clock);
        if (now.isAfter(jwtTokenPair.getAccessTokenExpiresAt())) {

            if (now.isAfter(jwtTokenPair.getRefreshTokenExpiresAt())) {
                jwtTokenPair = authServiceClient.requestToken(serviceCredentials);
                return;
            }
            jwtTokenPair = authServiceClient.refreshToken(jwtTokenPair.getRefreshToken());
        }
    }

}
