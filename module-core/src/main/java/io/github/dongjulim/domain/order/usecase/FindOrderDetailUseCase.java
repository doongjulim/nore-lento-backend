package io.github.dongjulim.domain.order.usecase;

import io.github.dongjulim.domain.order.dto.FindOrderDetailResponse;

public interface FindOrderDetailUseCase {

    FindOrderDetailResponse findOrderDetail(Long orderId, String username);
}
