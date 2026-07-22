package io.github.dongjulim.domain.payment.dto;

import io.github.dongjulim.domain.payment.entity.Payment;
import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import io.github.dongjulim.domain.payment.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindPaymentResponse {

    private final Long paymentId;
    private final Long orderId;
    private final PaymentMethod method;
    private final PaymentStatus status;
    private final Long amount;
    private final String transactionId;
    private final LocalDateTime createAt;

    public static FindPaymentResponse from(Payment payment) {
        return FindPaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .createAt(payment.getCreateAt())
                .build();
    }
}
