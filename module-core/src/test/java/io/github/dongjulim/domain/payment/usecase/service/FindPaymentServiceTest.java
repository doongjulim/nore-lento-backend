package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.common.exception.PaymentNotFoundException;
import io.github.dongjulim.domain.payment.dto.FindPaymentResponse;
import io.github.dongjulim.domain.payment.entity.Payment;
import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import io.github.dongjulim.domain.payment.enums.PaymentStatus;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindPaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindPaymentService findPaymentService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findPayment - 결제 상세 정보를 반환한다")
    void findPayment_shouldReturnPaymentDetail() {
        Payment payment = Payment.builder()
                .id(1L).orderId(10L).userId(1L)
                .method(PaymentMethod.CARD).status(PaymentStatus.COMPLETED).amount(5000L)
                .build();

        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(payment));

        FindPaymentResponse result = findPaymentService.findPayment(1L, "testuser");

        assertThat(result.getPaymentId()).isEqualTo(1L);
        assertThat(result.getOrderId()).isEqualTo(10L);
        assertThat(result.getMethod()).isEqualTo(PaymentMethod.CARD);
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(result.getAmount()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("findPayment - 존재하지 않거나 타인의 결제이면 PaymentNotFoundException을 던진다")
    void findPayment_throwsPaymentNotFoundException_whenNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findPaymentService.findPayment(99L, "testuser"))
                .isInstanceOf(PaymentNotFoundException.class);
    }
}
