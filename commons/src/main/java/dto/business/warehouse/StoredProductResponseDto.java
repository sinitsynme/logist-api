package dto.business.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredProductResponseDto {
    private UUID productId;
    private Long warehouseId;
    private String warehouseCode;
    private int availableForReserveQuantity;
    private int quantity;
    private int reservedQuantity;
    private BigDecimal price;
    private int quantum;
}
