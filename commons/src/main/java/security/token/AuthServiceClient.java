package security.token;

import exception.service.BadRequestException;
import exception.service.ServerErrorException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static exception.ExceptionSeverity.ERROR;
import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class AuthServiceClient {
    private static final String REQUEST_TOKEN_PATH = "/token";
    private static final String REFRESH_TOKEN_PATH = "/token/refresh";
    private final RestTemplate restTemplate;
    private final String authServerHost;

    public AuthServiceClient(RestTemplate restTemplate, String authServerHost) {
        this.restTemplate = restTemplate;
        this.authServerHost = authServerHost;
    }

    public JwtTokenPair requestToken(ServiceCredentials credentials) {
        try {
            return restTemplate.getForObject(
                    authServerHost + REQUEST_TOKEN_PATH,
                    JwtTokenPair.class,
                    credentials
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new BadRequestException(
                        e.getMessage(),
                        e,
                        BAD_REQUEST,
                        "Auth server responded with client error to access token request",
                        WARN
                );
            } else throw new ServerErrorException(
                    e.getMessage(),
                    e,
                    INTERNAL_SERVER_ERROR,
                    "Auth server responded with server error to access token request",
                    ERROR
            );
        }
    }


    public JwtTokenPair refreshToken(String refreshToken) {
        try {
            return restTemplate.getForObject(
                    authServerHost + REFRESH_TOKEN_PATH,
                    JwtTokenPair.class,
                    refreshToken
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new BadRequestException(
                        e.getMessage(),
                        e,
                        BAD_REQUEST,
                        "Auth server responded with client error to access token request",
                        WARN
                );
            } else throw new ServerErrorException(
                    e.getMessage(),
                    e,
                    INTERNAL_SERVER_ERROR,
                    "Auth server responded with server error to access token request",
                    ERROR
            );
        }
    }
}
