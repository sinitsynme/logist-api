package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class OrderItemId implements Serializable {

    @Serial
    private static final long serialVersionUID = 4344609258075581567L;

    private UUID productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemId that = (OrderItemId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, order);
    }

    @Override
    public String toString() {
        return "OrderedProductId{" +
                "productId=" + productId +
                ", order=" + order.getId() +
                '}';
    }
}
