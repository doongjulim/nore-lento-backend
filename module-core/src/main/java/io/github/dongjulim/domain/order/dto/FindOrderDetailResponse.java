package io.github.dongjulim.domain.order.dto;

import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FindOrderDetailResponse {

    private final Long orderId;
    private final OrderStatus status;
    private final Long totalPrice;
    private final Long shippingAddressId;
    private final List<OrderItemResponse> items;
    private final LocalDateTime createAt;

    public static FindOrderDetailResponse from(Order order, List<OrderItem> items) {
        return FindOrderDetailResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .shippingAddressId(order.getShippingAddressId())
                .items(items.stream().map(OrderItemResponse::from).collect(Collectors.toList()))
                .createAt(order.getCreateAt())
                .build();
    }
}
