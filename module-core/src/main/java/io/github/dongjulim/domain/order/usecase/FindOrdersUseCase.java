package io.github.dongjulim.domain.order.usecase;

import io.github.dongjulim.domain.order.dto.FindOrderResponse;

import java.util.List;

public interface FindOrdersUseCase {

    List<FindOrderResponse> findOrders(String username);
}
