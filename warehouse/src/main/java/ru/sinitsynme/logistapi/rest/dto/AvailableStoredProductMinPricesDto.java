package ru.sinitsynme.logistapi.rest.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableStoredProductMinPricesDto {
    private UUID productId;
    private BigDecimal minimalPrice;
}
