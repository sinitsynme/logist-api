package ru.sinitsynme.logistapi.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequestDto {

    @Size(min = 3, max = 100, message = "Name length should be more than 3 and less than 100")
    private String name;

    @Size(min = 11, max = 11, message = "Phone number must be 11 symbols long")
    private String contactNumber;

    @Email
    private String email;

    private AddressRequestDto addressRequestDto;
    private long organizationId;
}
