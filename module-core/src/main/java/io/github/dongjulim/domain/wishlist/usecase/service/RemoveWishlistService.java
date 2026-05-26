package io.github.dongjulim.domain.wishlist.usecase.service;

import io.github.dongjulim.domain.common.exception.WishlistNotFoundException;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.wishlist.entity.Wishlist;
import io.github.dongjulim.domain.wishlist.repository.WishlistRepository;
import io.github.dongjulim.domain.wishlist.usecase.RemoveWishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveWishlistService implements RemoveWishlistUseCase {

    private final WishlistRepository wishlistRepository;
    private final UserLoader userLoader;

    @Override
    public void removeWishlist(Long wishlistId, String username) {
        User user = userLoader.load(username);

        Wishlist wishlist = wishlistRepository.findByIdAndUserId(wishlistId, user.getId())
                .orElseThrow(WishlistNotFoundException::new);

        wishlistRepository.delete(wishlist);
    }
}
