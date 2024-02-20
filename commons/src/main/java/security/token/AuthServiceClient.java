package security.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import exception.service.BadRequestException;
import exception.service.ServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static exception.ExceptionSeverity.ERROR;
import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class AuthServiceClient {
    private static final String REQUEST_TOKEN_PATH = "/token";
    private static final String REFRESH_TOKEN_PATH = "/token/refresh";
    private final Logger log = LoggerFactory.getLogger(AuthServiceClient.class);
    private final RestTemplate restTemplate;
    private final String authServerHost;

    public AuthServiceClient(RestTemplate restTemplate, String authServerHost) {
        this.restTemplate = restTemplate;
        this.authServerHost = authServerHost;
    }

    public JwtTokenPair requestToken(ServiceCredentials credentials) throws JsonProcessingException {
        try {
            HttpEntity<ServiceCredentials> credentialsEntity = new HttpEntity<>(credentials);
            JwtTokenPair response = restTemplate.postForObject(
                    authServerHost + REQUEST_TOKEN_PATH,
                    credentialsEntity,
                    JwtTokenPair.class
            );

            log.info("Successfully requested token pair in AuthService");
            return response;

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


    public JwtTokenPair refreshToken(String refreshToken) throws JsonProcessingException {
        try {
            JwtTokenPair response =  restTemplate.postForObject(
                    authServerHost + REFRESH_TOKEN_PATH,
                    refreshToken,
                    JwtTokenPair.class
                    );

            log.info("Successfully requested token pair in AuthService");
            return response;

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
