package io.github.dongjulim.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveOrderFromCartRequest {

    private Long shippingAddressId;

    private Long userCouponId;

    private Long usePoints;
}
