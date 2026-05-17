package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.payment.dto.FindPaymentResponse;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.payment.usecase.FindPaymentsUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindPaymentsService implements FindPaymentsUseCase {

    private final PaymentRepository paymentRepository;
    private final UserLoader userLoader;

    @Override
    public List<FindPaymentResponse> findPayments(String username) {
        User user = userLoader.load(username);

        return paymentRepository.findAllByUserId(user.getId()).stream()
                .map(FindPaymentResponse::from)
                .collect(Collectors.toList());
    }
}
