package io.github.dongjulim.domain.shippingAddress.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SaveShippingAddressRequest {

    @NotBlank
    private String recipientName;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    private String detailAddress;

    @NotBlank
    private String zipCode;

    @NotNull
    private Boolean isDefault;
}
