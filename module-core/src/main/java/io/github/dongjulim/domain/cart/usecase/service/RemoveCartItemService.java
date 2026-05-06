package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartItemRepository;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.cart.usecase.RemoveCartItemUseCase;
import io.github.dongjulim.domain.common.exception.CartItemNotFoundException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveCartItemService implements RemoveCartItemUseCase {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserLoader userLoader;

    @Override
    public void removeCartItems(List<Long> cartItemIds, String username) {
        User user = userLoader.load(username);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);

        if (cartItems.size() != cartItemIds.size()) {
            throw new CartItemNotFoundException();
        }

        boolean allBelongToCart = cartItems.stream()
                .allMatch(item -> item.getCartId().equals(cart.getId()));

        if (!allBelongToCart) {
            throw new CartItemNotFoundException();
        }

        cartItemRepository.deleteAll(cartItems);
    }
}
