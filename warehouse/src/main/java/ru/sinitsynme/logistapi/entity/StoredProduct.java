package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
