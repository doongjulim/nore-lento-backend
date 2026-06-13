package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.CartEmptyException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.common.exception.InsufficientPointException;
import io.github.dongjulim.domain.common.exception.OutOfStockException;
import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.order.dto.SaveOrderFromCartRequest;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
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
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserLoader userLoader;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private SaveOrderFromCartService saveOrderFromCartService;

    private User user;
    private Product product1;
    private Product product2;
    private ShippingAddress shippingAddress;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        product1 = Product.builder().id(10L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
        product2 = Product.builder().id(20L).name("배").price(3000L).categoryId(1L).deleteCheck(false).build();
        shippingAddress = ShippingAddress.builder().id(1L).userId(1L).recipientName("홍길동")
                .phone("010-1234-5678").address("서울시 강남구").zipCode("12345").build();
    }

    private SaveOrderFromCartRequest buildRequest(Long shippingAddressId, Long userCouponId) {
        SaveOrderFromCartRequest request = new SaveOrderFromCartRequest();
        ReflectionTestUtils.setField(request, "shippingAddressId", shippingAddressId);
        ReflectionTestUtils.setField(request, "userCouponId", userCouponId);
        return request;
    }

    @Test
    @DisplayName("saveOrderFromCart - 장바구니 상품들로 주문이 배송지 ID와 함께 생성된다")
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
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
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

        ArgumentCaptor<OrderItem> itemCaptor = ArgumentCaptor.forClass(OrderItem.class);
        then(orderItemRepository).should(org.mockito.Mockito.times(2)).save(itemCaptor.capture());
    }

    @Test
    @DisplayName("saveOrderFromCart - 주문 시 재고가 주문 수량만큼 차감된다")
    void saveOrderFromCart_shouldDecreaseStock() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(2).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                .totalPrice(4000L).shippingAddressId(1L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser");

        assertThat(stock.getQuantity()).isEqualTo(8); // 10 - 2
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
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                        .totalPrice(10000L).shippingAddressId(1L).build());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser"))
                .isInstanceOf(OutOfStockException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 주문 생성 후 장바구니가 비워진다")
    void saveOrderFromCart_shouldClearCartAfterOrder() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        List<CartItem> mutableItems = new java.util.ArrayList<>(List.of(item));
        ReflectionTestUtils.setField(cart, "cartItems", mutableItems);

        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                .totalPrice(2000L).shippingAddressId(1L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

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
    @DisplayName("saveOrderFromCart - 존재하지 않거나 다른 사용자의 배송지면 ShippingAddressNotFoundException을 던진다")
    void saveOrderFromCart_throwsShippingAddressNotFoundException_whenAddressNotFound() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, null), "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 쿠폰 적용 시 할인된 금액으로 주문이 생성되고 쿠폰이 사용 처리된다")
    void saveOrderFromCart_shouldApplyDiscountAndMarkCouponUsed() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(3).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon coupon = Coupon.builder().id(10L).name("1000원 할인").discountType(DiscountType.FIXED).discountValue(1000L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Order savedOrder = Order.builder().id(100L).userId(1L).totalPrice(5000L).shippingAddressId(1L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, 1L), "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(5000L); // 6000 - 1000
        assertThat(userCoupon.getIsUsed()).isTrue();
    }

    @Test
    @DisplayName("saveOrderFromCart - shippingAddressId가 null이면 기본 배송지를 자동으로 사용한다")
    void saveOrderFromCart_shouldUseDefaultShippingAddress_whenShippingAddressIdIsNull() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        ShippingAddress defaultAddress = ShippingAddress.builder().id(5L).userId(1L)
                .recipientName("홍길동").phone("010-1234-5678").address("서울시 강남구").zipCode("12345").build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                .totalPrice(2000L).shippingAddressId(5L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.of(defaultAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderFromCartService.saveOrderFromCart(buildRequest(null, null), "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getShippingAddressId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("saveOrderFromCart - shippingAddressId가 null이고 기본 배송지도 없으면 ShippingAddressNotFoundException을 던진다")
    void saveOrderFromCart_throwsShippingAddressNotFoundException_whenNoDefaultAddress() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(null, null), "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrderFromCart - 존재하지 않는 쿠폰이면 CouponNotFoundException을 던진다")
    void saveOrderFromCart_throwsCouponNotFoundException_whenCouponNotFound() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(userCouponRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequest(1L, 99L), "testuser"))
                .isInstanceOf(CouponNotFoundException.class);
    }

    private SaveOrderFromCartRequest buildRequestWithPoints(Long shippingAddressId, Long usePoints) {
        SaveOrderFromCartRequest request = buildRequest(shippingAddressId, null);
        ReflectionTestUtils.setField(request, "usePoints", usePoints);
        return request;
    }

    @Test
    @DisplayName("saveOrderFromCart - 포인트 사용 시 총액에서 사용 포인트가 차감된다")
    void saveOrderFromCart_shouldDeductPoints_whenUsePointsIsProvided() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(3).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        UserPoint userPoint = UserPoint.builder().userId(1L).balance(2000L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Order savedOrder = Order.builder().id(100L).userId(1L).totalPrice(5000L).shippingAddressId(1L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderFromCartService.saveOrderFromCart(buildRequestWithPoints(1L, 1000L), "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(5000L); // 6000 - 1000
        assertThat(userPoint.getBalance()).isEqualTo(1000L); // 2000 - 1000
    }

    @Test
    @DisplayName("saveOrderFromCart - 포인트 잔액이 부족하면 InsufficientPointException을 던진다")
    void saveOrderFromCart_throwsInsufficientPointException_whenPointsNotEnough() {
        Cart cart = Cart.builder().id(1L).userId(1L).build();
        CartItem item = CartItem.builder().id(1L).cartId(1L).productId(10L).quantity(1).build();
        ReflectionTestUtils.setField(cart, "cartItems", new java.util.ArrayList<>(List.of(item)));

        UserPoint userPoint = UserPoint.builder().userId(1L).balance(500L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(cartRepository.findByUserId(1L)).willReturn(Optional.of(cart));
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product1));
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        assertThatThrownBy(() -> saveOrderFromCartService.saveOrderFromCart(buildRequestWithPoints(1L, 3000L), "testuser"))
                .isInstanceOf(InsufficientPointException.class);
    }
}
