package io.github.dongjulim.domain.payment.usecase;

import io.github.dongjulim.domain.payment.dto.PayOrderRequest;

public interface PayOrderUseCase {

    void payOrder(PayOrderRequest request, String username);
}
