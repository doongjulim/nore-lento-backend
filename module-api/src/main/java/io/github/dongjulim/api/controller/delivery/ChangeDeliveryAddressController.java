package io.github.dongjulim.api.controller.delivery;

import io.github.dongjulim.domain.delivery.dto.ChangeDeliveryAddressRequest;
import io.github.dongjulim.domain.delivery.usecase.ChangeDeliveryAddressUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChangeDeliveryAddressController {

    private final ChangeDeliveryAddressUseCase changeDeliveryAddressUseCase;

    @PatchMapping("/api/v2/deliveries/{id}/address")
    public ResponseEntity<Void> changeAddress(
            @PathVariable Long id,
            @RequestBody ChangeDeliveryAddressRequest request
    ) {
        changeDeliveryAddressUseCase.changeAddress(id, request.getAddress());
        return ResponseEntity.ok().build();
    }
}
