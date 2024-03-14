package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOrganizationEditRequestDto {

    private UUID clientId;
    private AddressRequestDto addressRequestDto;
    private String name;
    private String bik;
    private String clientAccount;
    private String bankName;
    private String correspondentAccount;

}
