package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.dto.UpdateCartItemRequest;
import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartItemRepository;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.cart.usecase.UpdateCartItemUseCase;
import io.github.dongjulim.domain.common.exception.CartItemNotFoundException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.common.exception.OutOfStockException;
import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCartItemService implements UpdateCartItemUseCase {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final StockRepository stockRepository;
    private final UserLoader userLoader;

    @Override
    public void updateCartItem(Long cartItemId, UpdateCartItemRequest request, String username) {
        User user = userLoader.load(username);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .filter(item -> item.getCartId().equals(cart.getId()))
                .orElseThrow(CartItemNotFoundException::new);

        Stock stock = stockRepository.findByProductId(cartItem.getProductId())
                .orElseThrow(StockNotFoundException::new);

        if (stock.getQuantity() < request.getQuantity()) {
            throw new OutOfStockException();
        }

        cartItem.updateQuantity(request.getQuantity());
    }
}
