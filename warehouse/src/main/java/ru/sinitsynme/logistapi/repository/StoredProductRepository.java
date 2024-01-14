package ru.sinitsynme.logistapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.entity.StoredProductId;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoredProductRepository extends JpaRepository<StoredProduct, StoredProductId> {

    Page<StoredProduct> findByIdProductId(String productId, Pageable pageable);

    @Query(
            value = "SELECT * FROM stored_product WHERE warehouse_id = ?1",
            nativeQuery = true
    )
    Page<StoredProduct> findByWarehouseId(Long warehouseId, Pageable pageable);

    @Query(
            value = "SELECT * FROM stored_product WHERE product_id = ?1 AND warehouse_id = ?2",
            nativeQuery = true
    )
    Optional<StoredProduct> findByProductAndWarehouse(UUID productId, Long warehouseId);
}
