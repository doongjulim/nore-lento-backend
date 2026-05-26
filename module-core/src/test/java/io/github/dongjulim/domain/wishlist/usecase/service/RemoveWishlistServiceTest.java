package io.github.dongjulim.domain.wishlist.usecase.service;

import io.github.dongjulim.domain.common.exception.WishlistNotFoundException;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.wishlist.entity.Wishlist;
import io.github.dongjulim.domain.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RemoveWishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private RemoveWishlistService removeWishlistService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("removeWishlist - 찜을 정상적으로 삭제한다")
    void removeWishlist_shouldDeleteWishlist() {
        Wishlist wishlist = Wishlist.builder().id(5L).userId(1L).productId(10L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(wishlistRepository.findByIdAndUserId(5L, 1L)).willReturn(Optional.of(wishlist));

        removeWishlistService.removeWishlist(5L, "testuser");

        then(wishlistRepository).should().delete(wishlist);
    }

    @Test
    @DisplayName("removeWishlist - 존재하지 않거나 다른 사용자의 찜이면 WishlistNotFoundException을 던진다")
    void removeWishlist_throwsWishlistNotFoundException_whenNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(wishlistRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> removeWishlistService.removeWishlist(99L, "testuser"))
                .isInstanceOf(WishlistNotFoundException.class);
    }
}
