package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.CartEmptyException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveOrderFromCartServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private SaveOrderFromCartService saveOrderFromCartService;

    private User user;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        product1 = Product.builder().id(10L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
        product2 = Product.builder().id(20L).name("배").price(3000L).categoryId(1L).deleteCheck(false).build();
    }

    @Test
    @DisplayName("saveOrderFromCart - 장바구니 상품들로 주문이 정상적으로 생성된다")
    void saveOrderFromCart_shouldCreateOrderFromCartItems() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item1 = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(2).build();
        CartItem item2 = CartItem.builder().id(2L).cartId(1L).productId(20L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item1, item2)));

        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(7000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(productRepository.findByIdAndDeleteCheckFalse(20L)).willReturn(Optional.of(product2));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderFromCartService.saveOrderFromCart("testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(7000L); // 2000*2 + 3000*1

        ArgumentCaptor<OrderItem> itemCaptor = ArgumentCaptor.forClass(OrderItem.class);
        then(orderItemRepository).should(org.mockito.Mockito.times(2)).save(itemCaptor.capture());
    }

    @Test
    @DisplayName("saveOrderFromCart - 주문 생성 후 장바구니가 비워진다")
    void saveOrderFromCart_shouldClearCartAfterOrder() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        List<CartItem> mutableItems = new java.util.ArrayList<>(List.of(item));
        ReflectionTestUtils.setField(cart, "cartItems", mutableItems);

        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(2000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderFromCartService.saveOrderFromCart("testuser");

        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("saveOrderFromCart - 장바구니가 없으면 CartNotFoundException을 던진다")
    void saveOrderFromCart_throwsCartNotFoundException_whenCartNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart("testuser"))
                .isInstanceOf(CartNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 장바구니가 비어있으면 CartEmptyException을 던진다")
    void saveOrderFromCart_throwsCartEmptyException_whenCartIsEmpty() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        ReflectionTestUtils.setField(cart, "cartItems", List.of());

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart("testuser"))
                .isInstanceOf(CartEmptyException.class);
    }
}
