package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
import io.github.dongjulim.domain.delivery.usecase.UpdateDeliveryStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateDeliveryStatusService implements UpdateDeliveryStatusUseCase {

    private final DeliveryRepository deliveryRepository;

    @Override
    public void updateDeliveryStatus(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.advance();
    }
}
