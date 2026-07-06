package io.github.dongjulim.domain.cart.dto;

import io.github.dongjulim.domain.cart.entity.Cart;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class FindCartResponse {

    private final Long cartId;
    private final List<CartItemResponse> items;
    private final Long totalAmount;

    public static FindCartResponse from(Cart cart, Map<Long, Integer> stockQuantities) {
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(item -> CartItemResponse.from(item, stockQuantities.getOrDefault(item.getProductId(), 0)))
                .collect(Collectors.toList());

        long totalAmount = items.stream()
                .mapToLong(CartItemResponse::getTotalPrice)
                .sum();

        return FindCartResponse.builder()
                .cartId(cart.getId())
                .items(items)
                .totalAmount(totalAmount)
                .build();
    }

    public static FindCartResponse empty() {
        return FindCartResponse.builder()
                .cartId(null)
                .items(List.of())
                .totalAmount(0L)
                .build();
    }
}
