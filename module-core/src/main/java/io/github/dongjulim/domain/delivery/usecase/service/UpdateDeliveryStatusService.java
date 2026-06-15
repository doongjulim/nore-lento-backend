package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.enums.DeliveryStatus;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
import io.github.dongjulim.domain.delivery.usecase.UpdateDeliveryStatusUseCase;
import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateDeliveryStatusService implements UpdateDeliveryStatusUseCase {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void updateDeliveryStatus(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.advance();

        Order order = orderRepository.findById(delivery.getOrderId())
                .orElseThrow(OrderNotFoundException::new);

        notificationRepository.save(Notification.builder()
                .userId(order.getUserId())
                .title(notificationTitle(delivery.getStatus()))
                .content(notificationContent(delivery.getStatus()))
                .build());
    }

    private String notificationTitle(DeliveryStatus status) {
        return status == DeliveryStatus.SHIPPING ? "배송 시작 알림" : "배송 완료 알림";
    }

    private String notificationContent(DeliveryStatus status) {
        return status == DeliveryStatus.SHIPPING
                ? "주문하신 상품의 배송이 시작되었습니다."
                : "주문하신 상품이 배달 완료되었습니다.";
    }
}
