package ru.sinitsynme.logistapi.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

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
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = CargoTruckType.class)
    @JoinColumn(name = "cargo_truck_type_id", nullable = false)
    private CargoTruckType type;
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
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
