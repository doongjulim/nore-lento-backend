package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.delivery.dto.FindDeliveryResponse;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
import io.github.dongjulim.domain.delivery.usecase.FindDeliveryUseCase;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindDeliveryService implements FindDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserLoader userLoader;

    @Override
    public FindDeliveryResponse findDelivery(Long orderId, String username) {
        User user = userLoader.load(username);

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(OrderNotFoundException::new);

        return deliveryRepository.findByOrderId(order.getId())
                .map(FindDeliveryResponse::from)
                .orElseThrow(DeliveryNotFoundException::new);
    }
}
