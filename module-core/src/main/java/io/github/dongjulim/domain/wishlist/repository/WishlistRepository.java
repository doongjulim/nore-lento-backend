package io.github.dongjulim.domain.wishlist.repository;

import io.github.dongjulim.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

    Optional<Wishlist> findByIdAndUserId(Long id, Long userId);

    List<Wishlist> findAllByUserId(Long userId);
}
