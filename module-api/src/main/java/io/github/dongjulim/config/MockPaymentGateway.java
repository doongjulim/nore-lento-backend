package io.github.dongjulim.config;

import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import io.github.dongjulim.domain.payment.gateway.PaymentGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public String approve(Long orderId, Long amount, PaymentMethod method) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void cancel(String transactionId) {
        // 항상 성공 (실제 PG 교체 시 이 구현체만 변경)
    }
}
