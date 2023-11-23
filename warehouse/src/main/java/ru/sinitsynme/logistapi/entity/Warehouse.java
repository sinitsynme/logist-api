package ru.sinitsynme.logistapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "warehouse_sequence", allocationSize = 1)
    private Long id;
    private String name;
    private String contactNumber;
    private String email;
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @ManyToOne(fetch = EAGER, targetEntity = Organization.class)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    @OneToMany(mappedBy = "warehouse", fetch = LAZY)
    private List<Driver> driverList;
    @OneToMany(mappedBy = "warehouse", fetch = LAZY)
    private List<CargoTruck> cargoTruckList;
    @OneToMany(mappedBy = "warehouse", fetch = LAZY)
    private List<StoredProduct> storedProducts;

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
