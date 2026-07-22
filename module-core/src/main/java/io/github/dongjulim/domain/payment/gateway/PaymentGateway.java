package io.github.dongjulim.domain.payment.gateway;

import io.github.dongjulim.domain.payment.enums.PaymentMethod;

public interface PaymentGateway {
    String approve(Long orderId, Long amount, PaymentMethod method);
    void cancel(String transactionId);
}
