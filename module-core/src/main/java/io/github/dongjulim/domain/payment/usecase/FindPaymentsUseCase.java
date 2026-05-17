package io.github.dongjulim.domain.payment.usecase;

import io.github.dongjulim.domain.payment.dto.FindPaymentResponse;

import java.util.List;

public interface FindPaymentsUseCase {

    List<FindPaymentResponse> findPayments(String username);
}
