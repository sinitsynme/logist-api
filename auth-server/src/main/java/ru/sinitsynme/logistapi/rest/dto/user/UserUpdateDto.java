package ru.sinitsynme.logistapi.rest.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;

}
