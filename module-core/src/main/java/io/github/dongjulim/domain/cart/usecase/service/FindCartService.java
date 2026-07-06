package io.github.dongjulim.domain.cart.usecase.service;

import io.github.dongjulim.domain.cart.dto.FindCartResponse;
import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.cart.usecase.FindCartUseCase;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCartService implements FindCartUseCase {

    private final CartRepository cartRepository;
    private final StockRepository stockRepository;
    private final UserLoader userLoader;

    @Override
    public FindCartResponse findCart(String username) {
        User user = userLoader.load(username);

        Optional<Cart> cart = cartRepository.findByUserId(user.getId());
        if (cart.isEmpty()) {
            return FindCartResponse.empty();
        }

        List<Long> productIds = cart.get().getCartItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        Map<Long, Integer> stockQuantities = stockRepository.findAllByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(Stock::getProductId, Stock::getQuantity));

        return FindCartResponse.from(cart.get(), stockQuantities);
    }
}
