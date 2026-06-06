package io.github.dongjulim.domain.order.usecase;

import io.github.dongjulim.domain.order.dto.FindOrderResponse;
import io.github.dongjulim.domain.order.enums.OrderStatus;

import java.util.List;

public interface FindOrdersUseCase {

    List<FindOrderResponse> findOrders(String username, OrderStatus status);
}
