package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.CartEmptyException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.common.exception.OutOfStockException;
import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.order.component.OrderCreationHelper;
import io.github.dongjulim.domain.order.dto.SaveOrderFromCartRequest;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
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
    private StockRepository stockRepository;

    @Mock
    private UserLoader userLoader;

    @Mock
    private OrderCreationHelper orderCreationHelper;

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

    private SaveOrderFromCartRequest buildRequest(Long shippingAddressId, Long userCouponId) {
        SaveOrderFromCartRequest request = new SaveOrderFromCartRequest();
        ReflectionTestUtils.setField(request, "shippingAddressId", shippingAddressId);
        ReflectionTestUtils.setField(request, "userCouponId", userCouponId);
        return request;
    }

    @Test
    @DisplayName("saveOrderFromCart - 장바구니 상품들로 주문이 생성된다")
    void saveOrderFromCart_shouldCreateOrderFromCartItems() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item1 = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(2).build();
        CartItem item2 = CartItem.builder().id(2L).cartId(1L).productId(20L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item1, item2)));

        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                .totalPrice(7000L).shippingAddressId(1L).build();
        Stock stock1 = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Stock stock2 = Stock.builder().id(2L).productId(20L).quantity(5).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(orderCreationHelper.resolveShippingAddressId(1L, 1L)).willReturn(1L);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(productRepository.findByIdAndDeleteCheckFalse(20L)).willReturn(Optional.of(product2));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock1));
        given(stockRepository.findByProductId(20L)).willReturn(Optional.of(stock2));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(7000L);
        assertThat(orderCaptor.getValue().getShippingAddressId()).isEqualTo(1L);

        then(orderItemRepository).should(org.mockito.Mockito.times(2)).save(any(OrderItem.class));
    }

    @Test
    @DisplayName("saveOrderFromCart - 주문 시 재고가 주문 수량만큼 차감된다")
    void saveOrderFromCart_shouldDecreaseStock() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(2).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(orderCreationHelper.resolveShippingAddressId(1L, 1L)).willReturn(1L);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(4000L).shippingAddressId(1L).build());

        saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser");

        assertThat(stock.getQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("saveOrderFromCart - 재고가 부족하면 OutOfStockException을 던진다")
    void saveOrderFromCart_throwsOutOfStockException_whenStockInsufficient() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(5).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        Stock stock = Stock.builder().id(1L).productId(10L).quantity(3).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(orderCreationHelper.resolveShippingAddressId(1L, 1L)).willReturn(1L);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(10000L).shippingAddressId(1L).build());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser"))
                .isInstanceOf(OutOfStockException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 주문 생성 후 장바구니가 비워진다")
    void saveOrderFromCart_shouldClearCartAfterOrder() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(orderCreationHelper.resolveShippingAddressId(1L, 1L)).willReturn(1L);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(2000L).shippingAddressId(1L).build());

        saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser");

        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("saveOrderFromCart - 장바구니가 없으면 CartNotFoundException을 던진다")
    void saveOrderFromCart_throwsCartNotFoundException_whenCartNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser"))
                .isInstanceOf(CartNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 장바구니가 비어있으면 CartEmptyException을 던진다")
    void saveOrderFromCart_throwsCartEmptyException_whenCartIsEmpty() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        ReflectionTestUtils.setField(cart, "cartItems", List.of());

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser"))
                .isInstanceOf(CartEmptyException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 배송지를 찾을 수 없으면 ShippingAddressNotFoundException을 던진다")
    void saveOrderFromCart_throwsShippingAddressNotFoundException_whenAddressNotFound() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(orderCreationHelper.resolveShippingAddressId(1L, 1L))
                .willThrow(new ShippingAddressNotFoundException());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 쿠폰이 있으면 helper.applyCoupon이 호출되고 할인된 금액으로 주문이 생성된다")
    void saveOrderFromCart_shouldApplyCouponDiscount_whenCouponProvided() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(3).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(orderCreationHelper.resolveShippingAddressId(1L, 1L)).willReturn(1L);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(orderCreationHelper.applyCoupon(1L, 1L, 6000L)).willReturn(5000L);
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).totalPrice(5000L).shippingAddressId(1L).build());

        saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, 1L), "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("saveOrderFromCart - 포인트 사용 시 helper.applyPoints가 호출되고 차감된 금액으로 주문이 생성된다")
    void saveOrderFromCart_shouldDeductPoints_whenPointsProvided() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(3).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        SaveOrderFromCartRequest request = buildRequest(1L, null);
        ReflectionTestUtils.setField(request, "usePoints", 1000L);

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(orderCreationHelper.resolveShippingAddressId(1L, 1L)).willReturn(1L);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(orderCreationHelper.applyPoints(1000L, 1L, 6000L)).willReturn(5000L);
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).totalPrice(5000L).shippingAddressId(1L).build());

        saveOrderFromCartService.saveOrderFromCart(request, "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(5000L);
    }
}
