package io.github.dongjulim.domain.order.usecase;

public interface CancelOrderUseCase {

    void cancelOrder(Long orderId, String username);
}
