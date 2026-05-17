package io.github.dongjulim.domain.payment.usecase;

public interface RefundPaymentUseCase {

    void refundPayment(Long paymentId, String username);
}
