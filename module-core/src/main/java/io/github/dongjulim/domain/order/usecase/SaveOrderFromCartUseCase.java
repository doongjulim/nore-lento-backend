package io.github.dongjulim.domain.order.usecase;

public interface SaveOrderFromCartUseCase {

    void saveOrderFromCart(Long shippingAddressId, String username);
}
