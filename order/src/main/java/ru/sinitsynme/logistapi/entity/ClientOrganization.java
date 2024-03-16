package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.sinitsynme.logistapi.entity.enums.OrganizationStatus;

import java.util.ArrayList;
import java.util.List;
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
    private String inn;

    private UUID clientId;

    @Enumerated(EnumType.STRING)
    private OrganizationStatus organizationStatus;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "clientOrganization")
    private List<Address> addressList = new ArrayList<>();

    private String name;
    private String bik;
    private String clientAccount;
    private String bankName;
    private String correspondentAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientOrganization that = (ClientOrganization) o;
        return Objects.equals(inn, that.inn) && Objects.equals(clientId, that.clientId) && organizationStatus == that.organizationStatus
                && Objects.equals(name, that.name) && Objects.equals(bik, that.bik)
                && Objects.equals(clientAccount, that.clientAccount) && Objects.equals(bankName, that.bankName)
                && Objects.equals(correspondentAccount, that.correspondentAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inn, clientId, organizationStatus, name, bik, clientAccount, bankName, correspondentAccount);
    }
}
