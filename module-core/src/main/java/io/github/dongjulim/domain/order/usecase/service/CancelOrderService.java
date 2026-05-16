package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.order.usecase.CancelOrderUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final UserLoader userLoader;

    @Override
    public void cancelOrder(Long orderId, String username) {
        User user = userLoader.load(username);

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(OrderNotFoundException::new);

        order.cancel();
    }
}
