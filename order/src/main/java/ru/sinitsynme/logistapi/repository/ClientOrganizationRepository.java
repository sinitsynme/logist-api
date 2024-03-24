package ru.sinitsynme.logistapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.ClientOrganization;

import java.util.UUID;

@Repository
public interface ClientOrganizationRepository extends JpaRepository<ClientOrganization, String> {

    Page<ClientOrganization> findAllByClientId(UUID clientId, Pageable pageable);

}
