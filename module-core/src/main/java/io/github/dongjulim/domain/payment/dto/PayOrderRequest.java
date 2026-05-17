package io.github.dongjulim.domain.payment.dto;

import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PayOrderRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private PaymentMethod method;
}
