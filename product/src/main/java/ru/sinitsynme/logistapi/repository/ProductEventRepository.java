package ru.sinitsynme.logistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.ProductEvent;

import java.util.UUID;

@Repository
public interface ProductEventRepository extends JpaRepository<ProductEvent, UUID> {
}
