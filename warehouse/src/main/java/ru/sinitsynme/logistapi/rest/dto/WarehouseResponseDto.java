package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseDto {

    private long id;
    private String name;
    private String contactNumber;
    private String email;
    private AddressResponseDto address;
    private OrganizationResponseDto organization;
}
