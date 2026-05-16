package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.order.dto.FindOrderDetailResponse;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
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
class FindOrderDetailServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindOrderDetailService findOrderDetailService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findOrderDetail - 주문 상세 정보를 반환한다")
    void findOrderDetail_shouldReturnOrderDetail() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.PENDING).totalPrice(5000L).build();
        OrderItem item = OrderItem.builder().id(1L).orderId(1L).productId(10L).quantity(2).price(2500L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrderId(1L)).willReturn(List.of(item));

        FindOrderDetailResponse result = findOrderDetailService.findOrderDetail(1L, "testuser");

        assertThat(result.getOrderId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getTotalPrice()).isEqualTo(5000L);
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getProductId()).isEqualTo(10L);
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(result.getItems().get(0).getPrice()).isEqualTo(2500L);
    }

    @Test
    @DisplayName("findOrderDetail - 존재하지 않는 주문이면 OrderNotFoundException을 던진다")
    void findOrderDetail_throwsOrderNotFoundException_whenOrderNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findOrderDetailService.findOrderDetail(99L, "testuser"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("findOrderDetail - 다른 사용자의 주문이면 OrderNotFoundException을 던진다")
    void findOrderDetail_throwsOrderNotFoundException_whenOrderBelongsToAnotherUser() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findOrderDetailService.findOrderDetail(1L, "testuser"))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
