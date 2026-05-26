package io.github.dongjulim.domain.order.usecase;

import io.github.dongjulim.domain.order.dto.SaveOrderFromCartRequest;

public interface SaveOrderFromCartUseCase {

    void saveOrderFromCart(SaveOrderFromCartRequest request, String username);
}
