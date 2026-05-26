package io.github.dongjulim.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SaveOrderFromCartRequest {

    @NotNull
    private Long shippingAddressId;

    private Long userCouponId;
}
