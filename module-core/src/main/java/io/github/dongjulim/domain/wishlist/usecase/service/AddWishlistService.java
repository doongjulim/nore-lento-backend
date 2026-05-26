package io.github.dongjulim.domain.wishlist.usecase.service;

import io.github.dongjulim.domain.common.exception.AlreadyWishlistedException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.wishlist.dto.AddWishlistRequest;
import io.github.dongjulim.domain.wishlist.entity.Wishlist;
import io.github.dongjulim.domain.wishlist.repository.WishlistRepository;
import io.github.dongjulim.domain.wishlist.usecase.AddWishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddWishlistService implements AddWishlistUseCase {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserLoader userLoader;

    @Override
    public void addWishlist(AddWishlistRequest request, String username) {
        User user = userLoader.load(username);

        Product product = productRepository.findByIdAndDeleteCheckFalse(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        wishlistRepository.findByUserIdAndProductId(user.getId(), product.getId())
                .ifPresent(w -> { throw new AlreadyWishlistedException(); });

        wishlistRepository.save(Wishlist.builder()
                .userId(user.getId())
                .productId(product.getId())
                .build());
    }
}
