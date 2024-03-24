package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sinitsynme.logistapi.entity.enums.OrderStatus;
import ru.sinitsynme.logistapi.entity.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "actual_order_address_id")
    private Address actualOrderAddress;

    @Column(nullable = false)
    private Long warehouseId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    @OneToMany(mappedBy = "id.order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Document> documentsList = new ArrayList<>();

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
                ", warehouseId=" + warehouseId +
                '}';
    }
}
