package io.github.dongjulim.domain.cart.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    private CartItem createCartItem(int quantity) {
        return CartItem.builder()
                .id(1L)
                .cartId(1L)
                .productId(10L)
                .quantity(quantity)
                .build();
    }

    @Test
    @DisplayName("addQuantity - 수량이 증가된다")
    void addQuantity_shouldIncreaseQuantity() {
        CartItem cartItem = createCartItem(2);

        cartItem.addQuantity(3);

        assertThat(cartItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("addQuantity - 수량 증가 후 cartId는 유지된다")
    void addQuantity_shouldNotChangeCartId() {
        CartItem cartItem = createCartItem(2);

        cartItem.addQuantity(1);

        assertThat(cartItem.getCartId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("updateQuantity - 수량이 새 값으로 변경된다")
    void updateQuantity_shouldSetNewQuantity() {
        CartItem cartItem = createCartItem(5);

        cartItem.updateQuantity(10);

        assertThat(cartItem.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("updateQuantity - 수량 변경 후 productId는 유지된다")
    void updateQuantity_shouldNotChangeProductId() {
        CartItem cartItem = createCartItem(5);

        cartItem.updateQuantity(1);

        assertThat(cartItem.getProductId()).isEqualTo(10L);
    }
}
