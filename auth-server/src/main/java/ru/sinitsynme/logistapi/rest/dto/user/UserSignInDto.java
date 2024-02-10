package ru.sinitsynme.logistapi.rest.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInDto {
    private String email;
    private byte[] password;

    public void setPassword(String password) {
        this.password = password.getBytes();
    }
}
