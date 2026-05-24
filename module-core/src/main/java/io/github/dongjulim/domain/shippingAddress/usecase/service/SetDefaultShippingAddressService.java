package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import io.github.dongjulim.domain.shippingAddress.usecase.SetDefaultShippingAddressUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SetDefaultShippingAddressService implements SetDefaultShippingAddressUseCase {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserLoader userLoader;

    @Override
    public void setDefaultShippingAddress(Long id, String username) {
        User user = userLoader.load(username);
        ShippingAddress address = shippingAddressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(ShippingAddressNotFoundException::new);

        shippingAddressRepository.findByUserIdAndIsDefaultTrue(user.getId())
                .ifPresent(ShippingAddress::unsetDefault);

        address.setDefault();
    }
}
