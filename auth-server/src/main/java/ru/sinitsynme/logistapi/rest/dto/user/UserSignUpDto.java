package ru.sinitsynme.logistapi.rest.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDto {

    @Email(message = "Correct email must be provided")
    private String email;
    @Size(min = 8, message = "Password must have at least 8 symbols")
    @Size(max = 100, message = "Password must have at most 100 symbols")
    private byte[] password;
    @Size(min = 2, message = "First name must have at least 2 symbols")
    @Size(max = 50, message = "First name must have at most 50 symbols")
    private String firstName;
    @Size(min = 2, message = "Middle name must have at least 2 symbols")
    @Size(max = 50, message = "Middle name must have at most 50 symbols")
    private String middleName;
    @Size(min = 2, message = "Last name must have at least 2 symbols")
    @Size(max = 50, message = "Last name must have at most 50 symbols")
    private String lastName;
    @Size(min = 11, max = 11, message = "Phone number must be 11 symbols long")
    private String phoneNumber;

    public void setPassword(String password) {
        this.password = password.getBytes();
    }
}
