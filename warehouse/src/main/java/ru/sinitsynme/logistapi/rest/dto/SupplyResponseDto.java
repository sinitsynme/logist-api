package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplyResponseDto {
    private Long warehouseId;
    private int availableForReserveQuantity;
    private BigDecimal price;
    private int quantum;
}
