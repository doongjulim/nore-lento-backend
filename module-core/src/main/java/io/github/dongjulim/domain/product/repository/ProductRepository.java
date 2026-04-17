package io.github.dongjulim.domain.product.repository;

import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndDeleteCheckFalse(Long id);

    @Query("SELECT p FROM product p WHERE p.deleteCheck = false" +
            " AND (:category IS NULL OR p.category = :category)" +
            " AND (:keyword IS NULL OR p.name LIKE %:keyword%)")
    Page<Product> findAllBySearchCondition(
            @Param("category") ProductCategory category,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
