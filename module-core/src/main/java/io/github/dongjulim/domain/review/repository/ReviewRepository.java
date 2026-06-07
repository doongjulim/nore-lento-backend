package io.github.dongjulim.domain.review.repository;

import io.github.dongjulim.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndDeleteCheckFalse(Long id);

    @Query(value = "SELECT r FROM review r JOIN FETCH r.user WHERE r.productId = :productId AND r.deleteCheck = false AND (:rating IS NULL OR r.rating = :rating)",
           countQuery = "SELECT COUNT(r) FROM review r WHERE r.productId = :productId AND r.deleteCheck = false AND (:rating IS NULL OR r.rating = :rating)")
    Page<Review> findAllByProductId(@Param("productId") Long productId, @Param("rating") Integer rating, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM review r WHERE r.productId = :productId AND r.deleteCheck = false")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    long countByProductIdAndDeleteCheckFalse(Long productId);

    boolean existsByUserIdAndProductIdAndDeleteCheckFalse(Long userId, Long productId);
}
