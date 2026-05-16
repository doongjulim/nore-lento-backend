package io.github.dongjulim.domain.order.dto;

import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindOrderResponse {

    private final Long orderId;
    private final OrderStatus status;
    private final Long totalPrice;
    private final LocalDateTime createAt;

    public static FindOrderResponse from(Order order) {
        return FindOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createAt(order.getCreateAt())
                .build();
    }
}
