package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.shippingAddress.dto.FindShippingAddressResponse;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import io.github.dongjulim.domain.shippingAddress.usecase.FindShippingAddressesUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindShippingAddressesService implements FindShippingAddressesUseCase {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserLoader userLoader;

    @Override
    public List<FindShippingAddressResponse> findShippingAddresses(String username) {
        User user = userLoader.load(username);
        return shippingAddressRepository.findAllByUserId(user.getId()).stream()
                .map(FindShippingAddressResponse::from)
                .collect(Collectors.toList());
    }
}
