package io.github.dongjulim.domain.wishlist.usecase.service;

import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.wishlist.dto.FindWishlistResponse;
import io.github.dongjulim.domain.wishlist.entity.Wishlist;
import io.github.dongjulim.domain.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindWishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindWishlistService findWishlistService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findWishlist - 찜 목록을 상품 정보와 함께 반환한다")
    void findWishlist_shouldReturnWishlistItems() {
        Wishlist wishlist1 = Wishlist.builder().id(1L).userId(1L).productId(10L).build();
        Wishlist wishlist2 = Wishlist.builder().id(2L).userId(1L).productId(20L).build();
        Product product1 = Product.builder().id(10L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
        Product product2 = Product.builder().id(20L).name("배").price(3000L).categoryId(1L).deleteCheck(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(wishlistRepository.findAllByUserId(1L)).willReturn(List.of(wishlist1, wishlist2));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(productRepository.findByIdAndDeleteCheckFalse(20L)).willReturn(Optional.of(product2));

        List<FindWishlistResponse> result = findWishlistService.findWishlist("testuser");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getProductName()).isEqualTo("사과");
        assertThat(result.get(0).getProductPrice()).isEqualTo(2000L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getProductName()).isEqualTo("배");
    }

    @Test
    @DisplayName("findWishlist - 찜 목록이 없으면 빈 리스트를 반환한다")
    void findWishlist_shouldReturnEmptyList_whenNoWishlist() {
        given(userLoader.load("testuser")).willReturn(user);
        given(wishlistRepository.findAllByUserId(1L)).willReturn(List.of());

        List<FindWishlistResponse> result = findWishlistService.findWishlist("testuser");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findWishlist - 삭제된 상품은 찜 목록에서 제외된다")
    void findWishlist_shouldExcludeDeletedProducts() {
        Wishlist wishlist1 = Wishlist.builder().id(1L).userId(1L).productId(10L).build();
        Wishlist wishlist2 = Wishlist.builder().id(2L).userId(1L).productId(20L).build();
        Product product1 = Product.builder().id(10L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(wishlistRepository.findAllByUserId(1L)).willReturn(List.of(wishlist1, wishlist2));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(productRepository.findByIdAndDeleteCheckFalse(20L)).willReturn(Optional.empty());

        List<FindWishlistResponse> result = findWishlistService.findWishlist("testuser");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName()).isEqualTo("사과");
    }
}
