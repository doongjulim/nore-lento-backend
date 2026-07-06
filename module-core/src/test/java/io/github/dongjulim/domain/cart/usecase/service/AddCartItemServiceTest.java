package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.dto.AddCartItemRequest;
import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartItemRepository;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.OutOfStockException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
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
class AddCartItemServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserLoader userLoader;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private AddCartItemService addCartItemService;

    private User user;
    private Product product;
    private Cart cart;
    private Stock stock;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        product = Product.builder().id(10L).name("사과").price(1000L).categoryId(1L).deleteCheck(false).build();
        cart = Cart.builder().id(100L).userId(1L).build();
        stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
    }

    @Test
    @DisplayName("addCartItem - 장바구니에 새 아이템이 추가된다")
    void addCartItem_shouldSaveNewCartItem() {
        AddCartItemRequest request = new AddCartItemRequest();
        ReflectionTestUtils.setField(request, "productId", 10L);
        ReflectionTestUtils.setField(request, "quantity", 2);

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findByCartIdAndProductId(100L, 10L)).willReturn(Optional.empty());

        addCartItemService.addCartItem(request, "testuser");

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        then(cartItemRepository).should().save(captor.capture());
        assertThat(captor.getValue().getProductId()).isEqualTo(10L);
        assertThat(captor.getValue().getQuantity()).isEqualTo(2);
        assertThat(captor.getValue().getCartId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("addCartItem - 이미 존재하는 아이템이면 수량이 증가한다")
    void addCartItem_shouldAddQuantity_whenItemAlreadyExists() {
        AddCartItemRequest request = new AddCartItemRequest();
        ReflectionTestUtils.setField(request, "productId", 10L);
        ReflectionTestUtils.setField(request, "quantity", 3);

        CartItem existingItem = CartItem.builder().id(1L).cartId(100L).productId(10L).quantity(2).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findByCartIdAndProductId(100L, 10L)).willReturn(Optional.of(existingItem));

        addCartItemService.addCartItem(request, "testuser");

        assertThat(existingItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("addCartItem - 장바구니가 없으면 새로 생성한다")
    void addCartItem_shouldCreateNewCart_whenCartNotExists() {
        AddCartItemRequest request = new AddCartItemRequest();
        ReflectionTestUtils.setField(request, "productId", 10L);
        ReflectionTestUtils.setField(request, "quantity", 1);

        Cart newCart = Cart.builder().id(200L).userId(1L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(cartRepository.findByUserId(1L)).willReturn(Optional.empty());
        given(cartRepository.save(any(Cart.class))).willReturn(newCart);
        given(cartItemRepository.findByCartIdAndProductId(200L, 10L)).willReturn(Optional.empty());

        addCartItemService.addCartItem(request, "testuser");

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        then(cartItemRepository).should().save(captor.capture());
        assertThat(captor.getValue().getCartId()).isEqualTo(200L);
    }

    @Test
    @DisplayName("addCartItem - 재고가 부족하면 OutOfStockException을 던진다")
    void addCartItem_throwsOutOfStockException_whenStockInsufficient() {
        AddCartItemRequest request = new AddCartItemRequest();
        ReflectionTestUtils.setField(request, "productId", 10L);
        ReflectionTestUtils.setField(request, "quantity", 20);

        Stock insufficientStock = Stock.builder().id(1L).productId(10L).quantity(5).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(insufficientStock));
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(cartItemRepository.findByCartIdAndProductId(100L, 10L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> addCartItemService.addCartItem(request, "testuser"))
                .isInstanceOf(OutOfStockException.class);
    }

    @Test
    @DisplayName("addCartItem - 존재하지 않는 상품이면 ProductNotFoundException을 던진다")
    void addCartItem_throwsProductNotFoundException_whenProductNotFound() {
        AddCartItemRequest request = new AddCartItemRequest();
        ReflectionTestUtils.setField(request, "productId", 99L);
        ReflectionTestUtils.setField(request, "quantity", 1);

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> addCartItemService.addCartItem(request, "testuser"))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
