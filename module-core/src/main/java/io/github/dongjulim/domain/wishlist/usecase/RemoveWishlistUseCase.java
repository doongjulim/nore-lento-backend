package io.github.dongjulim.domain.wishlist.usecase;

public interface RemoveWishlistUseCase {

    void removeWishlist(Long wishlistId, String username);
}
