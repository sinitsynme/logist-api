package ru.sinitsynme.logistapi.rest.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sinitsynme.logistapi.rest.dto.address.AddressResponseDto;
import ru.sinitsynme.logistapi.rest.dto.document.DocumentResponseDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {

    private String clientOrganizationInn;
    private Long warehouseId;
    private List<OrderItemResponseDto> orderItemList = new ArrayList<>();
    private BigDecimal finalSum = getFinalSum();
    private AddressResponseDto actualOrderAddress;
    private List<DocumentResponseDto> documentResponseDtos = new ArrayList<>();

    public BigDecimal getFinalSum() {
        return orderItemList.stream()
                .map(it -> it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
