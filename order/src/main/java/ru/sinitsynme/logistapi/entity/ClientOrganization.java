package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
public class ClientOrganization {

    @Id
    private UUID clientId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private String name;
    private String inn;
    private String bik;
    private String clientAccount;
    private String bankName;
    private String correspondentAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientOrganization that = (ClientOrganization) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(address, that.address) && Objects.equals(name, that.name) && Objects.equals(inn, that.inn) && Objects.equals(bik, that.bik) && Objects.equals(clientAccount, that.clientAccount) && Objects.equals(bankName, that.bankName) && Objects.equals(correspondentAccount, that.correspondentAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, address, name, inn, bik, clientAccount, bankName, correspondentAccount);
    }

    @Override
    public String toString() {
        return "ClientOrganization{" +
                "clientId=" + clientId +
                ", address=" + address +
                ", name='" + name + '\'' +
                ", inn='" + inn + '\'' +
                ", bik='" + bik + '\'' +
                ", clientAccount='" + clientAccount + '\'' +
                ", bankName='" + bankName + '\'' +
                ", correspondentAccount='" + correspondentAccount + '\'' +
                '}';
    }
}
