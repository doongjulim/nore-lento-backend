package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderNotCancellableException;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
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
class CancelOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private CancelOrderService cancelOrderService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("cancelOrder - PENDING 상태의 주문이 정상적으로 취소된다")
    void cancelOrder_shouldCancelPendingOrder() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.PENDING).totalPrice(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrderId(1L)).willReturn(List.of());

        cancelOrderService.cancelOrder(1L, "testuser");

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancelOrder - 취소 시 주문 상품의 재고가 복원된다")
    void cancelOrder_shouldRestoreStock() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.PENDING).totalPrice(6000L).build();
        OrderItem item1 = OrderItem.builder().id(1L).orderId(1L).productId(10L).quantity(3).price(2000L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(7).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrderId(1L)).willReturn(List.of(item1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));

        cancelOrderService.cancelOrder(1L, "testuser");

        assertThat(stock.getQuantity()).isEqualTo(10); // 7 + 3
    }

    @Test
    @DisplayName("cancelOrder - 쿠폰을 사용한 주문 취소 시 쿠폰이 복원된다")
    void cancelOrder_shouldRestoreCoupon_whenCouponWasUsed() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.PENDING).totalPrice(5000L).userCouponId(10L).build();
        UserCoupon userCoupon = UserCoupon.builder().id(10L).userId(1L).couponId(1L).isUsed(true).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrderId(1L)).willReturn(List.of());
        given(userCouponRepository.findById(10L)).willReturn(Optional.of(userCoupon));

        cancelOrderService.cancelOrder(1L, "testuser");

        assertThat(userCoupon.getIsUsed()).isFalse();
        assertThat(userCoupon.getUsedAt()).isNull();
    }

    @Test
    @DisplayName("cancelOrder - 존재하지 않는 주문이면 OrderNotFoundException을 던진다")
    void cancelOrder_throwsOrderNotFoundException_whenOrderNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> cancelOrderService.cancelOrder(99L, "testuser"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("cancelOrder - 이미 CANCELLED 상태의 주문은 OrderNotCancellableException을 던진다")
    void cancelOrder_throwsOrderNotCancellableException_whenAlreadyCancelled() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.CANCELLED).totalPrice(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> cancelOrderService.cancelOrder(1L, "testuser"))
                .isInstanceOf(OrderNotCancellableException.class);
    }

    @Test
    @DisplayName("cancelOrder - COMPLETED 상태의 주문은 OrderNotCancellableException을 던진다")
    void cancelOrder_throwsOrderNotCancellableException_whenCompleted() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> cancelOrderService.cancelOrder(1L, "testuser"))
                .isInstanceOf(OrderNotCancellableException.class);
    }
}
