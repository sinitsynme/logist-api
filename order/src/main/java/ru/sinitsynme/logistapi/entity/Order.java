package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.sinitsynme.logistapi.entity.enums.OrderStatus;
import ru.sinitsynme.logistapi.entity.enums.PaymentStatus;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id")
    private ClientOrganization clientOrganization;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private Address actualOrderAddress;

    private UUID warehouseId;

    @OneToMany(mappedBy = "id.order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderedProduct> orderedProductList;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Document> documentsList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && status == order.status && paymentStatus == order.paymentStatus && Objects.equals(actualOrderAddress, order.actualOrderAddress) && Objects.equals(warehouseId, order.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, paymentStatus, actualOrderAddress, warehouseId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", paymentStatus=" + paymentStatus +
                ", clientOrganization=" + clientOrganization.getClientId() +
                ", actualOrderAddress=" + actualOrderAddress +
                ", warehouseId=" + warehouseId +
                '}';
    }
}
