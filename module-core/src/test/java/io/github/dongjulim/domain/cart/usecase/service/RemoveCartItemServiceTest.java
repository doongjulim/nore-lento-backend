package io.github.dongjulim.domain.cart.usecase.service;

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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RemoveCartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private RemoveCartItemService removeCartItemService;

    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        cart = Cart.builder().id(100L).userId(1L).build();
    }

    @Test
    @DisplayName("removeCartItems - 아이템이 정상적으로 삭제된다")
    void removeCartItems_shouldDeleteItems() {
        CartItem item1 = CartItem.builder().id(1L).cartId(100L).productId(10L).quantity(2).build();
        CartItem item2 = CartItem.builder().id(2L).cartId(100L).productId(20L).quantity(1).build();
        List<Long> ids = List.of(1L, 2L);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllById(ids)).willReturn(List.of(item1, item2));

        removeCartItemService.removeCartItems(ids, "testuser");

        then(cartItemRepository).should().deleteAll(List.of(item1, item2));
    }

    @Test
    @DisplayName("removeCartItems - 장바구니가 없으면 CartNotFoundException을 던진다")
    void removeCartItems_throwsCartNotFoundException_whenCartNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> removeCartItemService.removeCartItems(List.of(1L), "testuser"))
                .isInstanceOf(CartNotFoundException.class);
    }

    @Test
    @DisplayName("removeCartItems - 존재하지 않는 아이템 ID가 포함되면 CartItemNotFoundException을 던진다")
    void removeCartItems_throwsCartItemNotFoundException_whenItemCountMismatch() {
        List<Long> ids = List.of(1L, 2L);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllById(ids)).willReturn(
                List.of(CartItem.builder().id(1L).cartId(100L).productId(10L).quantity(1).build())
        );

        assertThatThrownBy(() -> removeCartItemService.removeCartItems(ids, "testuser"))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @Test
    @DisplayName("removeCartItems - 다른 장바구니의 아이템이면 CartItemNotFoundException을 던진다")
    void removeCartItems_throwsCartItemNotFoundException_whenItemBelongsToAnotherCart() {
        CartItem item = CartItem.builder().id(1L).cartId(999L).productId(10L).quantity(1).build();
        List<Long> ids = List.of(1L);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllById(ids)).willReturn(List.of(item));

        assertThatThrownBy(() -> removeCartItemService.removeCartItems(ids, "testuser"))
                .isInstanceOf(CartItemNotFoundException.class);
    }
}
