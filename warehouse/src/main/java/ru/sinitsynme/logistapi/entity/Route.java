package ru.sinitsynme.logistapi.entity;

import lombok.*;
import ru.sinitsynme.logistapi.entity.enums.RouteStatus;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToMany(fetch = FetchType.LAZY)
    @OrderColumn(name = "order_index")
    private List<Destination> destinationList;
    @OneToOne
    private Driver driver;
    @OneToOne
    private CargoTruck cargoTruck;
    @OneToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @Enumerated(EnumType.STRING)
    private RouteStatus routeStatus;
    private Date plannedAt;
    private Date completedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(destinationList, route.destinationList) && Objects.equals(driver, route.driver) && Objects.equals(cargoTruck, route.cargoTruck) && Objects.equals(warehouse, route.warehouse) && Objects.equals(plannedAt, route.plannedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationList, driver, cargoTruck, warehouse, plannedAt);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", destinationIds=" + destinationList.stream().map(Destination::getId).toList() +
                ", driver=" + driver +
                ", cargoTruck=" + cargoTruck +
                ", warehouse=" + warehouse +
                ", plannedAt=" + plannedAt +
                ", completedAt=" + completedAt +
                ", routeStatus=" + routeStatus +
                '}';
    }
}
