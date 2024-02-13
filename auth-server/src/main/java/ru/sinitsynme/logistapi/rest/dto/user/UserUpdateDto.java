package ru.sinitsynme.logistapi.rest.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {

    @Email(message = "Correct email must be provided")
    private String email;
    private String firstName;
    @Size(min = 2, message = "Middle name must have at least 2 symbols")
    @Size(max = 50, message = "Middle name must have at most 50 symbols")
    private String middleName;
    @Size(min = 2, message = "Last name must have at least 2 symbols")
    @Size(max = 50, message = "Last name must have at most 50 symbols")
    private String lastName;
    @Size(min = 11, max = 11, message = "Phone number must be 11 symbols long")
    private String phoneNumber;
}
