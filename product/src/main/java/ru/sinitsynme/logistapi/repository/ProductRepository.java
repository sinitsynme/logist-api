package ru.sinitsynme.logistapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.Product;
import ru.sinitsynme.logistapi.entity.ProductCategory;
import ru.sinitsynme.logistapi.entity.enums.ProductStatus;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findById(UUID id);

    Page<Product> findByNameContainingIgnoreCase(String query, Pageable pageable);

    Page<Product> findByStatusAndProductCategoryIn(ProductStatus status,
                                                   Collection<ProductCategory> categories,
                                                   Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

}
