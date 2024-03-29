package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String categoryCode;
    private String categoryName;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productCategory")
    private List<Product> productList;

    public ProductCategory(String categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return Objects.equals(categoryCode, that.categoryCode) && Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryCode, categoryName);
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "categoryCode='" + categoryCode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
