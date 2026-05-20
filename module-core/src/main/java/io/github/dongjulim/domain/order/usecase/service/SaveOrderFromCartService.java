package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.CartEmptyException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.order.usecase.SaveOrderFromCartUseCase;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveOrderFromCartService implements SaveOrderFromCartUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final UserLoader userLoader;

    @Override
    public void saveOrderFromCart(String username) {
        User user = userLoader.load(username);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new CartEmptyException();
        }

        long totalPrice = 0L;
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findByIdAndDeleteCheckFalse(cartItem.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            totalPrice += product.getPrice() * cartItem.getQuantity();
        }

        Order order = orderRepository.save(Order.builder()
                .userId(user.getId())
                .totalPrice(totalPrice)
                .build());

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findByIdAndDeleteCheckFalse(cartItem.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            Stock stock = stockRepository.findByProductId(product.getId())
                    .orElseThrow(StockNotFoundException::new);
            stock.decrease(cartItem.getQuantity());
            orderItemRepository.save(OrderItem.builder()
                    .orderId(order.getId())
                    .productId(product.getId())
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build());
        }

        cart.clearItems();
    }
}
