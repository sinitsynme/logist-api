package ru.sinitsynme.logistapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.StoredProduct;
import ru.sinitsynme.logistapi.entity.StoredProductId;

@Repository
public interface StoredProductRepository extends PagingAndSortingRepository<StoredProduct, StoredProductId> {

    Page<StoredProduct> findByIdProductId(String productId, Pageable pageable);

// TODO -
//    @Query(
//            value = "SELECT * FROM stored_product WHERE warehouse_id = ?1",
//            countQuery = ""
//            nativeQuery = true, )
//    Page<StoredProduct> findByWarehouseId(String warehouseId, Pageable pageable);

}
