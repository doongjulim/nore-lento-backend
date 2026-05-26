package io.github.dongjulim.domain.wishlist.usecase;

import io.github.dongjulim.domain.wishlist.dto.AddWishlistRequest;

public interface AddWishlistUseCase {

    void addWishlist(AddWishlistRequest request, String username);
}
