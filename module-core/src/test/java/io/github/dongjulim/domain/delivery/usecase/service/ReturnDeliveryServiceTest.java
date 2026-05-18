package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
import io.github.dongjulim.domain.common.exception.DeliveryNotReturnableException;
import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.enums.DeliveryStatus;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
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
class ReturnDeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private ReturnDeliveryService returnDeliveryService;

    @Test
    @DisplayName("returnDelivery - DELIVERED 배송이 RETURNED로 변경된다")
    void returnDelivery_shouldChangeDeliveredToReturned() {
        Delivery delivery = Delivery.builder().id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.DELIVERED).build();

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        returnDeliveryService.returnDelivery(1L);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.RETURNED);
    }

    @Test
    @DisplayName("returnDelivery - 존재하지 않는 배송이면 DeliveryNotFoundException을 던진다")
    void returnDelivery_throwsDeliveryNotFoundException_whenNotFound() {
        given(deliveryRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> returnDeliveryService.returnDelivery(99L))
                .isInstanceOf(DeliveryNotFoundException.class);
    }

    @Test
    @DisplayName("returnDelivery - DELIVERED가 아닌 배송이면 DeliveryNotReturnableException을 던진다")
    void returnDelivery_throwsDeliveryNotReturnableException_whenNotDelivered() {
        Delivery delivery = Delivery.builder().id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.SHIPPING).build();

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        assertThatThrownBy(() -> returnDeliveryService.returnDelivery(1L))
                .isInstanceOf(DeliveryNotReturnableException.class);
    }
}
