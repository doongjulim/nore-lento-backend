package io.github.dongjulim.domain.delivery.dto;

import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.enums.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindDeliveryResponse {

    private final Long deliveryId;
    private final Long orderId;
    private final String address;
    private final String trackingNumber;
    private final DeliveryStatus status;
    private final LocalDateTime createAt;

    public static FindDeliveryResponse from(Delivery delivery) {
        return FindDeliveryResponse.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .address(delivery.getAddress())
                .trackingNumber(delivery.getTrackingNumber())
                .status(delivery.getStatus())
                .createAt(delivery.getCreateAt())
                .build();
    }
}
