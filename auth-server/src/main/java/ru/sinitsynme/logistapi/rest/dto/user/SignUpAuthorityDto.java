package ru.sinitsynme.logistapi.rest.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sinitsynme.logistapi.rest.dto.user.UserSignUpDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpAuthorityDto {
    private UserSignUpDto signUpDto;
    private String authorityName;
}
