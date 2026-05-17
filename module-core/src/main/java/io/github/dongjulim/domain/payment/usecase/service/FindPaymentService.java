package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.common.exception.PaymentNotFoundException;
import io.github.dongjulim.domain.payment.dto.FindPaymentResponse;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.payment.usecase.FindPaymentUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindPaymentService implements FindPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final UserLoader userLoader;

    @Override
    public FindPaymentResponse findPayment(Long paymentId, String username) {
        User user = userLoader.load(username);

        return paymentRepository.findByIdAndUserId(paymentId, user.getId())
                .map(FindPaymentResponse::from)
                .orElseThrow(PaymentNotFoundException::new);
    }
}
