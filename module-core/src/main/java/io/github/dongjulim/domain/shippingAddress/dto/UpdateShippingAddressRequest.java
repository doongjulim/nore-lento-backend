package io.github.dongjulim.domain.shippingAddress.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateShippingAddressRequest {

    @NotBlank
    private String recipientName;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    private String detailAddress;

    @NotBlank
    private String zipCode;
}
