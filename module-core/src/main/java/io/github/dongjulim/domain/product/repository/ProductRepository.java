package io.github.dongjulim.domain.product.repository;

import io.github.dongjulim.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndDeleteCheckFalse(Long id);

    @Query("SELECT p FROM product p WHERE p.deleteCheck = false" +
            " AND (:categoryId IS NULL OR p.categoryId = :categoryId)" +
            " AND (:keyword IS NULL OR p.name LIKE %:keyword%)" +
            " AND (:minPrice IS NULL OR p.price >= :minPrice)" +
            " AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findAllBySearchCondition(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable
    );
}
