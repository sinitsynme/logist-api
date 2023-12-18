package ru.sinitsynme.logistapi.rest.dto;

import lombok.*;

import javax.validation.constraints.Min;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoredProductRequestDto {

    private UUID productId;
    @Min(value = 1, message = "Warehouse ID must be at least 1")
    private Long warehouseId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
