package ru.sinitsynme.logistapi.rest.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    @Size(min = 8, message = "Password must have at least 8 symbols")
    @Size(max = 100, message = "Password must have at most 100 symbols")
    private String password;
}
