package io.github.dongjulim.domain.shippingAddress.dto;

import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindShippingAddressResponse {

    private final Long id;
    private final String recipientName;
    private final String phone;
    private final String address;
    private final String detailAddress;
    private final String zipCode;
    private final Boolean isDefault;

    public static FindShippingAddressResponse from(ShippingAddress shippingAddress) {
        return FindShippingAddressResponse.builder()
                .id(shippingAddress.getId())
                .recipientName(shippingAddress.getRecipientName())
                .phone(shippingAddress.getPhone())
                .address(shippingAddress.getAddress())
                .detailAddress(shippingAddress.getDetailAddress())
                .zipCode(shippingAddress.getZipCode())
                .isDefault(shippingAddress.getIsDefault())
                .build();
    }
}
