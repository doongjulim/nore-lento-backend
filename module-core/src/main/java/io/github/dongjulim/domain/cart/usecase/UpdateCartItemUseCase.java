package io.github.dongjulim.domain.cart.usecase;

import io.github.dongjulim.domain.cart.dto.UpdateCartItemRequest;

public interface UpdateCartItemUseCase {

    void updateCartItem(Long cartItemId, UpdateCartItemRequest request, String username);
}
