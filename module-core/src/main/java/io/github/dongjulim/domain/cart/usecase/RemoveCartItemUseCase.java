package io.github.dongjulim.domain.cart.usecase;

import java.util.List;

public interface RemoveCartItemUseCase {

    void removeCartItems(List<Long> cartItemIds, String username);
}
