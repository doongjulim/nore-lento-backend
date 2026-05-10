package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.dto.UpdateCartItemRequest;
import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartItemRepository;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.CartItemNotFoundException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateCartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private UpdateCartItemService updateCartItemService;

    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        cart = Cart.builder().id(100L).userId(1L).build();
    }

    @Test
    @DisplayName("updateCartItem - 수량이 정상적으로 변경된다")
    void updateCartItem_shouldUpdateQuantity() {
        CartItem cartItem = CartItem.builder().id(1L).cartId(100L).productId(10L).quantity(2).build();
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        ReflectionTestUtils.setField(request, "quantity", 5);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findById(1L)).willReturn(Optional.of(cartItem));

        updateCartItemService.updateCartItem(1L, request, "testuser");

        assertThat(cartItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("updateCartItem - 장바구니가 없으면 CartNotFoundException을 던진다")
    void updateCartItem_throwsCartNotFoundException_whenCartNotFound() {
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        ReflectionTestUtils.setField(request, "quantity", 3);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateCartItemService.updateCartItem(1L, request, "testuser"))
                .isInstanceOf(CartNotFoundException.class);
    }

    @Test
    @DisplayName("updateCartItem - 아이템이 없으면 CartItemNotFoundException을 던진다")
    void updateCartItem_throwsCartItemNotFoundException_whenItemNotFound() {
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        ReflectionTestUtils.setField(request, "quantity", 3);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateCartItemService.updateCartItem(99L, request, "testuser"))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @Test
    @DisplayName("updateCartItem - 다른 장바구니의 아이템이면 CartItemNotFoundException을 던진다")
    void updateCartItem_throwsCartItemNotFoundException_whenItemBelongsToAnotherCart() {
        CartItem cartItem = CartItem.builder().id(1L).cartId(999L).productId(10L).quantity(2).build();
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        ReflectionTestUtils.setField(request, "quantity", 3);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findById(1L)).willReturn(Optional.of(cartItem));

        assertThatThrownBy(() -> updateCartItemService.updateCartItem(1L, request, "testuser"))
                .isInstanceOf(CartItemNotFoundException.class);
    }
}
