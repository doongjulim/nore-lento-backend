package io.github.dongjulim.domain.order.entity;

import io.github.dongjulim.domain.common.exception.OrderNotCancellableException;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    @DisplayName("cancel - PENDING 상태의 주문이 CANCELLED로 변경된다")
    void cancel_shouldChangePendingToCancelled() {
        Order order = Order.builder()
                .id(1L)
                .userId(1L)
                .status(OrderStatus.PENDING)
                .totalPrice(10000L)
                .build();

        order.cancel();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancel - 이미 CANCELLED 상태이면 OrderNotCancellableException을 던진다")
    void cancel_throwsOrderNotCancellableException_whenAlreadyCancelled() {
        Order order = Order.builder()
                .id(1L)
                .userId(1L)
                .status(OrderStatus.CANCELLED)
                .totalPrice(10000L)
                .build();

        assertThatThrownBy(order::cancel)
                .isInstanceOf(OrderNotCancellableException.class);
    }

    @Test
    @DisplayName("cancel - COMPLETED 상태이면 OrderNotCancellableException을 던진다")
    void cancel_throwsOrderNotCancellableException_whenCompleted() {
        Order order = Order.builder()
                .id(1L)
                .userId(1L)
                .status(OrderStatus.COMPLETED)
                .totalPrice(10000L)
                .build();

        assertThatThrownBy(order::cancel)
                .isInstanceOf(OrderNotCancellableException.class);
    }
}
