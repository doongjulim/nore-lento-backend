package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClearCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private ClearCartService clearCartService;

    @Test
    @DisplayName("clearCart - 장바구니 아이템이 모두 제거된다")
    void clearCart_shouldClearAllItems() {
        User user = User.builder().id(1L).username("testuser").build();
        Cart cart = Cart.builder().id(100L).userId(1L).build();
        CartItem item1 = CartItem.builder().id(1L).cartId(100L).productId(10L).quantity(2).build();
        CartItem item2 = CartItem.builder().id(2L).cartId(100L).productId(20L).quantity(1).build();
        cart.getCartItems().add(item1);
        cart.getCartItems().add(item2);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));

        clearCartService.clearCart("testuser");

        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("clearCart - 장바구니가 없으면 CartNotFoundException을 던진다")
    void clearCart_throwsCartNotFoundException_whenCartNotFound() {
        User user = User.builder().id(1L).username("testuser").build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clearCartService.clearCart("testuser"))
                .isInstanceOf(CartNotFoundException.class);
    }
}
