package io.github.dongjulim.domain.payment.usecase;

import io.github.dongjulim.domain.payment.dto.FindPaymentResponse;

public interface FindPaymentUseCase {

    FindPaymentResponse findPayment(Long paymentId, String username);
}
