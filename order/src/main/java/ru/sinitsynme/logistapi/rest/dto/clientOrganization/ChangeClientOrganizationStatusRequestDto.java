package ru.sinitsynme.logistapi.rest.dto.clientOrganization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeClientOrganizationStatusRequestDto {
    private String status;
}
