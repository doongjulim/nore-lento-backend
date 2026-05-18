package io.github.dongjulim.domain.delivery.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CreateDeliveryRequest {

    @NotNull
    private Long orderId;

    @NotBlank
    private String address;

    private String trackingNumber;
}
