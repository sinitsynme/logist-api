package ru.sinitsynme.logistapi.rest.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequestDto {

    @Size(min = 3, max = 100, message = "Name length should be more than 3 and less than 100")
    private String name;
}
