package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sinitsynme.logistapi.entity.enums.OrganizationStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOrganizationResponseDto {

    private UUID clientId;
    private AddressResponseDto addressResponseDto;
    private String name;
    private String inn;
    private String bik;
    private String clientAccount;
    private String bankName;
    private String correspondentAccount;
    private OrganizationStatus organizationStatus;
}
