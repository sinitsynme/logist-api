package ru.sinitsynme.logistapi.rest.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private String clientOrganizationInn;
    @Min(1)
    private Long warehouseId;

    @Size(min = 1)
    private List<OrderItemRequestDto> itemRequestDto;
    private UUID addressId;

}
