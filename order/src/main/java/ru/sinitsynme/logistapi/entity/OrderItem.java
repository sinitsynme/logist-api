package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ordered_product")
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;
    private BigDecimal price;
    private int quantity;
    private int returnedQuantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem that = (OrderItem) o;
        return quantity == that.quantity && returnedQuantity == that.returnedQuantity && Objects.equals(id, that.id) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, quantity, returnedQuantity);
    }

    @Override
    public String toString() {
        return "OrderedProduct{" +
                "id=" + id +
                ", price=" + price +
                ", quantity=" + quantity +
                ", returnedQuantity=" + returnedQuantity +
                '}';
    }
}
