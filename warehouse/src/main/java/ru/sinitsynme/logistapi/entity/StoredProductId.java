package ru.sinitsynme.logistapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class StoredProductId implements Serializable {
    private static final long serialVersionUID = -4090542817685847499L;

    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoredProductId that = (StoredProductId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(warehouse, that.warehouse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, warehouse);
    }

    @Override
    public String toString() {
        return "StoredProductId{" +
                "productId=" + productId +
                ", warehouse=" + warehouse.getId() +
                '}';
    }
}
