package ru.sinitsynme.logistapi.rest.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequestDto {

    @Size(min = 3, max = 100, message = "Name length should be more than 3 and less than 100")
    private String name;
}
