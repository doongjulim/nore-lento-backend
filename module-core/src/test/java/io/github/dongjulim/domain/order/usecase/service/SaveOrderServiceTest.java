package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.OutOfStockException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.order.dto.OrderItemRequest;
import io.github.dongjulim.domain.order.dto.SaveOrderRequest;
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
    private UserLoader userLoader;

    @InjectMocks
    private SaveOrderService saveOrderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        product = Product.builder().id(10L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
    }

    @Test
    @DisplayName("saveOrder - 주문 항목 목록으로 주문이 정상적으로 생성된다")
    void saveOrder_shouldSaveOrderWithItems() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", 10L);
        ReflectionTestUtils.setField(itemRequest, "quantity", 3);

        SaveOrderRequest request = new SaveOrderRequest();
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));

        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(6000L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertThat(capturedOrder.getUserId()).isEqualTo(1L);
        assertThat(capturedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(capturedOrder.getTotalPrice()).isEqualTo(6000L); // 2000 * 3

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
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", 10L);
        ReflectionTestUtils.setField(itemRequest, "quantity", 3);

        SaveOrderRequest request = new SaveOrderRequest();
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));

        Order savedOrder = Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(6000L).build();
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(10).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        saveOrderService.saveOrder(request, "testuser");

        assertThat(stock.getQuantity()).isEqualTo(7); // 10 - 3
    }

    @Test
    @DisplayName("saveOrder - 재고가 부족하면 OutOfStockException을 던진다")
    void saveOrder_throwsOutOfStockException_whenStockInsufficient() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", 10L);
        ReflectionTestUtils.setField(itemRequest, "quantity", 5);

        SaveOrderRequest request = new SaveOrderRequest();
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));

        Stock stock = Stock.builder().id(1L).productId(10L).quantity(3).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(10000L).build());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(OutOfStockException.class);
    }

    @Test
    @DisplayName("saveOrder - 재고 정보가 없으면 StockNotFoundException을 던진다")
    void saveOrder_throwsStockNotFoundException_whenStockNotFound() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", 10L);
        ReflectionTestUtils.setField(itemRequest, "quantity", 1);

        SaveOrderRequest request = new SaveOrderRequest();
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(10L)).willReturn(Optional.empty());
        given(orderRepository.save(any(Order.class))).willReturn(
                Order.builder().id(100L).userId(1L).status(OrderStatus.PENDING).totalPrice(2000L).build());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(StockNotFoundException.class);
    }

    @Test
    @DisplayName("saveOrder - 존재하지 않는 상품이면 ProductNotFoundException을 던진다")
    void saveOrder_throwsProductNotFoundException_whenProductNotFound() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        ReflectionTestUtils.setField(itemRequest, "productId", 99L);
        ReflectionTestUtils.setField(itemRequest, "quantity", 1);

        SaveOrderRequest request = new SaveOrderRequest();
        ReflectionTestUtils.setField(request, "orderItems", List.of(itemRequest));

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveOrderService.saveOrder(request, "testuser"))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
