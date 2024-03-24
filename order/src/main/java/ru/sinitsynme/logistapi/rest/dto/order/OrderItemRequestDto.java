package ru.sinitsynme.logistapi.rest.dto.order;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDto {

    private UUID productId;
    @Min(1)
    private int quantity;
}
