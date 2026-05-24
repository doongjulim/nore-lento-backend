package io.github.dongjulim.domain.shippingAddress.usecase;

import io.github.dongjulim.domain.shippingAddress.dto.FindShippingAddressResponse;

import java.util.List;

public interface FindShippingAddressesUseCase {

    List<FindShippingAddressResponse> findShippingAddresses(String username);
}
