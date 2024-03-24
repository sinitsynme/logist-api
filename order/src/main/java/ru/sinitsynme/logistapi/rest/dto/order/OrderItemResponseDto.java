package ru.sinitsynme.logistapi.rest.dto.order;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private UUID productId;
    private BigDecimal price;

    @Min(value = 1)
    private int quantity;
    private int returnedQuantity;
}
