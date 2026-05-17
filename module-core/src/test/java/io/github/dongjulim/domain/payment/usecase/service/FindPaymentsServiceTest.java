package io.github.dongjulim.domain.payment.usecase.service;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindPaymentsServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindPaymentsService findPaymentsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findPayments - 사용자의 결제 목록을 반환한다")
    void findPayments_shouldReturnPaymentList() {
        Payment p1 = Payment.builder().id(1L).orderId(10L).userId(1L).method(PaymentMethod.CARD).status(PaymentStatus.COMPLETED).amount(5000L).build();
        Payment p2 = Payment.builder().id(2L).orderId(20L).userId(1L).method(PaymentMethod.KAKAO_PAY).status(PaymentStatus.REFUNDED).amount(3000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findAllByUserId(1L)).willReturn(List.of(p1, p2));

        List<FindPaymentResponse> result = findPaymentsService.findPayments("testuser");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPaymentId()).isEqualTo(1L);
        assertThat(result.get(1).getStatus()).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    @DisplayName("findPayments - 결제가 없으면 빈 목록을 반환한다")
    void findPayments_shouldReturnEmptyList_whenNoPayments() {
        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findAllByUserId(1L)).willReturn(List.of());

        List<FindPaymentResponse> result = findPaymentsService.findPayments("testuser");

        assertThat(result).isEmpty();
    }
}
