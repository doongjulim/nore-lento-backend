package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.CouponAlreadyUsedException;
import io.github.dongjulim.domain.common.exception.CouponExpiredException;
import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.common.exception.InsufficientPointException;
import io.github.dongjulim.domain.common.exception.OrderAmountNotEnoughException;
import io.github.dongjulim.domain.common.exception.OutOfStockException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.order.dto.OrderItemRequest;
import io.github.dongjulim.domain.order.dto.SaveOrderRequest;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.point.entity.UserPoint;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

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

    @InjectMocks
    private SaveOrderService saveOrderService;

    private User user;
    private Product product;
    private ShippingAddress shippingAddress;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        product = Product.builder().id(10L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
        shippingAddress = ShippingAddress.builder().id(1L).userId(1L).recipientName("홍길동")
                .phone("010-1234-5678").address("서울시 강남구").zipCode("12345").build();
    }

    private SaveOrderRequest buildRequest(long productId, int quantity) {
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", productId);
        ReflectionTestUtils.setField(itemRequest, "quantity", quantity);

        SaveOrderRequest request = new SaveOrderRequest();
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));
        ReflectionTestUtils.setField(request, "shippingAddressId", 1L);
        return request;
    }

    @Test
    @DisplayName("saveOrder - 주문 항목 목록으로 주문이 정상적으로 생성된다")
    void saveOrder_shouldSaveOrderWithItems() {
        SaveOrderRequest request = buildRequest(10L, 3);
        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                .totalPrice(6000L).shippingAddressId(1L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertThat(capturedOrder.getUserId()).isEqualTo(1L);
        assertThat(capturedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(capturedOrder.getTotalPrice()).isEqualTo(6000L);
        assertThat(capturedOrder.getShippingAddressId()).isEqualTo(1L);

        ArgumentCaptor<OrderItem> itemCaptor = ArgumentCaptor.forClass(OrderItem.class);
        then(orderItemRepository).should().save(itemCaptor.capture());
        OrderItem capturedItem = itemCaptor.getValue();
        assertThat(capturedItem.getProductId()).isEqualTo(10L);
        assertThat(capturedItem.getQuantity()).isEqualTo(3);
        assertThat(capturedItem.getPrice()).isEqualTo(2000L);
        assertThat(capturedItem.getOrderId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("saveOrder - 주문 시 재고가 주문 수량만큼 차감된다")
    void saveOrder_shouldDecreaseStock() {
        SaveOrderRequest request = buildRequest(10L, 3);
        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                .totalPrice(6000L).shippingAddressId(1L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        assertThat(stock.getQuantity()).isEqualTo(7); // 10 - 3
    }

    @Test
    @DisplayName("saveOrder - 재고가 부족하면 OutOfStockException을 던진다")
    void saveOrder_throwsOutOfStockException_whenStockInsufficient() {
        SaveOrderRequest request = buildRequest(10L, 5);
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(3).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                        .totalPrice(10000L).shippingAddressId(1L).build());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(OutOfStockException.class);
    }

    @Test
    @DisplayName("saveOrder - 재고 정보가 없으면 StockNotFoundException을 던진다")
    void saveOrder_throwsStockNotFoundException_whenStockNotFound() {
        SaveOrderRequest request = buildRequest(10L, 1);

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.empty());
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                        .totalPrice(2000L).shippingAddressId(1L).build());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(StockNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrder - 존재하지 않는 상품이면 ProductNotFoundException을 던진다")
    void saveOrder_throwsProductNotFoundException_whenProductNotFound() {
        SaveOrderRequest request = buildRequest(99L, 1);

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrder - 존재하지 않거나 다른 사용자의 배송지면 ShippingAddressNotFoundException을 던진다")
    void saveOrder_throwsShippingAddressNotFoundException_whenAddressNotFound() {
        SaveOrderRequest request = buildRequest(10L, 1);

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }

    private SaveOrderRequest buildRequestWithCoupon(long productId, int quantity, Long userCouponId) {
        SaveOrderRequest request = buildRequest(productId, quantity);
        ReflectionTestUtils.setField(request, "userCouponId", userCouponId);
        return request;
    }

    @Test
    @DisplayName("saveOrder - 정액 쿠폰 적용 시 할인된 금액으로 주문이 생성되고 쿠폰이 사용 처리된다")
    void saveOrder_shouldApplyFixedDiscountAndMarkCouponUsed() {
        SaveOrderRequest request = buildRequestWithCoupon(10L, 3, 1L);
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon coupon = Coupon.builder().id(10L).name("1000원 할인").discountType(DiscountType.FIXED).discountValue(1000L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Order savedOrder = Order.builder().id(100L).userId(1L).totalPrice(5000L).shippingAddressId(1L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(5000L); // 6000 - 1000
        assertThat(userCoupon.getIsUsed()).isTrue();
    }

    @Test
    @DisplayName("saveOrder - 퍼센트 쿠폰 적용 시 할인된 금액으로 주문이 생성된다")
    void saveOrder_shouldApplyPercentageDiscount() {
        SaveOrderRequest request = buildRequestWithCoupon(10L, 3, 1L);
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon coupon = Coupon.builder().id(10L).name("10% 할인").discountType(DiscountType.PERCENTAGE).discountValue(10L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Order savedOrder = Order.builder().id(100L).userId(1L).totalPrice(5400L).shippingAddressId(1L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(5400L); // 6000 * 90 / 100
    }

    @Test
    @DisplayName("saveOrder - 존재하지 않는 쿠폰이면 CouponNotFoundException을 던진다")
    void saveOrder_throwsCouponNotFoundException_whenCouponNotFound() {
        SaveOrderRequest request = buildRequestWithCoupon(10L, 1, 99L);

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(userCouponRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrder - 이미 사용된 쿠폰이면 CouponAlreadyUsedException을 던진다")
    void saveOrder_throwsCouponAlreadyUsedException_whenCouponAlreadyUsed() {
        SaveOrderRequest request = buildRequestWithCoupon(10L, 1, 1L);
        UserCoupon usedCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(true).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(usedCoupon));

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(CouponAlreadyUsedException.class);
    }

    @Test
    @DisplayName("saveOrder - 만료된 쿠폰이면 CouponExpiredException을 던진다")
    void saveOrder_throwsCouponExpiredException_whenCouponExpired() {
        SaveOrderRequest request = buildRequestWithCoupon(10L, 1, 1L);
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon expiredCoupon = Coupon.builder().id(10L).name("만료쿠폰").discountType(DiscountType.FIXED)
                .discountValue(1000L).expiresAt(LocalDateTime.now().minusDays(1)).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(expiredCoupon));

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(CouponExpiredException.class);
    }

    @Test
    @DisplayName("saveOrder - shippingAddressId가 null이면 기본 배송지를 자동으로 사용한다")
    void saveOrder_shouldUseDefaultShippingAddress_whenShippingAddressIdIsNull() {
        SaveOrderRequest request = new SaveOrderRequest();
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", 10L);
        ReflectionTestUtils.setField(itemRequest, "quantity", 1);
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));
        // shippingAddressId는 null

        ShippingAddress defaultAddress = ShippingAddress.builder().id(5L).userId(1L)
                .recipientName("홍길동").phone("010-1234-5678").address("서울시 강남구").zipCode("12345").build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING)
                .totalPrice(2000L).shippingAddressId(5L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.of(defaultAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getShippingAddressId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("saveOrder - shippingAddressId가 null이고 기본 배송지도 없으면 ShippingAddressNotFoundException을 던진다")
    void saveOrder_throwsShippingAddressNotFoundException_whenNoDefaultAddress() {
        SaveOrderRequest request = new SaveOrderRequest();
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", 10L);
        ReflectionTestUtils.setField(itemRequest, "quantity", 1);
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));
        // shippingAddressId는 null

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrder - 최소 주문 금액 미달이면 OrderAmountNotEnoughException을 던진다")
    void saveOrder_throwsOrderAmountNotEnoughException_whenAmountBelowMinimum() {
        SaveOrderRequest request = buildRequestWithCoupon(10L, 1, 1L); // totalPrice = 2000
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon coupon = Coupon.builder().id(10L).name("할인쿠폰").discountType(DiscountType.FIXED)
                .discountValue(1000L).minOrderAmount(5000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon));

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(OrderAmountNotEnoughException.class);
    }

    @Test
    @DisplayName("saveOrder - 포인트 사용 시 총액에서 사용 포인트가 차감된다")
    void saveOrder_shouldDeductPoints_whenUsePointsIsProvided() {
        SaveOrderRequest request = buildRequest(10L, 3); // totalPrice = 6000
        ReflectionTestUtils.setField(request, "usePoints", 1000L);
        UserPoint userPoint = UserPoint.builder().userId(1L).balance(2000L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();
        Order savedOrder = Order.builder().id(100L).userId(1L).totalPrice(5000L).shippingAddressId(1L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualTo(5000L); // 6000 - 1000
        assertThat(userPoint.getBalance()).isEqualTo(1000L); // 2000 - 1000
    }

    @Test
    @DisplayName("saveOrder - 포인트 잔액이 부족하면 InsufficientPointException을 던진다")
    void saveOrder_throwsInsufficientPointException_whenPointsNotEnough() {
        SaveOrderRequest request = buildRequest(10L, 1); // totalPrice = 2000
        ReflectionTestUtils.setField(request, "usePoints", 5000L);
        UserPoint userPoint = UserPoint.builder().userId(1L).balance(1000L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(shippingAddress));
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(InsufficientPointException.class);
    }
}
