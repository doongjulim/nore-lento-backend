package io.github.dongjulim.domain.delivery.usecase;

public interface ChangeDeliveryAddressUseCase {
    void changeAddress(Long deliveryId, String address);
}
