package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.sinitsynme.logistapi.entity.enums.OrderEventType;
import ru.sinitsynme.logistapi.entity.enums.OrderStatus;
import ru.sinitsynme.logistapi.entity.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_event")
public class OrderEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private LocalDateTime registeredAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus newStatus;
    @Enumerated(EnumType.STRING)
    private PaymentStatus newPaymentStatus;
    @Enumerated(EnumType.STRING)
    private OrderEventType type;
    private UUID initiatorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEvent that = (OrderEvent) o;
        return Objects.equals(id, that.id) && Objects.equals(registeredAt, that.registeredAt) && newStatus == that.newStatus && newPaymentStatus == that.newPaymentStatus && Objects.equals(initiatorId, that.initiatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registeredAt, newStatus, newPaymentStatus, initiatorId);
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "id=" + id +
                ", order=" + order.getId() +
                ", registeredAt=" + registeredAt +
                ", newStatus=" + newStatus +
                ", newPaymentStatus=" + newPaymentStatus +
                ", initiatorId=" + initiatorId +
                '}';
    }
}
