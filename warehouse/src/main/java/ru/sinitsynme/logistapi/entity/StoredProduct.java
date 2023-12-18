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

    @EmbeddedId
    private StoredProductId id;

    private String warehouseCode;

    @Column(nullable = false)
    private int quantity;

    private int reservedQuantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoredProduct that = (StoredProduct) o;
        return quantity == that.quantity && reservedQuantity == that.reservedQuantity && Objects.equals(id, that.id) && Objects.equals(warehouseCode, that.warehouseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, warehouseCode, quantity, reservedQuantity);
    }

    @Override
    public String toString() {
        return "StoredProduct{" +
                "id=" + id +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", quantity=" + quantity +
                ", reservedQuantity=" + reservedQuantity +
                '}';
    }
}
