package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.dto.FindCartResponse;
import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.repository.CartRepository;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindCartService findCartService;

    @Test
    @DisplayName("findCart - 장바구니가 존재하면 장바구니 정보를 반환한다")
    void findCart_shouldReturnCartResponse_whenCartExists() {
        User user = User.builder().id(1L).username("testuser").build();
        Cart cart = Cart.builder().id(100L).userId(1L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));

        FindCartResponse result = findCartService.findCart("testuser");

        assertThat(result.getCartId()).isEqualTo(100L);
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalAmount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("findCart - 장바구니가 없으면 빈 응답을 반환한다")
    void findCart_shouldReturnEmptyResponse_whenCartNotFound() {
        User user = User.builder().id(1L).username("testuser").build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.empty());

        FindCartResponse result = findCartService.findCart("testuser");

        assertThat(result.getCartId()).isNull();
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalAmount()).isEqualTo(0L);
    }
}
