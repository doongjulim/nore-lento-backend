package io.github.dongjulim.domain.cart.usecase;

import io.github.dongjulim.domain.cart.dto.AddCartItemRequest;

public interface AddCartItemUseCase {

    void addCartItem(AddCartItemRequest request, String username);
}
