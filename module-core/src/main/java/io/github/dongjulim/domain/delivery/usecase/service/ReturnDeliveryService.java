package io.github.dongjulim.domain.delivery.usecase.service;

import io.github.dongjulim.domain.common.exception.DeliveryNotFoundException;
import io.github.dongjulim.domain.delivery.entity.Delivery;
import io.github.dongjulim.domain.delivery.repository.DeliveryRepository;
import io.github.dongjulim.domain.delivery.usecase.ReturnDeliveryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnDeliveryService implements ReturnDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Override
    public void returnDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        delivery.returnDelivery();
    }
}
