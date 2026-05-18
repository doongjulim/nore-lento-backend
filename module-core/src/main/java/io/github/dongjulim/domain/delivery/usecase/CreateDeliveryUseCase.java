package io.github.dongjulim.domain.delivery.usecase;

import io.github.dongjulim.domain.delivery.dto.CreateDeliveryRequest;

public interface CreateDeliveryUseCase {

    void createDelivery(CreateDeliveryRequest request);
}
