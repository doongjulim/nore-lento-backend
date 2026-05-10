package io.github.dongjulim.domain.cart.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    @DisplayName("clearItems - 장바구니 아이템이 모두 제거된다")
    void clearItems_shouldRemoveAllItems() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item1 = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(2).build();
        CartItem item2 = CartItem.builder().id(2L).cartId(1L).productId(20L).quantity(1).build();
        cart.getCartItems().add(item1);
        cart.getCartItems().add(item2);

        cart.clearItems();

        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("clearItems - 아이템이 없는 장바구니를 비워도 예외가 발생하지 않는다")
    void clearItems_shouldNotThrow_whenAlreadyEmpty() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();

        cart.clearItems();

        assertThat(cart.getCartItems()).isEmpty();
    }
}
