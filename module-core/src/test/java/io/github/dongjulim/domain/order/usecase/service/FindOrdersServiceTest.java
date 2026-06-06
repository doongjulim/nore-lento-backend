package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.order.dto.FindOrderResponse;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.enums.OrderStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindOrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindOrdersService findOrdersService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findOrders - status가 null이면 사용자의 전체 주문 목록을 반환한다")
    void findOrders_shouldReturnOrderList_whenStatusIsNull() {
        Order order1 = Order.builder().id(1L).userId(1L).status(OrderStatus.PENDING).totalPrice(5000L).build();
        Order order2 = Order.builder().id(2L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(10000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findAllByUserId(1L)).willReturn(List.of(order1, order2));

        List<FindOrderResponse> result = findOrdersService.findOrders("testuser", null);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOrderId()).isEqualTo(1L);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.get(1).getOrderId()).isEqualTo(2L);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("findOrders - status를 지정하면 해당 상태의 주문만 반환한다")
    void findOrders_shouldReturnFilteredOrders_whenStatusIsGiven() {
        Order pendingOrder = Order.builder().id(1L).userId(1L).status(OrderStatus.PENDING).totalPrice(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findAllByUserIdAndStatus(1L, OrderStatus.PENDING)).willReturn(List.of(pendingOrder));

        List<FindOrderResponse> result = findOrdersService.findOrders("testuser", OrderStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("findOrders - 주문이 없으면 빈 목록을 반환한다")
    void findOrders_shouldReturnEmptyList_whenNoOrders() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findAllByUserId(1L)).willReturn(List.of());

        List<FindOrderResponse> result = findOrdersService.findOrders("testuser", null);

        assertThat(result).isEmpty();
    }
}
