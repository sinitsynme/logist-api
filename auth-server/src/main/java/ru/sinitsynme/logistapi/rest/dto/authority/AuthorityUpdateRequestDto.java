package ru.sinitsynme.logistapi.rest.dto.authority;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityUpdateRequestDto {
    @Size(min = 3, message = "Description must have at least 3 symbols")
    @Size(max = 100, message = "Description must have at most 100 symbols")
    private String description;
}
