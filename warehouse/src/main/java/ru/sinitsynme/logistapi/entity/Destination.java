package ru.sinitsynme.logistapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sinitsynme.logistapi.entity.enums.DestinationStatus;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private UUID orderId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    private Date completedAt;
    @Enumerated(EnumType.STRING)
    private DestinationStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination that = (Destination) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(address, that.address) && Objects.equals(completedAt, that.completedAt) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, address, completedAt, status);
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", address=" + address +
                ", completedAt=" + completedAt +
                ", status=" + status +
                '}';
    }
}
