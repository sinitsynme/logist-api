package ru.sinitsynme.logistapi.entity;

import lombok.*;

import javax.persistence.*;
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

    private String warehouseCode;
    private String name;
    private String description;
    private String pathToImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_code")
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    private BigDecimal price;
    private double weight; // in kilos
    private double volume; // in litres

    private boolean isPackaged;
    private int quantityInPackage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(weight, product.weight) == 0 && Double.compare(volume, product.volume) == 0 && isPackaged == product.isPackaged && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(productCategory, product.productCategory) && Objects.equals(manufacturer, product.manufacturer) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, productCategory, manufacturer, price, weight, volume, isPackaged);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productCategory=" + productCategory +
                ", manufacturer=" + manufacturer +
                ", price=" + price +
                ", weight=" + weight +
                ", volume=" + volume +
                ", isPackaged=" + isPackaged +
                '}';
    }
}
