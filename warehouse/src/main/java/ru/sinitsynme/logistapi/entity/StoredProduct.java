package ru.sinitsynme.logistapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class StoredProduct {
    @Id
    private UUID productId;
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    private int quantity;
    private int reservedQuantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoredProduct that = (StoredProduct) o;
        return quantity == that.quantity && reservedQuantity == that.reservedQuantity && Objects.equals(productId, that.productId) && Objects.equals(warehouse.getId(), that.warehouse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, warehouse.getId(), quantity, reservedQuantity);
    }

    @Override
    public String toString() {
        return "StoredProduct{" +
                "productId=" + productId +
                ", warehouse=" + warehouse.getId() +
                ", quantity=" + quantity +
                ", reservedQuantity=" + reservedQuantity +
                '}';
    }
}
