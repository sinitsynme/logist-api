package ru.sinitsynme.logistapi.rest.dto.authority;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityRequestDto {
    @Size(min = 6, message = "Authority must start with ROLE_ prefix and be at least 6 symbols long")
    @Size(max = 30, message = "Authority must have at most 30 symbols")
    private String name;

    @Size(min = 3, message = "Description must have at least 3 symbols")
    @Size(max = 100, message = "Description must have at most 100 symbols")
    private String description;
}
