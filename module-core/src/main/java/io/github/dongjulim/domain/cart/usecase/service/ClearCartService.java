package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.cart.usecase.ClearCartUseCase;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClearCartService implements ClearCartUseCase {

    private final CartRepository cartRepository;
    private final UserLoader userLoader;

    @Override
    public void clearCart(String username) {
        User user = userLoader.load(username);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        cart.clearItems();
    }
}
