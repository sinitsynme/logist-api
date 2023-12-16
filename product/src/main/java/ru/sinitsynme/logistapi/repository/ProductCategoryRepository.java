package ru.sinitsynme.logistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.ProductCategory;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    Optional<ProductCategory> findByCategoryCode(String categoryCode);
    Optional<ProductCategory> findByCategoryName(String categoryName);

    boolean existsByCategoryCode(String categoryCode);
    boolean existsByCategoryName(String categoryName);
}

