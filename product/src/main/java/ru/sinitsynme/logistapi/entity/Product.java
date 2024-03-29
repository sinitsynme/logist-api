package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.sinitsynme.logistapi.entity.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String name;
    private String description;
    private String pathToImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    private double weight; // in kilos
    private double volume; // in litres

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(weight, product.weight) == 0 &&
                Double.compare(volume, product.volume) == 0 &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(productCategory, product.productCategory) &&
                Objects.equals(manufacturer, product.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, productCategory,
                manufacturer, weight, volume);
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pathToImage='" + pathToImage + '\'' +
                ", productCategory=" + productCategory +
                ", manufacturer=" + manufacturer +
                ", weight=" + weight +
                ", volume=" + volume +
                '}';
    }
}
