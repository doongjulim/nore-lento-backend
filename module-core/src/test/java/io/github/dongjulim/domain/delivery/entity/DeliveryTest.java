package io.github.dongjulim.domain.delivery.entity;

import io.github.dongjulim.domain.common.exception.DeliveryNotReturnableException;
import io.github.dongjulim.domain.common.exception.DeliveryStatusNotAdvancableException;
import io.github.dongjulim.domain.delivery.enums.DeliveryStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeliveryTest {

    @Test
    @DisplayName("advance - PREPARING 상태가 SHIPPING으로 변경된다")
    void advance_shouldChangePreparingToShipping() {
        Delivery delivery = Delivery.builder()
                .id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.PREPARING).build();

        delivery.advance();

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.SHIPPING);
    }

    @Test
    @DisplayName("advance - SHIPPING 상태가 DELIVERED로 변경된다")
    void advance_shouldChangeShippingToDelivered() {
        Delivery delivery = Delivery.builder()
                .id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.SHIPPING).build();

        delivery.advance();

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
    }

    @Test
    @DisplayName("advance - DELIVERED 상태에서 DeliveryStatusNotAdvancableException을 던진다")
    void advance_throwsDeliveryStatusNotAdvancableException_whenDelivered() {
        Delivery delivery = Delivery.builder()
                .id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.DELIVERED).build();

        assertThatThrownBy(delivery::advance)
                .isInstanceOf(DeliveryStatusNotAdvancableException.class);
    }

    @Test
    @DisplayName("advance - RETURNED 상태에서 DeliveryStatusNotAdvancableException을 던진다")
    void advance_throwsDeliveryStatusNotAdvancableException_whenReturned() {
        Delivery delivery = Delivery.builder()
                .id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.RETURNED).build();

        assertThatThrownBy(delivery::advance)
                .isInstanceOf(DeliveryStatusNotAdvancableException.class);
    }

    @Test
    @DisplayName("returnDelivery - DELIVERED 상태가 RETURNED로 변경된다")
    void returnDelivery_shouldChangeDeliveredToReturned() {
        Delivery delivery = Delivery.builder()
                .id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.DELIVERED).build();

        delivery.returnDelivery();

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.RETURNED);
    }

    @Test
    @DisplayName("returnDelivery - DELIVERED가 아닌 상태에서 DeliveryNotReturnableException을 던진다")
    void returnDelivery_throwsDeliveryNotReturnableException_whenNotDelivered() {
        Delivery delivery = Delivery.builder()
                .id(1L).orderId(1L).address("서울시 강남구").status(DeliveryStatus.SHIPPING).build();

        assertThatThrownBy(delivery::returnDelivery)
                .isInstanceOf(DeliveryNotReturnableException.class);
    }
}
