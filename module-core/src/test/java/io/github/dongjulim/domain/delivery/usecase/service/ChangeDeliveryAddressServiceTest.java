package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryAddressNotChangeableException;
import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
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
class ChangeDeliveryAddressServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private ChangeDeliveryAddressService changeDeliveryAddressService;

    @Test
    @DisplayName("changeAddress - PREPARING 상태에서 주소가 변경된다")
    void changeAddress_shouldUpdateAddress_whenPreparing() {
        Delivery delivery = Delivery.builder().id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.PREPARING).build();

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        changeDeliveryAddressService.changeAddress(1L, "부산시 해운대구");

        assertThat(delivery.getAddress()).isEqualTo("부산시 해운대구");
    }

    @Test
    @DisplayName("changeAddress - PREPARING이 아닌 상태에서 DeliveryAddressNotChangeableException을 던진다")
    void changeAddress_throwsDeliveryAddressNotChangeableException_whenNotPreparing() {
        Delivery delivery = Delivery.builder().id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.SHIPPING).build();

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        assertThatThrownBy(() -> changeDeliveryAddressService.changeAddress(1L, "부산시 해운대구"))
                .isInstanceOf(DeliveryAddressNotChangeableException.class);
    }

    @Test
    @DisplayName("changeAddress - 존재하지 않는 배송이면 DeliveryNotFoundException을 던진다")
    void changeAddress_throwsDeliveryNotFoundException_whenNotFound() {
        given(deliveryRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> changeDeliveryAddressService.changeAddress(99L, "부산시 해운대구"))
                .isInstanceOf(DeliveryNotFoundException.class);
    }
}
