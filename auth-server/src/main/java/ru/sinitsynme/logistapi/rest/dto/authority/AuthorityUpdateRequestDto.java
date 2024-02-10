package ru.sinitsynme.logistapi.rest.dto.authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityUpdateRequestDto {
    private String description;
}
