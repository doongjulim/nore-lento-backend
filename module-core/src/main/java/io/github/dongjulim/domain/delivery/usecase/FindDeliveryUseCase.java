package io.github.dongjulim.domain.delivery.usecase;

import io.github.dongjulim.domain.delivery.dto.FindDeliveryResponse;

public interface FindDeliveryUseCase {

    FindDeliveryResponse findDelivery(Long orderId, String username);
}
