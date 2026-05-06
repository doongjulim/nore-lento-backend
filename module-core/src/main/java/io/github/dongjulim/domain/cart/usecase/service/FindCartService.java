package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.dto.FindCartResponse;
import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.cart.usecase.FindCartUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCartService implements FindCartUseCase {

    private final CartRepository cartRepository;
    private final UserLoader userLoader;

    @Override
    public FindCartResponse findCart(String username) {
        User user = userLoader.load(username);

        Optional<Cart> cart = cartRepository.findByUserId(user.getId());

        return cart.map(FindCartResponse::from)
                .orElseGet(FindCartResponse::empty);
    }
}
