package io.github.dongjulim.domain.productCategory.repository;

import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByIdAndDeleteCheckFalse(Long id);

    List<ProductCategory> findAllByDeleteCheckFalse();
}
