package ru.sinitsynme.logistapi.rest.dto.orderEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sinitsynme.logistapi.entity.enums.OrderEventType;
import ru.sinitsynme.logistapi.entity.enums.OrderStatus;
import ru.sinitsynme.logistapi.entity.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventResponseDto {

    private UUID id;
    private UUID orderId;
    private LocalDateTime registeredAt;
    private OrderStatus newStatus;
    private PaymentStatus newPaymentStatus;
    private OrderEventType type;
    private UUID initiatorId;
    private String comment;
}
