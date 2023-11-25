package ru.sinitsynme.logistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sinitsynme.logistapi.entity.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    Optional<Address> findById(UUID id);

}
