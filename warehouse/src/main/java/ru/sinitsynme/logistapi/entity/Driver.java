package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_sequence")
    @SequenceGenerator(name = "driver_sequence", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String patronymic;
    @Column(nullable = false)
    private String drivingLicenseNumber;
    @ManyToOne(targetEntity = Warehouse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
    private Date createdAt;
    private Date updatedAt;
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(firstName, driver.firstName) && Objects.equals(lastName, driver.lastName) && Objects.equals(patronymic, driver.patronymic) && Objects.equals(drivingLicenseNumber, driver.drivingLicenseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, drivingLicenseNumber);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", drivingLicenseNumber='" + drivingLicenseNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
