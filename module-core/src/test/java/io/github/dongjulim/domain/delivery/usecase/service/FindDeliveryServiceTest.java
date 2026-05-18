package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.delivery.dto.FindDeliveryResponse;
import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.enums.DeliveryStatus;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindDeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindDeliveryService findDeliveryService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findDelivery - 주문의 배송 정보를 정상적으로 조회한다")
    void findDelivery_shouldReturnDeliveryInfo() {
        Order order = Order.builder().id(10L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(5000L).build();
        Delivery delivery = Delivery.builder().id(1L).orderId(10L).address("서울시 강남구").status(DeliveryStatus.SHIPPING).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(10L, 1L)).willReturn(Optional.of(order));
        given(deliveryRepository.findByOrderId(10L)).willReturn(Optional.of(delivery));

        FindDeliveryResponse result = findDeliveryService.findDelivery(10L, "testuser");

        assertThat(result.getDeliveryId()).isEqualTo(1L);
        assertThat(result.getOrderId()).isEqualTo(10L);
        assertThat(result.getAddress()).isEqualTo("서울시 강남구");
        assertThat(result.getStatus()).isEqualTo(DeliveryStatus.SHIPPING);
    }

    @Test
    @DisplayName("findDelivery - 타인의 주문이면 OrderNotFoundException을 던진다")
    void findDelivery_throwsOrderNotFoundException_whenOrderBelongsToAnotherUser() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(10L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findDeliveryService.findDelivery(10L, "testuser"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("findDelivery - 배송이 없으면 DeliveryNotFoundException을 던진다")
    void findDelivery_throwsDeliveryNotFoundException_whenDeliveryNotFound() {
        Order order = Order.builder().id(10L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(orderRepository.findByIdAndUserId(10L, 1L)).willReturn(Optional.of(order));
        given(deliveryRepository.findByOrderId(10L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findDeliveryService.findDelivery(10L, "testuser"))
                .isInstanceOf(DeliveryNotFoundException.class);
    }
}
