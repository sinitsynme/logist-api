package ru.sinitsynme.logistapi.rest.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoredProductInfoResponseDto {

    private UUID productId;
    private boolean isAvailable;
    private BigDecimal minimalPrice;
    private List<SupplyResponseDto> supplyList;
}
