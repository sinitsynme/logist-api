package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sinitsynme.logistapi.entity.enums.HolidayType;

import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;
    @Enumerated(EnumType.STRING)
    private HolidayType holidayType;
    @Column(nullable = false)
    private Date periodFrom;
    @Column(nullable = false)
    private Date periodTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(driver.getId(), holiday.driver.getId()) && holidayType == holiday.holidayType && Objects.equals(periodFrom, holiday.periodFrom) && Objects.equals(periodTo, holiday.periodTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driver.getId(), holidayType, periodFrom, periodTo);
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", driverId=" + driver.getId() +
                ", holidayType=" + holidayType +
                ", periodFrom=" + periodFrom +
                ", periodTo=" + periodTo +
                '}';
    }
}
