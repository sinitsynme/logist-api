package ru.sinitsynme.logistapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.entity.StoredProductId;
import ru.sinitsynme.logistapi.repository.jpql.ProductMinPrice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoredProductRepository extends JpaRepository<StoredProduct, StoredProductId> {

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

    @Query(
            value = "SELECT * FROM stored_product WHERE product_id = ?1",
            nativeQuery = true
    )
    List<StoredProduct> findByProductId(UUID productId);

    @Query(
            value = "SELECT s.product_id AS productId, MIN(s.price) AS minimalPrice "
            + "FROM stored_product AS s WHERE s.quantity - s.reserved_quantity > 0 "
            + "GROUP BY s.product_id HAVING s.product_id IN ?1 ",
            nativeQuery = true
    )
    List<ProductMinPrice> calculateMinPriceOfEachProduct(List<UUID> productIds);
}
