package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.shippingAddress.dto.UpdateShippingAddressRequest;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import io.github.dongjulim.domain.shippingAddress.usecase.UpdateShippingAddressUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateShippingAddressService implements UpdateShippingAddressUseCase {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserLoader userLoader;

    @Override
    public void updateShippingAddress(Long id, UpdateShippingAddressRequest request, String username) {
        User user = userLoader.load(username);
        ShippingAddress address = shippingAddressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(ShippingAddressNotFoundException::new);
        address.update(request.getRecipientName(), request.getPhone(), request.getAddress(),
                request.getDetailAddress(), request.getZipCode());
    }
}
