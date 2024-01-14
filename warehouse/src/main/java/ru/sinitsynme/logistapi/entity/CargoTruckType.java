package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
public class CargoTruckType {
    @Id
    private String brand;
    //Litres TODO remove after swagger is done
    private Double maxVolume;
    //Tons TODO remove after swagger is done
    private Double maxWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CargoTruckType that = (CargoTruckType) o;
        return Objects.equals(brand, that.brand) && Objects.equals(maxVolume, that.maxVolume) && Objects.equals(maxWeight, that.maxWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, maxVolume, maxWeight);
    }

    @Override
    public String toString() {
        return "CargoTruckType{" +
                "brand='" + brand + '\'' +
                ", maxVolume=" + maxVolume +
                ", maxWeight=" + maxWeight +
                '}';
    }
}
