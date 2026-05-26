package io.github.dongjulim.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class SaveOrderRequest {

    @NotNull
    private Long shippingAddressId;

    private Long userCouponId;

    @Valid
    @NotEmpty
    private List<OrderItemRequest> orderItems;
}
