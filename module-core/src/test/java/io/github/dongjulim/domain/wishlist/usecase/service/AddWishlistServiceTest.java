package io.github.dongjulim.domain.wishlist.usecase.service;

import io.github.dongjulim.domain.common.exception.AlreadyWishlistedException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.wishlist.dto.AddWishlistRequest;
import io.github.dongjulim.domain.wishlist.entity.Wishlist;
import io.github.dongjulim.domain.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AddWishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private AddWishlistService addWishlistService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        product = Product.builder().id(10L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
    }

    private AddWishlistRequest buildRequest(Long productId) {
        AddWishlistRequest request = new AddWishlistRequest();
        ReflectionTestUtils.setField(request, "productId", productId);
        return request;
    }

    @Test
    @DisplayName("addWishlist - 상품을 정상적으로 찜한다")
    void addWishlist_shouldSaveWishlist() {
        AddWishlistRequest request = buildRequest(10L);

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(wishlistRepository.findByUserIdAndProductId(1L, 10L)).willReturn(Optional.empty());

        addWishlistService.addWishlist(request, "testuser");

        ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        then(wishlistRepository).should().save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(1L);
        assertThat(captor.getValue().getProductId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("addWishlist - 존재하지 않는 상품이면 ProductNotFoundException을 던진다")
    void addWishlist_throwsProductNotFoundException_whenProductNotFound() {
        AddWishlistRequest request = buildRequest(99L);

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> addWishlistService.addWishlist(request, "testuser"))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("addWishlist - 이미 찜한 상품이면 AlreadyWishlistedException을 던진다")
    void addWishlist_throwsAlreadyWishlistedException_whenAlreadyWishlisted() {
        AddWishlistRequest request = buildRequest(10L);
        Wishlist existingWishlist = Wishlist.builder().id(5L).userId(1L).productId(10L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(wishlistRepository.findByUserIdAndProductId(1L, 10L)).willReturn(Optional.of(existingWishlist));

        assertThatThrownBy(() -> addWishlistService.addWishlist(request, "testuser"))
                .isInstanceOf(AlreadyWishlistedException.class);
    }
}
