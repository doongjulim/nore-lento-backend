package io.github.dongjulim.domain.order.dto;

import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FindOrderDetailResponse {

    private final Long orderId;
    private final OrderStatus status;
    private final Long totalPrice;
    private final Long shippingAddressId;
    private final List<OrderItemResponse> items;
    private final LocalDateTime createAt;

    public static FindOrderDetailResponse from(Order order, List<OrderItemResponse> items) {
        return FindOrderDetailResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .shippingAddressId(order.getShippingAddressId())
                .items(items)
                .createAt(order.getCreateAt())
                .build();
    }
}
