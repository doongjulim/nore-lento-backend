package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
import io.github.dongjulim.domain.common.exception.DeliveryStatusNotAdvancableException;
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
class UpdateDeliveryStatusServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private UpdateDeliveryStatusService updateDeliveryStatusService;

    @Test
    @DisplayName("updateDeliveryStatus - PREPARING 배송이 SHIPPING으로 변경된다")
    void updateDeliveryStatus_shouldChangePreparingToShipping() {
        Delivery delivery = Delivery.builder().id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.PREPARING).build();

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        updateDeliveryStatusService.updateDeliveryStatus(1L);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.SHIPPING);
    }

    @Test
    @DisplayName("updateDeliveryStatus - SHIPPING 배송이 DELIVERED로 변경된다")
    void updateDeliveryStatus_shouldChangeShippingToDelivered() {
        Delivery delivery = Delivery.builder().id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.SHIPPING).build();

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        updateDeliveryStatusService.updateDeliveryStatus(1L);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
    }

    @Test
    @DisplayName("updateDeliveryStatus - 존재하지 않는 배송이면 DeliveryNotFoundException을 던진다")
    void updateDeliveryStatus_throwsDeliveryNotFoundException_whenNotFound() {
        given(deliveryRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateDeliveryStatusService.updateDeliveryStatus(99L))
                .isInstanceOf(DeliveryNotFoundException.class);
    }

    @Test
    @DisplayName("updateDeliveryStatus - DELIVERED 상태에서 DeliveryStatusNotAdvancableException을 던진다")
    void updateDeliveryStatus_throwsDeliveryStatusNotAdvancableException_whenDelivered() {
        Delivery delivery = Delivery.builder().id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.DELIVERED).build();

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        assertThatThrownBy(() -> updateDeliveryStatusService.updateDeliveryStatus(1L))
                .isInstanceOf(DeliveryStatusNotAdvancableException.class);
    }
}
