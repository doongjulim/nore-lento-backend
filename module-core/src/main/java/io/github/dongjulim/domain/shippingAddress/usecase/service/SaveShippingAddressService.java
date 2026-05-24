package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.shippingAddress.dto.SaveShippingAddressRequest;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import io.github.dongjulim.domain.shippingAddress.usecase.SaveShippingAddressUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveShippingAddressService implements SaveShippingAddressUseCase {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserLoader userLoader;

    @Override
    public void saveShippingAddress(SaveShippingAddressRequest request, String username) {
        User user = userLoader.load(username);

        if (request.getIsDefault()) {
            shippingAddressRepository.findByUserIdAndIsDefaultTrue(user.getId())
                    .ifPresent(ShippingAddress::unsetDefault);
        }

        shippingAddressRepository.save(ShippingAddress.builder()
                .userId(user.getId())
                .recipientName(request.getRecipientName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .zipCode(request.getZipCode())
                .isDefault(request.getIsDefault())
                .build());
    }
}
