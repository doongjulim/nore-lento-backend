package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderNotPayableException;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.payment.dto.PayOrderRequest;
import io.github.dongjulim.domain.payment.entity.Payment;
import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import io.github.dongjulim.domain.payment.enums.PaymentStatus;
import io.github.dongjulim.domain.payment.gateway.PaymentGateway;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.point.usecase.EarnPointUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PayOrderServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserLoader userLoader;

    @Mock
    private EarnPointUseCase earnPointUseCase;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private PayOrderService payOrderService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("payOrder - PENDING 주문에 대해 결제가 생성되고 주문이 COMPLETED로 변경된다")
    void payOrder_shouldCreatePaymentAndCompleteOrder() {
        Order order = Order.builder().id(10L).userId(1L).status(OrderStatus.PENDING).totalPrice(5000L).build();

        PayOrderRequest request = new PayOrderRequest();
        ReflectionTestUtils.setField(request, "orderId", 10L);
        ReflectionTestUtils.setField(request, "method", PaymentMethod.CARD);

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(10L, 1L)).willReturn(Optional.of(order));

        payOrderService.payOrder(request, "testuser");

        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        then(paymentRepository).should().save(captor.capture());
        Payment saved = captor.getValue();
        assertThat(saved.getOrderId()).isEqualTo(10L);
        assertThat(saved.getUserId()).isEqualTo(1L);
        assertThat(saved.getMethod()).isEqualTo(PaymentMethod.CARD);
        assertThat(saved.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(saved.getAmount()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("payOrder - 결제 완료 시 주문 총액의 1%가 포인트로 적립된다")
    void payOrder_shouldEarnPoint_afterPaymentCompleted() {
        Order order = Order.builder().id(10L).userId(1L).status(OrderStatus.PENDING).totalPrice(10000L).build();

        PayOrderRequest request = new PayOrderRequest();
        ReflectionTestUtils.setField(request, "orderId", 10L);
        ReflectionTestUtils.setField(request, "method", PaymentMethod.CARD);

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(10L, 1L)).willReturn(Optional.of(order));

        payOrderService.payOrder(request, "testuser");

        then(earnPointUseCase).should().earnPoint(1L, 10000L);
    }

    @Test
    @DisplayName("payOrder - 존재하지 않는 주문이면 OrderNotFoundException을 던진다")
    void payOrder_throwsOrderNotFoundException_whenOrderNotFound() {
        PayOrderRequest request = new PayOrderRequest();
        ReflectionTestUtils.setField(request, "orderId", 99L);
        ReflectionTestUtils.setField(request, "method", PaymentMethod.CARD);

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> payOrderService.payOrder(request, "testuser"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("payOrder - PENDING이 아닌 주문이면 OrderNotPayableException을 던진다")
    void payOrder_throwsOrderNotPayableException_whenOrderNotPending() {
        Order order = Order.builder().id(10L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(5000L).build();

        PayOrderRequest request = new PayOrderRequest();
        ReflectionTestUtils.setField(request, "orderId", 10L);
        ReflectionTestUtils.setField(request, "method", PaymentMethod.CARD);

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(10L, 1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> payOrderService.payOrder(request, "testuser"))
                .isInstanceOf(OrderNotPayableException.class);
    }
}
