package io.github.dongjulim.domain.shippingAddress.usecase;

import io.github.dongjulim.domain.shippingAddress.dto.SaveShippingAddressRequest;

public interface SaveShippingAddressUseCase {

    void saveShippingAddress(SaveShippingAddressRequest request, String username);
}
