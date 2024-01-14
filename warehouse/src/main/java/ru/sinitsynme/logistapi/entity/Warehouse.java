package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_sequence")
    @SequenceGenerator(name = "warehouse_sequence", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String contactNumber;
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @ManyToOne(fetch = EAGER, targetEntity = Organization.class)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    @OneToMany(mappedBy = "warehouse", fetch = LAZY, cascade = CascadeType.REMOVE)
    private List<Driver> driverList;
    @OneToMany(mappedBy = "warehouse", fetch = LAZY, cascade = CascadeType.REMOVE)
    private List<CargoTruck> cargoTruckList;
    @OneToMany(mappedBy = "id.warehouse", fetch = LAZY, cascade = CascadeType.REMOVE)
    private List<StoredProduct> storedProducts;
    private int storedProductsCodeCounter;

    public String getStoredProductsCodeCounterString() {
        return String.valueOf(storedProductsCodeCounter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(name, warehouse.name) && Objects.equals(contactNumber, warehouse.contactNumber) && Objects.equals(email, warehouse.email) && Objects.equals(address, warehouse.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, contactNumber, email, address);
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }
}
