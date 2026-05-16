package io.github.dongjulim.domain.order.usecase;

import io.github.dongjulim.domain.order.dto.SaveOrderRequest;

public interface SaveOrderUseCase {

    void saveOrder(SaveOrderRequest request, String username);
}
