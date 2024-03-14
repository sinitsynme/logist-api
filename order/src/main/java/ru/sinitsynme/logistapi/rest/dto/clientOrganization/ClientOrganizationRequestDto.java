package ru.sinitsynme.logistapi.rest.dto.clientOrganization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sinitsynme.logistapi.rest.dto.AddressRequestDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOrganizationRequestDto {

    private UUID clientId;
    private AddressRequestDto addressRequestDto;
    private String name;
    private String inn;
    private String bik;
    private String clientAccount;
    private String bankName;
    private String correspondentAccount;

}
