package security.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenPair {

    private String accessToken;
    private LocalDateTime accessTokenExpiresAt;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiresAt;
}
