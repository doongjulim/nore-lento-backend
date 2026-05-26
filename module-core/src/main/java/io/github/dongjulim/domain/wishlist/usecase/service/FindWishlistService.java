package io.github.dongjulim.domain.wishlist.usecase.service;

import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.wishlist.dto.FindWishlistResponse;
import io.github.dongjulim.domain.wishlist.repository.WishlistRepository;
import io.github.dongjulim.domain.wishlist.usecase.FindWishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindWishlistService implements FindWishlistUseCase {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserLoader userLoader;

    @Override
    public List<FindWishlistResponse> findWishlist(String username) {
        User user = userLoader.load(username);

        return wishlistRepository.findAllByUserId(user.getId()).stream()
                .flatMap(wishlist -> productRepository.findByIdAndDeleteCheckFalse(wishlist.getProductId())
                        .map(product -> FindWishlistResponse.from(wishlist, product))
                        .stream())
                .collect(Collectors.toList());
    }
}
