package io.github.dongjulim.domain.cart.usecase;

import io.github.dongjulim.domain.cart.dto.FindCartResponse;

public interface FindCartUseCase {

    FindCartResponse findCart(String username);
}
