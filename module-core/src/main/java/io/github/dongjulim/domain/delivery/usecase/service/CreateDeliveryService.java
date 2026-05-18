package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderAlreadyHasDeliveryException;
import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderNotCompletedException;
import io.github.dongjulim.domain.delivery.dto.CreateDeliveryRequest;
import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
import io.github.dongjulim.domain.delivery.usecase.CreateDeliveryUseCase;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateDeliveryService implements CreateDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    @Override
    public void createDelivery(CreateDeliveryRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new OrderNotCompletedException();
        }

        if (deliveryRepository.existsByOrderId(order.getId())) {
            throw new OrderAlreadyHasDeliveryException();
        }

        deliveryRepository.save(Delivery.builder()
                .orderId(order.getId())
                .address(request.getAddress())
                .trackingNumber(request.getTrackingNumber())
                .build());
    }
}
