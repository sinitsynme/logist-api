package ru.sinitsynme.logistapi.rest.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {
    private double latitude;
    private double longitude;

}
