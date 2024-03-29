package ru.sinitsynme.logistapi.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoredProductRequestDto {

    private UUID productId;
    @Min(value = 1, message = "Warehouse ID must be at least 1")
    private Long warehouseId;
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal price;
    @Min(value = 1, message = "Quantum must be at least 1")
    private int quantum;
}