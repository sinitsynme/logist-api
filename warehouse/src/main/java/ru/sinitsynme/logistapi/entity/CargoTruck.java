package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table
public class CargoTruck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(fetch = EAGER, targetEntity = CargoTruckType.class)
    @JoinColumn(name = "cargo_truck_type_id", nullable = false)
    private CargoTruckType type;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
    @Column(nullable = false)
    private String registrationNumber;
    private Date createdAt;
    private Date updatedAt;
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CargoTruck that = (CargoTruck) o;
        return Objects.equals(registrationNumber, that.registrationNumber);
    }

    @Override
    public int hashCode() {
        return registrationNumber.hashCode();
    }

    @Override
    public String toString() {
        return "CargoTruck{" +
                "id=" + id +
                ", type=" + type +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
