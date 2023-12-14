package ru.sinitsynme.logistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sinitsynme.logistapi.entity.Warehouse;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findById(Long id);

    Optional<Warehouse> findByName(String name);

}
