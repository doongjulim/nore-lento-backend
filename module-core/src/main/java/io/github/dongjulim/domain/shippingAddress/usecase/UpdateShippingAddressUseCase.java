package io.github.dongjulim.domain.shippingAddress.usecase;

import io.github.dongjulim.domain.shippingAddress.dto.UpdateShippingAddressRequest;

public interface UpdateShippingAddressUseCase {

    void updateShippingAddress(Long id, UpdateShippingAddressRequest request, String username);
}
