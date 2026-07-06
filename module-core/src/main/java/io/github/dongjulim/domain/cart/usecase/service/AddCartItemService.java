package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.dto.AddCartItemRequest;
import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartItemRepository;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.cart.usecase.AddCartItemUseCase;
import io.github.dongjulim.domain.common.exception.OutOfStockException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddCartItemService implements AddCartItemUseCase {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserLoader userLoader;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Override
    public void addCartItem(AddCartItemRequest request, String username) {
        User user = userLoader.load(username);

        Product product = productRepository.findByIdAndDeleteCheckFalse(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        Stock stock = stockRepository.findByProductId(product.getId())
                .orElseThrow(StockNotFoundException::new);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .userId(user.getId())
                        .build()));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            int newTotalQuantity = existingItem.get().getQuantity() + request.getQuantity();
            if (stock.getQuantity() < newTotalQuantity) {
                throw new OutOfStockException();
            }
            existingItem.get().addQuantity(request.getQuantity());
        } else {
            if (stock.getQuantity() < request.getQuantity()) {
                throw new OutOfStockException();
            }
            CartItem cartItem = CartItem.builder()
                    .cartId(cart.getId())
                    .productId(product.getId())
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(cartItem);
        }
    }
}
