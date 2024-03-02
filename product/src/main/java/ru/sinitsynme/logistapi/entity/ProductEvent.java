package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.sinitsynme.logistapi.entity.enums.ProductStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_event")
public class ProductEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID productId;
    private LocalDateTime registeredAt;
    @Enumerated(EnumType.STRING)
    private ProductStatus newStatus;
    private UUID initiatorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEvent that = (ProductEvent) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(registeredAt, that.registeredAt) &&
                newStatus == that.newStatus &&
                Objects.equals(initiatorId, that.initiatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registeredAt, newStatus, initiatorId);
    }

    @Override
    public String toString() {
        return "ProductEvent{" +
                "id=" + id +
                ", registeredAt=" + registeredAt +
                ", newStatus=" + newStatus +
                ", initiatorId=" + initiatorId +
                '}';
    }
}
