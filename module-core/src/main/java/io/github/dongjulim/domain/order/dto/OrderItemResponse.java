package io.github.dongjulim.domain.order.dto;

import io.github.dongjulim.domain.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {

    private final Long productId;
    private final Integer quantity;
    private final Long price;
    private final Long totalPrice;

    public static OrderItemResponse from(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getPrice() * item.getQuantity())
                .build();
    }
}
