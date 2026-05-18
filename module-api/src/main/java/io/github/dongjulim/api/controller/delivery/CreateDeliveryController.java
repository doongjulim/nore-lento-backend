package io.github.dongjulim.api.controller.delivery;

import io.github.dongjulim.domain.delivery.dto.CreateDeliveryRequest;
import io.github.dongjulim.domain.delivery.usecase.CreateDeliveryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CreateDeliveryController {

    private final CreateDeliveryUseCase createDeliveryUseCase;

    @PostMapping("/api/v2/deliveries")
    public ResponseEntity<Void> createDelivery(
            @RequestBody @Valid CreateDeliveryRequest request
    ) {
        createDeliveryUseCase.createDelivery(request);
        return ResponseEntity.ok().build();
    }
}
