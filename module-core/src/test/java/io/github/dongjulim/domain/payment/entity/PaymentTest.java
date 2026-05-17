package io.github.dongjulim.domain.payment.entity;

import io.github.dongjulim.domain.common.exception.PaymentNotRefundableException;
import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import io.github.dongjulim.domain.payment.enums.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @Test
    @DisplayName("refund - COMPLETED 상태의 결제가 REFUNDED로 변경된다")
    void refund_shouldChangeCompletedToRefunded() {
        Payment payment = Payment.builder()
                .id(1L)
                .orderId(1L)
                .userId(1L)
                .method(PaymentMethod.CARD)
                .status(PaymentStatus.COMPLETED)
                .amount(10000L)
                .build();

        payment.refund();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    @DisplayName("refund - PENDING 상태이면 PaymentNotRefundableException을 던진다")
    void refund_throwsPaymentNotRefundableException_whenPending() {
        Payment payment = Payment.builder()
                .id(1L)
                .orderId(1L)
                .userId(1L)
                .method(PaymentMethod.CARD)
                .status(PaymentStatus.PENDING)
                .amount(10000L)
                .build();

        assertThatThrownBy(payment::refund)
                .isInstanceOf(PaymentNotRefundableException.class);
    }

    @Test
    @DisplayName("refund - 이미 REFUNDED 상태이면 PaymentNotRefundableException을 던진다")
    void refund_throwsPaymentNotRefundableException_whenAlreadyRefunded() {
        Payment payment = Payment.builder()
                .id(1L)
                .orderId(1L)
                .userId(1L)
                .method(PaymentMethod.CARD)
                .status(PaymentStatus.REFUNDED)
                .amount(10000L)
                .build();

        assertThatThrownBy(payment::refund)
                .isInstanceOf(PaymentNotRefundableException.class);
    }
}
