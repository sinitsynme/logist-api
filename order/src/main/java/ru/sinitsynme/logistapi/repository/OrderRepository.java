package ru.sinitsynme.logistapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.ClientOrganization;
import ru.sinitsynme.logistapi.entity.Order;
import ru.sinitsynme.logistapi.entity.enums.OrderStatus;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByClientOrganizationAndStatusIn(
            ClientOrganization clientOrganization,
            Collection<OrderStatus> orderStatuses,
            Pageable pageable
    );

    Page<Order> findByWarehouseIdAndStatusIn(
            UUID warehouseId,
            Collection<OrderStatus> orderStatuses,
            Pageable pageable
    );

}
