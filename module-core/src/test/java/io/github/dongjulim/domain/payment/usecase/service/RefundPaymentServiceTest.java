package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.common.exception.PaymentNotFoundException;
import io.github.dongjulim.domain.common.exception.PaymentNotRefundableException;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.payment.entity.Payment;
import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import io.github.dongjulim.domain.payment.enums.PaymentStatus;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RefundPaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private RefundPaymentService refundPaymentService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("refundPayment - COMPLETED 결제가 REFUNDED로 변경되고 주문이 CANCELLED로 변경된다")
    void refundPayment_shouldRefundPaymentAndCancelOrder() {
        Payment payment = Payment.builder().id(1L).orderId(10L).userId(1L).method(PaymentMethod.CARD).status(PaymentStatus.COMPLETED).amount(5000L).build();
        Order order = Order.builder().id(10L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(payment));
        given(orderRepository.findById(10L)).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrderId(10L)).willReturn(List.of());

        refundPaymentService.refundPayment(1L, "testuser");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("refundPayment - 환불 시 주문 상품의 재고가 복원된다")
    void refundPayment_shouldRestoreStock() {
        Payment payment = Payment.builder().id(1L).orderId(10L).userId(1L).method(PaymentMethod.CARD).status(PaymentStatus.COMPLETED).amount(6000L).build();
        Order order = Order.builder().id(10L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(6000L).build();
        OrderItem item = OrderItem.builder().id(1L).orderId(10L).productId(20L).quantity(3).price(2000L).build();
        Stock stock = Stock.builder().id(1L).productId(20L).quantity(7).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(payment));
        given(orderRepository.findById(10L)).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrderId(10L)).willReturn(List.of(item));
        given(stockRepository.findByProductId(20L)).willReturn(Optional.of(stock));

        refundPaymentService.refundPayment(1L, "testuser");

        assertThat(stock.getQuantity()).isEqualTo(10); // 7 + 3
    }

    @Test
    @DisplayName("refundPayment - 존재하지 않거나 타인의 결제이면 PaymentNotFoundException을 던진다")
    void refundPayment_throwsPaymentNotFoundException_whenNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> refundPaymentService.refundPayment(99L, "testuser"))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("refundPayment - PENDING 결제는 환불할 수 없다")
    void refundPayment_throwsPaymentNotRefundableException_whenPending() {
        Payment payment = Payment.builder().id(1L).orderId(10L).userId(1L).method(PaymentMethod.CARD).status(PaymentStatus.PENDING).amount(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(payment));

        assertThatThrownBy(() -> refundPaymentService.refundPayment(1L, "testuser"))
                .isInstanceOf(PaymentNotRefundableException.class);
    }

    @Test
    @DisplayName("refundPayment - 이미 REFUNDED 결제는 환불할 수 없다")
    void refundPayment_throwsPaymentNotRefundableException_whenAlreadyRefunded() {
        Payment payment = Payment.builder().id(1L).orderId(10L).userId(1L).method(PaymentMethod.CARD).status(PaymentStatus.REFUNDED).amount(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(paymentRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(payment));

        assertThatThrownBy(() -> refundPaymentService.refundPayment(1L, "testuser"))
                .isInstanceOf(PaymentNotRefundableException.class);
    }
}
