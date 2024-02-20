package security.token;

import org.springframework.web.client.RestTemplate;

public record AuthServerRestTemplate(RestTemplate restTemplate) {
}
