package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderAlreadyHasDeliveryException;
import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderNotCompletedException;
import io.github.dongjulim.domain.delivery.dto.CreateDeliveryRequest;
import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.enums.DeliveryStatus;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderRepository;
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
class CreateDeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CreateDeliveryService createDeliveryService;

    @Test
    @DisplayName("createDelivery - COMPLETED 주문에 대해 배송이 정상적으로 생성된다")
    void createDelivery_shouldSaveDelivery_whenOrderIsCompleted() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(5000L).build();

        CreateDeliveryRequest request = new CreateDeliveryRequest();
        ReflectionTestUtils.setField(request, "orderId", 1L);
        ReflectionTestUtils.setField(request, "address", "서울시 강남구 테헤란로 1");
        ReflectionTestUtils.setField(request, "trackingNumber", "123456789");

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(deliveryRepository.existsByOrderId(1L)).willReturn(false);

        createDeliveryService.createDelivery(request);

        ArgumentCaptor<Delivery> captor = ArgumentCaptor.forClass(Delivery.class);
        then(deliveryRepository).should().save(captor.capture());
        Delivery saved = captor.getValue();
        assertThat(saved.getOrderId()).isEqualTo(1L);
        assertThat(saved.getAddress()).isEqualTo("서울시 강남구 테헤란로 1");
        assertThat(saved.getTrackingNumber()).isEqualTo("123456789");
        assertThat(saved.getStatus()).isEqualTo(DeliveryStatus.PREPARING);
    }

    @Test
    @DisplayName("createDelivery - 존재하지 않는 주문이면 OrderNotFoundException을 던진다")
    void createDelivery_throwsOrderNotFoundException_whenOrderNotFound() {
        CreateDeliveryRequest request = new CreateDeliveryRequest();
        ReflectionTestUtils.setField(request, "orderId", 99L);
        ReflectionTestUtils.setField(request, "address", "서울시 강남구");
        ReflectionTestUtils.setField(request, "trackingNumber", null);

        given(orderRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> createDeliveryService.createDelivery(request))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("createDelivery - COMPLETED가 아닌 주문이면 OrderNotCompletedException을 던진다")
    void createDelivery_throwsOrderNotCompletedException_whenOrderNotCompleted() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.PENDING).totalPrice(5000L).build();

        CreateDeliveryRequest request = new CreateDeliveryRequest();
        ReflectionTestUtils.setField(request, "orderId", 1L);
        ReflectionTestUtils.setField(request, "address", "서울시 강남구");
        ReflectionTestUtils.setField(request, "trackingNumber", null);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> createDeliveryService.createDelivery(request))
                .isInstanceOf(OrderNotCompletedException.class);
    }

    @Test
    @DisplayName("createDelivery - 이미 배송이 있는 주문이면 OrderAlreadyHasDeliveryException을 던진다")
    void createDelivery_throwsOrderAlreadyHasDeliveryException_whenDeliveryAlreadyExists() {
        Order order = Order.builder().id(1L).userId(1L).status(OrderStatus.COMPLETED).totalPrice(5000L).build();

        CreateDeliveryRequest request = new CreateDeliveryRequest();
        ReflectionTestUtils.setField(request, "orderId", 1L);
        ReflectionTestUtils.setField(request, "address", "서울시 강남구");
        ReflectionTestUtils.setField(request, "trackingNumber", null);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(deliveryRepository.existsByOrderId(1L)).willReturn(true);

        assertThatThrownBy(() -> createDeliveryService.createDelivery(request))
                .isInstanceOf(OrderAlreadyHasDeliveryException.class);
    }
}
