package dto.business.warehouse;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoredProductActionRequestDto {

    private UUID productId;
    @Min(value = 1, message = "Warehouse ID must be at least 1")
    private Long warehouseId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
