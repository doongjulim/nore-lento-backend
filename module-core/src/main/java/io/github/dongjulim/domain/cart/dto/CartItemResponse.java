package io.github.dongjulim.domain.cart.dto;

import io.github.dongjulim.domain.cart.entity.CartItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {

    private final Long cartItemId;
    private final Long productId;
    private final String productName;
    private final Long productPrice;
    private final Integer quantity;
    private final Long totalPrice;

    public static CartItemResponse from(CartItem cartItem) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .productId(cartItem.getProductId())
                .productName(cartItem.getProduct().getName())
                .productPrice(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .build();
    }
}
