package ru.sinitsynme.logistapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredProductResponseDto {
    private UUID productId;
    private Long warehouseId;
    private int availableForReserveQuantity;
    private int quantity;
    private int reservedQuantity;
}
