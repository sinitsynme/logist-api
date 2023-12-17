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
    private Long warehouseId;
    @Min(value = 1)
    private int quantity;
}
