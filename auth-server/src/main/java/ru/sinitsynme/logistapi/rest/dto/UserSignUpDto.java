package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDto {

    private String email;
    private byte[] password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;

    public void setPassword(String password) {
        this.password = password.getBytes();
    }
}
