package security.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceCredentials {

    private String email;
    private String password;

    public ServiceCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
